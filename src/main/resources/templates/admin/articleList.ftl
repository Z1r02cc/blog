<#include "./import/adminTop.ftl">

<#if articlePage?? && articlePageSize gt 0 >
    <form class="form-horizontal" action="/zer02/article/list" method="get">
        <div class="form-group">
            <label for="articleTitle" class="col-sm-1">文章标题</label>
            <div class="col-sm-3">
                <input type="text" value="${articleTitle!}" class="form-control" name="articleTitle" id="articleTitle" placeholder="文章标题">
            </div>
            <div class="cpl-sm-2">
                <button type="submit" class="btn btn-success"><i class="icon-search"></i> 搜索</button>
                <a href="/zer02/article/list" class="btn btn-success"><i class="icon-search"></i> 全部</a>
            </div>
        </div>
    </form>
    <div class="panel">
        <div class="panel-body">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>发布时间</th>
                    <th>发布者</th>
                    <th>文章标题</th>
                    <th>浏览数</th>
                    <th>点赞数</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list articlePage.records as articleVo>
                    <tr>
                        <td>
                            ${(articleVo.articleCreateTime?string("yyyy-MM-dd HH:mm:ss"))}
                        </td>
                        <td>${(articleVo.userName)!}</td>
                        <td>
                            ${(articleVo.articleTitle)!}
                        </td>
                        <td>${(articleVo.articleBrowseTime)!}</td>
                        <td>${(articleVo.articlePraiseNum)!}</td>
                        <td>
                            <div style="text-align: right">
                                <button onclick="delArticle('${(articleVo.articleId)!}')" type="button"
                                        class="btn btn-mini"><i
                                            class="icon-remove"></i> 删除
                                </button>
                                <a target="_blank" href="/article?articleId=${(articleVo.articleId)!}" class="btn btn-mini"><i class="icon-eye-open"></i> 查看</a>
                            </div>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

    <div class="panel">
        <div class="panel-body" style="padding: 0;">
            <div class="col-sm-12" style="padding: 0;text-align: center;">
                <ul class="pager ">
                    <li class="previous"><a id="previous" href="#">«</a></li>
                    <li><span>第${articlePage.current}页/共${articlePage.pages}页</span></li>
                    <li class="next"><a id="next">»</a></li>
                    <li class="next"><a><input style="height: 1.5em;width: 4em;" id="toPage"
                                               type="number" oninput="value=value.replace(/[^\d]/g,'')"></a></li>
                    <li class="next"><a onclick="toPage()" data-toggle="popover"
                                        data-placement="right"
                                        data-content="请输入正确的页码！">跳转</a></li>
                </ul>
            </div>
        </div>
    </div>

<#else >
    <#include "./import/nullData.ftl">
</#if>


<script>
    function delArticle(articleId) {
        if (confirm("是否删除")) {
            if (!checkNotNull(articleId)) {
                zuiMsg('程序出错，请刷新页面重试');
                return;
            }
            $.post("/zer02/article/del", {
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
    const tip = $('[data-toggle="popover"]')
    if (checkNull(${articlePage.current})) {
        const currentPage = ${articlePage.current}
        const next = document.getElementsByClassName("next")[0]
        const pre = document.getElementsByClassName("previous")[0]
        if (currentPage >= ${articlePage.pages}) {
            next.classList.add("disabled")
        } else {
            next.classList.contains("disabled") ? next.classList.remove("disabled") : null
            next.children[0].setAttribute("href", "/zer02/article/list?PageNum=${articlePage.current+1}" + "<#if (articleTitle??) >&articleTitle=${articleTitle}</#if>")
        }
        if (currentPage <= 1) {
            pre.classList.add("disabled")
        } else {
            pre.classList.contains("disabled") ? next.classList.remove("disabled") : null
            pre.children[0].setAttribute("href", "/zer02/article/list?PageNum=${articlePage.current-1}" + "<#if (articleTitle??) >&articleTitle=${articleTitle}</#if>")
        }

    }

    function toPage() {
        const input = document.getElementById("toPage")
        console.info(input.value)
        if (!checkNull(input.value) || input.value < 1 || input.value > ${articlePage.pages}) {
            tip.popover("show")
            setTimeout(function () {
                tip.popover("hide")
            }, 4000)
        } else {
            window.location.href = 'http://localhost:8080/zer02/article/list?PageNum=' + input.value + "<#if (articleTitle??) >&userName=${articleTitle}</#if>";
        }
    }

</script>

<#include "./import/bottom.ftl">