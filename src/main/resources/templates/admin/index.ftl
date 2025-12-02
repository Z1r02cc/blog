<#include "./import/adminTop.ftl">

<div class="panel col-xs-12">
    <div class="panel-body ">
        <div class="col-xs-6">
            <div class="panel ">
                <div class="panel-body ">
                    <h3><i class="icon icon-desktop"></i> 系统类型:${osName!}</h3>
                    <h3><i class="icon icon-server"></i> IP地址:${address!}</h3>
                </div>
            </div>
        </div>
        <div class="col-xs-6">
            <div class="panel">
                <div class="panel-body ">
                    <h3><i class="icon icon-user"></i> 用户数量:${userCount!}</h3>
                    <h3><i class="icon icon-stack"></i> 文章数量:${articleCount!}</h3>
                    <h3><i class="icon icon-th"></i> 标签数量:${articleTagCount!}</h3>
                </div>
            </div>
        </div>
    </div>
</div>

<#include "./import/bottom.ftl">