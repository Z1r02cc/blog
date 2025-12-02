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
        <li class="active"><i class="icon-search"></i> ${articleTitle!}</li>
    </ol>
    <div class="panel">
        <div class="panel-body">
            <#if articleIPage??>
                <#list  articleIPage.list as articleVo>
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
                <!-- 修改这里：移除多余的panel包装 -->
                <div class="col-sm-12" style="padding: 0;text-align: center; margin-top: 20px;">
                    <ul class="pager ">
                        <li class="previous"><a id="previous" href="#">«</a></li>
                        <li><span>第${articleIPage.pageNumber}页/共${articleIPage.totalPage}页</span></li>
                        <li class="next"><a id="next">»</a></li>
                        <li class="next"><a><input style="height: 1.5em;width: 4em;" id="toPage"
                                                   type="number" oninput="value=value.replace(/[^\d]/g,'')"></a></li>
                        <li class="next"><a onclick="toPage()" data-toggle="popover"
                                            data-placement="right"
                                            data-content="请输入正确的页码！">跳转</a></li>
                    </ul>
                </div>

            <#else >
                <#include "./import/nullData.ftl">
            </#if>
        </div>
    </div>
</div>


<script>
    const tip = $('[data-toggle="popover"]')
    if (checkNull(${articleIPage.pageNumber})) {
        const currentPage = ${articleIPage.pageNumber}
        const next = document.getElementsByClassName("next")[0]
        const pre = document.getElementsByClassName("previous")[0]
        if (currentPage >= ${articleIPage.totalPage}) {
            next.classList.add("disabled")
        } else {
            next.classList.contains("disabled") ? next.classList.remove("disabled") : null
            next.children[0].setAttribute("href", "/article/search?pageNumber=" + ${articleIPage.pageNumber+1} + "<#if articleTitle??>&articleTitle=${articleTitle!}</#if>")
        }
        if (currentPage <= 1) {
            pre.classList.add("disabled")
        } else {
            pre.classList.contains("disabled") ? next.classList.remove("disabled") : null
            pre.children[0].setAttribute("href", "/article/search?pageNumber=" + ${articleIPage.pageNumber-1} + "<#if articleTitle??>&articleTitle=${articleTitle!}</#if>")
        }

    }

    function toPage() {
        const input = document.getElementById("toPage")
        console.info(input.value)
        if (!checkNull(input.value) || input.value < 1 || input.value > ${articleIPage.totalPage}) {
            tip.popover("show")
            setTimeout(function () {
                tip.popover("hide")
            }, 4000)
        } else {
            window.location.href = "/article/search?pageNumber=" + pageNumber + "<#if articleTitle??>&articleTitle=${articleTitle!}</#if>";
        }
    }
</script>
<#include "./import/viewBottom.ftl">
