<#include "./import/adminTop.ftl">
<div class="panel">
    <form class="form-horizontal" action="/zer02/user/list" method="get">
        <div class="form-group" style="margin-top: 1em">
            <label for="userName" class="col-sm-1">查询用户：</label>
            <div class="col-sm-2 ">
                <input type="text" class="form-control" name="userName" id="userName" placeholder="用户名">
            </div>
            <div class="col-sm-1">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>
            <div class="col-sm-1 col-sm-offset-6">
                <a href="/zer02/user/list" class="btn btn-primary">查询全部</a>
            </div>
        </div>
    </form>
</div>
<#if userPage?? && userPageSize gt 0 >
    <div class="panel">
        <div class="panel-body">
            <table class="table">
                <thead>
                <tr>
                    <th>用户名</th>
                    <th>注册邮箱</th>
                    <th>注册时间</th>
                    <th>是否冻结</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list userPage.records as user>
                    <tr>
                        <td>${(user.userName)!}</td>
                        <td>${(user.userEmail)!}</td>
                        <td>${(user.userRegisterTime)?string("yyyy-MM-dd HH:mm:ss")}</td>
                        <td><#if user.userFreezing == 0>
                                <span class="label label-success">正常</span>
                            <#else >
                                <span class="label label-danger">冻结</span>
                            </#if></td>
                        <td>
                            <button onclick="userUpdate('${(user.userId)!}','${(user.userName)!}','${(user.userFreezing)!}','${(user.userEmail)!}')" class="btn btn-mini">修改</button>
                            <button onclick="delUser('${user.userId}')" class="btn btn-mini">删除</button>
                        </td>
                    </tr>
                </#list>

                </tbody>

            </table>

            <ul class="pager ">
                <li class="previous"><a id="previous" href="#">«</a></li>
                <li><span>第${userPage.current}页/共${userPage.pages}页</span></li>
                <li class="next"><a id="next">»</a></li>
                <li class="next"><a><input style="height: 1.5em;width: 4em;" id="toPage"
                                           type="number" oninput="value=value.replace(/[^\d]/g,'')"></a></li>
                <li class="next"><a onclick="toPage()" data-toggle="popover"
                                    data-placement="right"
                                    data-content="请输入正确的页码！">跳转</a></li>
            </ul>
        </div>

    </div>

<#else >
    <div class="panel">
        <div class="panel-body">
            <div style="text-align: center;">
                <h3><i class="icon icon-ban-circle"></i></h3>
                <h3>暂无数据</h3>
            </div>
        </div>
    </div>
</#if>

<div class="modal fade" id="userUpdateModel">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="/zer02/user/update" method="post">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title">修改用户</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="userId" id="userId">
                    <div class="form-group">
                        <label for="exampleInputAccount1">用户名:</label>
                        <input type="text" class="form-control" disabled="disabled" id="userNameUpdate" placeholder="用户名">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">密码:</label>
                        <input type="password" class="form-control" id="userPassword" placeholder="密码">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputPassword1">邮箱:</label>
                        <input type="text" class="form-control" id="userEmail" placeholder="密码">
                    </div>
                    <div class="form-group">
                        <label for="exampleInputMoney1">是否冻结</label>
                        <div class="input-group">
                            <label class="radio-inline">
                                <input type="radio" name="userFreezing" value="0"> 正常账号
                            </label><label class="radio-inline">
                                <input type="radio" name="userFreezing" value="1"> 冻结账号
                            </label>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="userUpdateSave()">保存</button>
                </div>
            </form>
        </div>
    </div>
</div>

<#include "./import/bottom.ftl">
<script>function userUpdateSave() {
        let userId= $("#userId").val()
        let userName= $("#userNameUpdate").val()
        let userEmail= $("#userEmail").val()
        let userPassword= $("#userPassword ").val()
        let userFreezing = $("input[name='userFreezing']:checked").val()
        console.info(userFreezing)
        console.info(userId)
        $.post("/zer02/user/update", {
                userId: userId,
                userPassword:userPassword,
                userEmail:userEmail,
                userFreezing:userFreezing
            },
            function (data) {
                if (data.code === 200) {
                    alert(data.message)
                    location.reload();
                    return
                }
                zuiMsg(data.message)
            }
        )
    }

    function userUpdate(userId,userName,userFreezing,userEmail) {
        $('#userUpdateModel').modal("toggle","center")
        $("#userId").val(userId)
        $("#userNameUpdate").val(userName)
        $(":radio[name='userFreezing'][value='"+userFreezing+"']").prop("checked","checked")
        $("#userEmail").val(userEmail)
    }

    const tip = $('[data-toggle="popover"]')
    if (checkNull(${userPage.current})) {
        const currentPage = ${userPage.current}
        const next = document.getElementsByClassName("next")[0]
        const pre = document.getElementsByClassName("previous")[0]
        if (currentPage >= ${userPage.pages}) {
            next.classList.add("disabled")
        } else {
            next.classList.contains("disabled") ? next.classList.remove("disabled") : null
            next.children[0].setAttribute("href", "/zer02/user/list?PageNum=${userPage.current+1}" + "<#if (userName??) >&userName=${userName}</#if>")
        }
        if (currentPage <= 1) {
            pre.classList.add("disabled")
        } else {
            pre.classList.contains("disabled") ? next.classList.remove("disabled") : null
            pre.children[0].setAttribute("href", "/zer02/user/list?PageNum=${userPage.current-1}" + "<#if (userName??) >&userName=${userName}</#if>")
        }

    }



    function toPage() {
        const input = document.getElementById("toPage")
        console.info(input.value)
        if (!checkNull(input.value) || input.value < 1 || input.value > ${userPage.pages}) {
            tip.popover("show")
            setTimeout(function () {
                tip.popover("hide")
            }, 4000)
        } else {
            window.location.href = 'http://localhost:8080/zer02/user/list?PageNum=' + input.value + "<#if (userName??) >&userName=${userName}</#if>";
        }
    }


    function delUser(userId) {
        userId=String(userId)
        console.info(userId)
        console.info(typeof userId)
        if (confirm("确认删除")){
            if (!checkNull(userId)) {
                new $.zui.Messager('删除失败！！！', {
                    type: 'danger'
                }).show();
                return
            }
            $.post("/zer02/user/del", {
                    userId: userId
                },
                function (data) {
                    if (data.code === 200) {
                        alert(data.message)
                        location.reload();
                        return
                    }
                    new $.zui.Messager(data.message, {
                        type: 'danger'
                    }).show();
                }
            )
        }
    }
</script>