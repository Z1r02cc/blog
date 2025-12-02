<#include "./import/top.ftl">
<#include "./import/navbar.ftl">
<style>
    .comments-list {
        margin-left: 25px;
    }

    .hoverPalm {
        cursor: pointer;
    }

    .loadMoreCommentBtn:hover {
        color: red;
    }

    .llBox {
        border-radius: 3px;
        border: 1px solid #D7D7D7;
        padding: 1px;
    }

    .goodCommentBgColor {
        background-color: #2c8931;
    }
</style>


<div class = "col-xs-12">
    <div class = "panel">
        <div class = "panel-body" style = "padding-top:0;">
            <article class = "article">
                <header>
                    <h1 class = "text-center" style = "margin-top: 5px;">
                        ${(article.articleTitle)!}
                        <br/>
                        <small>
                            发布时间: ${(article.articleCreateTime)?string("yyyy-MM-dd HH:mm:ss")}
                        </small>
                    </h1>
                    <dl class = "dl-inline">
                        <dd><i class = "icon-user"></i>发布者:${(article.userName)!}</dd>
                        <dt>
                        <dd class = "pull-right">
                            <span class = "label label-info">
                                <i class = "icon-eye-open"></i> ${(article.articleBrowseTime)!}
                            </span> |
                            <span class = "label label-success hoverPalm"
                                  onclick = "articlePraise('${(article.articleId)!}')">
                                <i class = "icon-thumbs-up"></i> <span
                                        id = "articlePraise">${(article.articlePraiseNum)!}</span>
                            </span>
                        </dd>
                    </dl>
                </header>
                <section class = "content">
                    <p>${(article.articleContext)!}</p>
                </section>
                <footer>
                    <div class = "col-xs-12" id = "commentListBox">
                    </div>
                    <div class = "form-group col-xs-12" style = "margin-top: 25px;padding: 2px;">
                        <span onclick = "loadMoreComment()" id = "loadMoreCommentBtn"
                              class = "pull-right hoverPalm loadMoreCommentBtn">更多评论<i
                                    class = "icon-double-angle-down"></i> </span>
                        <hr/>
                        <label for = "commentContent" id = "commentInfo"><#if user??>评论:<#else>请先登录</#if></label>

                        <textarea id = "commentContent" name = "commentContent" maxlength = "1500"
                                  class = "form-control new-comment-text" rows = "2"
                                  placeholder = "<#if user??>留下一个评论呗~<#else>请先登录呀</#if>"
                                  <#if user??><#else>disabled = "disabled"</#if>></textarea>

                        <button type = "button" id="commentBtn" onclick = "saveComment('${(article.articleId)!}')"
                                class = "btn btn-success pull-right"
                                style = "margin-top: 10px;" <#if user??><#else>disabled = "disabled"</#if>>评论
                        </button>
                    </div>
                </footer>
            </article>
        </div>
    </div>
</div>
<script>
    let articleId = '${(article.articleId)!}';
    function saveComment(articleId) {
        let commentContent = $('#commentContent').val();
        if (!checkNotNull(commentContent) || commentContent.length < 1) {
            zuiMsg("请填写评论");
            return;
        }
        $.post("/user/saveComment", {
                articleId: articleId,
                commentContent: commentContent
            },
            function (data) {
                if (data.code === 200) {
                    zuiMsg("评论成功~");
                    $('#commentContent').val("");
                    console.info(data.data.commentTime)
                    addCommentItem(data.data.commentTime, data.data.userName, data.data.commentContent, data.data.commentId, 1)
                    return;
                }
                zuiMsg(data.message);
            });
    }


    function articlePraise(articleId) {
        $.post("/articlePraise", {
                articleId: articleId
            },
            function (data) {
                if (data.code == 200) {
                    let articlePraise = $("#articlePraise").text();
                    $("#articlePraise").text(++articlePraise);
                    new $.zui.Messager(data.message, {
                        type: 'success',
                        placement: 'center'
                    }).show();
                    return;
                }
                zuiMsg(data.message);
            });
    }


    $(function () {
        $("#loadMoreCommentBtn").hide();
        $.post("/comment/list", {
                articleId: articleId
            },
            function (data) {
                if (data.code == 200) {
                    let commentList = data.data.list;
                    pageNumber = data.data.pageNumber;
                    totalPage = data.data.totalPage;
                    if (pageNumber < totalPage) {
                        $("#loadMoreCommentBtn").show();
                    } else {
                        $("#loadMoreCommentBtn").hide();
                    }
                    for (let i = 0; i < commentList.length; i++) {
                        addCommentItem(commentList[i].commentTime, commentList[i].userName, commentList[i].commentContent, commentList[i].commentId, 0)
                    }
                    return;
                }
                zuiMsg("获取评论失败！");
            });
    })


    function loadMoreComment() {
        if (pageNumber >= totalPage) {
            return;
        }
        $.post("/comment/list", {
                articleId: articleId,
                pageNumber: ++pageNumber
            },
            function (data) {
                if (data.code === 200) {
                    let commentList = data.data.list;
                    pageNumber = data.data.pageNumber;
                    totalPage = data.data.totalPage;
                    if (pageNumber < totalPage) {
                        $("#loadMoreCommentBtn").show();
                    } else {
                        $("#loadMoreCommentBtn").hide();
                    }
                    for (let i = 0; i < commentList.length; i++) {
                        addCommentItem(commentList[i].commentTime, commentList[i].userName, commentList[i].commentContent, commentList[i].commentId, 0)
                    }
                    return;
                }
                zuiMsg("获取评论失败！");
            });
    }

    function addCommentItem(commentTime,  userName, commentContent, commentId, isNewComment,) {
        let commentHtml =
            '<div class="content" style="border-bottom:1px dashed #D7D7D7;margin-bottom: 20px;margin-top: 10px;padding-bottom: 5px;">' +
            '<div class="pull-right text-muted"><i class="icon-time"></i> ' + commentTime + '</div> ' +
            '<div>' +
            '<strong>' +
            '<i class="icon-user"></i>' + userName + ' 说：' +
            '</strong>' +
            '</div>' +
            '<div class="text">&emsp;&emsp;' + commentContent + '</div>' +
            '<div class="actions">'
        commentHtml +=
            '</div>' +
            '<div class="comments-list" id="commentReplyBox"></div>' +
            '</div>';

        if (isNewComment == 1) {
            $("#commentListBox").prepend(commentHtml);
        } else {
            $("#commentListBox").append(commentHtml);
        }
    }

</script>
<#include "./import/viewBottom.ftl">
