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
            <header>
                <h1 class="text-center">
                    留言面板
                </h1>
                <p class="text-center">在这里留言任何对这个网站的意见或感觉吧！</p>
                <br/>
            </header>
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

                    <button type = "button" id="commentBtn" onclick = "saveComment()"
                            class = "btn btn-success pull-right"
                            style = "margin-top: 10px;" <#if user??><#else>disabled = "disabled"</#if>>评论
                    </button>
                </div>
            </footer>
        </div>
    </div>
</div>
<script>
    function saveComment() {
        let commentContent = $('#commentContent').val();
        if (!checkNotNull(commentContent) || commentContent.length < 1) {
            zuiMsg("请填写评论");
            return;
        }
        $.post("/user/leaveMessage", {
                leaveMessage: commentContent
            },
            function (data) {
                if (data.code === 200) {
                    zuiMsg("留言成功~");
                    $('#commentContent').val("");
                    console.info(data.data.leaveMessageTime)
                    addCommentItem(data.data.leaveMessageTime, data.data.userName, data.data.leaveMessageText, data.data.leaveMessageId, 1)
                    return;
                }
                zuiMsg(data.message);
            });
    }


    $(function () {
        $("#loadMoreCommentBtn").hide();
        $.post("/leaveMessage/list", {
            },
            function (data) {
                if (data.code == 200) {
                    let leaveMessageList = data.data.list;
                    pageNumber = data.data.pageNumber;
                    totalPage = data.data.totalPage;
                    if (pageNumber < totalPage) {
                        $("#loadMoreCommentBtn").show();
                    } else {
                        $("#loadMoreCommentBtn").hide();
                    }
                    for (let i = 0; i < leaveMessageList.length; i++) {
                        addCommentItem(leaveMessageList[i].leaveMessageTime, leaveMessageList[i].userName, leaveMessageList[i].leaveMessageText, leaveMessageList[i].leaveMessageId, 0)
                    }
                    return;
                }
                zuiMsg("获取留言失败！");
            });
    })


    function loadMoreComment() {
        if (pageNumber >= totalPage) {
            return;
        }
        $.post("/leaveMessage/list", {
                pageNumber: ++pageNumber
            },
            function (data) {
                if (data.code === 200) {
                    let leaveMessageList = data.data.list;
                    pageNumber = data.data.pageNumber;
                    totalPage = data.data.totalPage;
                    if (pageNumber < totalPage) {
                        $("#loadMoreCommentBtn").show();
                    } else {
                        $("#loadMoreCommentBtn").hide();
                    }
                    for (let i = 0; i < leaveMessageList.length; i++) {
                        addCommentItem(leaveMessageList[i].leaveMessageTime, leaveMessageList[i].userName, leaveMessageList[i].leaveMessageText, leaveMessageList[i].leaveMessageId, 0)
                    }
                    return;
                }
                zuiMsg("获取留言失败！");
            });
    }
    /**
     * 评论样式
     * @param commentTime
     * @param userName
     * @param commentContent
     * @param commentId
     * @param isNewComment
     */
    function addCommentItem(commentTime,userName, commentContent, commentId, isNewComment,) {
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
