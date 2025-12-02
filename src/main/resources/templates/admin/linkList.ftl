<#include "./import/adminTop.ftl">

<#if linkList?? && linkList?size gt 0 >
    <form class="form-horizontal" action="/zer02/article/list" method="get">
    </form>
    <div class="panel">
        <div class="panel-body">
            <button onclick="addLink()" class="btn btn-mini"><i class="icon-plus"></i> 添加</button>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Logo</th>
                    <th>标题</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list linkList as link>
                    <tr>
                        <td>
                            <img src="${(link.getLinkLogoUrl())!'/img/null.png'}" alt="Logo">
                        </td>
                        <td>${(link.getLinkTitle())!}</td>
                        <td>
                            ${(link.getLinkCreateTime())?string("yyyy-MM-dd HH:mm:ss")}
                        </td>
                        <td>
                            <button onclick="updateLink('${(link.linkId)!}',
                                    '${(link.linkTitle)!}',
                                    '${(link.linkUrl)!}',
                                    '${(link.linkLogoUrl)!}')"
                                    type="button" class="btn btn-mini"><i class="icon-cog"></i> 修改
                            </button>
                            <button onclick="delLink('${(link.linkId)!}')" type="button" class="btn btn-mini"><i
                                        class="icon-remove"></i> 删除
                            </button>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="linkUpdateModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="/zer02/link/updateOrAdd" method="post">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                                    class="sr-only">关闭</span></button>
                        <h4 class="modal-title" id="linkTitleTag">修改友联</h4>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="linkId" id="linkId">
                        <div class="form-group">
                            <label for="linkTitle">名称：</label>
                            <input type="text" class="form-control" name="linkTitle" id="linkTitle"
                                   placeholder="名称">
                        </div>
                        <div class="form-group">
                            <label for="linkLogoUrl">Logo：</label>
                            <input type="text" class="form-control" id="linkLogoUrl" name="linkLogoUrl"
                                   placeholder="Logo地址">
                        </div>
                        <div class="form-group">
                            <label for="linkUrl">链接：</label>
                            <input type="text" class="form-control" id="linkUrl" name="linkUrl"
                                   placeholder="链接">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" onclick="linkAddOrUpdateAction()">保存</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

<#else >
    <#include "./import/nullData.ftl">
</#if>


<script>

    function addLink() {
        $("#linkId").val("");
        $("#linkTitle").val("");
        $("#linkUrl").val("");
        $("#linkLogoUrl").val("");
        $("#linkTitleTag").text("添加友联");
        $('#linkUpdateModal').modal('toggle', 'center');
    }

    function updateLink(linkId, linkTitle, linkUrl, linkLogoUrl) {
        $("#linkTitleTag").text("修改友联");
        $("#linkId").val(linkId);
        $("#linkTitle").val(linkTitle);
        $("#linkUrl").val(linkUrl);
        $("#linkLogoUrl").val(linkLogoUrl);
        $('#linkUpdateModal').modal('toggle', 'center');
    }

    function linkAddOrUpdateAction() {
        let linkId = $("#linkId").val();
        let linkTitle = $("#linkTitle").val();
        let linkUrl = $("#linkUrl").val();
        let linkLogoUrl = $("#linkLogoUrl").val();
        console.info("啊啊啊啊")
        if (!checkNotNull(linkTitle) || !checkNotNull(linkUrl)) {
            zuiMsg("数据填写不完整");
            return;
        }
        $.post("/zer02/link/updateOrAdd", {
                linkId: linkId,
                linkTitle: linkTitle,
                linkUrl: linkUrl,
                linkLogoUrl: linkLogoUrl
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

    function delLink(linkId) {
        if (confirm("是否删除")) {
            if (!checkNotNull(linkId)) {
                new $.zui.Messager('程序出错，请刷新页面重试', {
                    type: 'warning',
                    placement: 'center'
                }).show();
                return;
            }
            $.post("/zer02/link/del", {
                    linkId: linkId
                },
                function (data) {
                    if (data.code == 200) {
                        alert(data.message)
                        location.reload();
                        return;
                    }
                    new $.zui.Messager(data.message, {
                        type: 'warning',
                        placement: 'center'
                    }).show();
                });
        }

    }



</script>

<#include "./import/bottom.ftl">