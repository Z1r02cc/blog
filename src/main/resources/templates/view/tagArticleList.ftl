<#include "./import/top.ftl">
<#include "./import/navbar.ftl">
<style>
    .cardimg {
        width: 100%;
    }
</style>
<div class="col-xs-12">
    <ol class="breadcrumb" style="margin-bottom: 5px;">
        <li><a href="/"><i class="icon-home"></i> 首页</a></li>
        <li class="active">${articleTagName!}</li>
    </ol>
    <div class="panel"> 
        <div class="panel-body">
            <#if articleVoIPage??  >
                <#list  articleVoIPage.list as articleVo>
                    <div class="col-xs-6 col-sm-3" style="padding: 2px;">
                        <div class="card">
                            <a href="/article?articleId=${(articleVo.articleId)!}" style="text-decoration:none">
                                <img class="cardimg" src="${(articleVo.articleCoverUrl)!}"
                                     alt="${(articleVo.articleTitle)!}">
                                <div class="card-heading"><strong>${(articleVo.articleTitle)!}</strong></div>
                                <div class="card-content text-muted">
                                    <i class="icon-time"></i>
                                    ${(articleVo.articleCreateTime)?string("yyyy-MM-dd HH:mm:ss")}
                                </div>

                                <div class="card-actions">
                        <span class="label label-info">
                            <i class="icon-eye-open"></i> ${(articleVo.articleBrowseTime)!}
                        </span> |
                                    <span class="label label-success">
                            <i class="icon-thumbs-up"></i> ${(articleVo.articlePraiseNum)!}
                        </span>
                                </div>
                            </a>
                        </div>
                    </div>
                </#list>


            <#else >
                <#include "./import/nullData.ftl">
            </#if>
        </div>
    </div>
</div>


<script>
    const tip = $('[data-toggle="popover"]')
    if (checkNull(${articleVoIPage.pageNumber})) {
        const currentPage = ${articleVoIPage.pageNumber}
        const next = document.getElementsByClassName("next")[0]
        const pre = document.getElementsByClassName("previous")[0]
        if (currentPage >= ${articleVoIPage.totalPage}) {
            next.classList.add("disabled")
        } else {
            next.classList.contains("disabled") ? next.classList.remove("disabled") : null
            next.children[0].setAttribute("href", "/tag/article/?pageNumber=" + ${articleVoIPage.pageNumber+1} + "<#if articleTagId??>&articleTagId=${articleTagId!}</#if>")
        }
        if (currentPage <= 1) {
            pre.classList.add("disabled")
        } else {
            pre.classList.contains("disabled") ? next.classList.remove("disabled") : null
            pre.children[0].setAttribute("href", "/tag/article/?pageNumber=" + ${articleVoIPage.pageNumber-1} + "<#if articleTagId??>&articleTagId=${articleTagId!}</#if>")
        }

    }

    function toPage() {
        const input = document.getElementById("toPage")
        console.info(input.value)
        if (!checkNull(input.value) || input.value < 1 || input.value > ${articleVoIPage.totalPage}) {
            tip.popover("show")
            setTimeout(function () {
                tip.popover("hide")
            }, 4000)
        } else {
            window.location.href = "/tag/article/?pageNumber=" + pageNumber + "<#if articleTagId??>&articleTagId=${articleTagId!}</#if>";
        }
    }
</script>
<#include "./import/viewBottom.ftl">
