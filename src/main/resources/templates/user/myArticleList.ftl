<#include "./import/top.ftl">
<#include "./import/navbar.ftl">
<style>
    .card img {
        width: 100%;
    }
</style>
<div class="col-xs-12">
    <#include "./import/userManagerNav.ftl">
    <div class="panel col-xs-12 col-sm-9" style="padding: 1px;">
        <div class="panel-heading">
            <i class="icon-book"></i>我的文章
        </div>
        <div class="panel-body">
            <#if articleVoIPage?? >
                <#list articleVoIPage.records as articleVo>
                    <div class="col-xs-6 col-sm-4" style="padding: 2px;">
                        <div class="card">
                            <a href="/article?articleId=${(articleVo.articleId)!}" style="text-decoration:none">
                                <img class="cardimg" src="${(articleVo.articleCoverUrl)!}"
                                     alt="${(articleVo.articleTitle)!}">
                                <div class="card-heading"><strong>${(articleVo.articleTitle)!}</strong></div>
                                <div class="card-content text-muted">
                                    <i class="icon-time"></i>
                                    ${(articleVo.articleCreateTime)?string("yyyy-MM-dd HH:mm:ss")}
                                </div>
                            </a>
                            <div class="card-actions">
                                <span class="label label-primary"><i
                                            class="icon-eye-open"></i> ${(articleVo.articleBrowseTime)!}</span>
                                <span class="label" onclick="updateArticle('${(articleVo.articleId)!}')">
                                修改
                            </span>
                                <span class="label" onclick="delArticle('${(articleVo.articleId)!}')">
                                删除
                            </span>
                            </div>
                        </div>
                    </div>
                </#list>

                <!-- 修改这里：去掉多余的panel包装 -->
                <div class="col-sm-12" style="padding: 0;text-align: center; margin-top: 20px;">
                    <ul class="pager ">
                        <li class="previous"><a id="previous" href="#">«</a></li>
                        <li><span>第${articleVoIPage.current}页/共${articleVoIPage.pages}页</span></li>
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
    function updateArticle(articleId) {
        window.location.href = "/user/publishArticle?articleId=" + articleId;
    }

    function delArticle(articleId) {
        if (confirm("删除文章将会删除文章的所有评论，是否删除？")) {
            $.post("/user/delArticle", {
                    articleId: articleId
                },
                function (data) {
                    if (data.code == 200) {
                        alert(data.message)
                        location.reload();
                        return;
                    }
                    zuiMsg(data.message);
                });
        }
    }

    if (checkNull(${articleVoIPage.current})) {
        const currentPage = ${articleVoIPage.current}
        const next = document.getElementsByClassName("next")[0]
        const pre = document.getElementsByClassName("previous")[0]
        if (currentPage >= ${articleVoIPage.pages}) {
            next.classList.add("disabled")
        } else {
            next.classList.contains("disabled") ? next.classList.remove("disabled") : null
            next.children[0].setAttribute("href", "/user/myArticleList/list?PageNum=${articleVoIPage.current+1}" )
        }
        if (currentPage <= 1) {
            pre.classList.add("disabled")
        } else {
            pre.classList.contains("disabled") ? next.classList.remove("disabled") : null
            pre.children[0].setAttribute("href", "/user/myArticleList/list?PageNum=${articleVoIPage.current-1}" )
        }
    }

    function toPage() {
        const input = document.getElementById("toPage")
        console.info(input.value)
        if (!checkNull(input.value) || input.value < 1 || input.value > ${articleVoIPage.pages}) {
            tip.popover("show")
            setTimeout(function () {
                tip.popover("hide")
            }, 4000)
        } else {
            window.location.href = 'http://localhost:8080/user/myArticleList/list?PageNum=' + input.value
        }
    }
</script>
<#include "./import/viewBottom.ftl">
