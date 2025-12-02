<#include "import/top.ftl">
<#include "import/navbar.ftl">
<style>
    .col2Padding {
        padding-left: 2px;
        padding-right: 2px;
    }

    .articleTagStyle {
        color: #ffffff;
    }

    .articleTagStyle:hover {
        font-weight: bold;
    }
    .message{
        width: auto;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-bottom: 0;
    }
</style>

<div class="container">
    <div class="col-xs-12  col-sm-9 col2Padding">
        <div class="panel">
            <div class="panel-body">
                <#list articleList as indexArticle>
                    <div class="col-xs-6 col-sm-4" style="padding: 2px;">
                        <div class="card">
                            <a href="/article?articleId=${(indexArticle.articleId)!}"
                               style="text-decoration:none">
                                <img class="cardImg" src="${(indexArticle.articleCoverUrl)!} "
                                     alt="${(indexArticle.articleTitle)!}">
                                <div class="card-heading"><strong>${(indexArticle.articleTitle)!}</strong>
                                </div>
                                <div class="card-content text-muted">
                                    <i class="icon-time"></i>
                                    ${(indexArticle.articleCreateTime)?string("yyyy-MM-dd")}
                                </div>

                                <div class="card-actions">
                                            <span class="label label-info">
                                                <i class="icon-eye-open"></i> ${(indexArticle.articleBrowseTime)!}
                                            </span> |
                                    <span class="label label-success">
                                                <i class="icon-thumbs-up"></i> ${(indexArticle.articlePraiseNum)!}
                                            </span>
                                </div>
                            </a>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </div>

    <div class="col-xs-12  col-sm-3 col2Padding">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <a style="color: white" href="/leaveMessage">最新留言</a>
            </div>
            <div class="panel-body">
                <ul class="list-group">
                    <#if leaveMessageList?? && leaveMessageList?size gt 0 >
                        <#list leaveMessageList as leaveMessage>
                            <li class="list-group-item">
                                <p class="message">${leaveMessage.leaveMessageText}</p>
                            </li>
                        </#list>
                    </#if>
                </ul>
            </div>
        </div>

        <div class="panel panel-primary">
            <div class="panel-heading">
                标签
            </div>
            <div class="panel-body" id="articleTagListBox">
                <#if articleTagList??  >
                    <#list articleTagList as articleTag>
                        <span class="label label-badge"><a class="articleTagStyle" style="text-decoration: none;"
                                                           href="/tag/article?articleTagId=${(articleTag.articleTagId)!}">${(articleTag.articleTagName)!}</a></span>
                    </#list>
                </#if>
            </div>
        </div>

        <div class="panel panel-primary">
            <div class="panel-heading">
                🔥 热门文章
            </div>
            <div class="panel-body">
                <ul class="list-group">
                    <#if articleHotList?? >
                        <#list articleHotList as articleHot>
                            <li class="list-group-item">
                                <a style="text-decoration: none;"
                                   href="/article?articleId=${(articleHot.articleId)!}"><p class="message">${(articleHot.articleCreateTime)?string("MM-dd")}
                                        ：${(articleHot.articleTitle)!}</p></a>
                            </li>
                        </#list>
                    </#if>
                </ul>
            </div>
        </div>


    </div>
</div>


<script>

    $(function () {
        let labelClassList = ["label-badge", "label-primary", "label-success", "label-info", "label-warning", "label-danger"];
        let articleTags = $("#articleTagListBox span");
        for (let i = 0; i < articleTags.length; i++) {
            $(articleTags[i]).addClass(labelClassList[Math.floor(Math.random() * labelClassList.length)]);
        }
    })

</script>

<#include "import/viewBottom.ftl">
