<#include "./import/top.ftl">
<style>
    /* ------ 新增工具栏样式 ------ */
    .toolbar-section {
        margin-top: 10px;
        margin-bottom: 20px;
    }
    .tool-card {
        background: #fff;
        border-radius: 8px;
        text-align: center;
        padding: 15px 5px;
        margin-bottom: 15px;
        display: block;
        color: #555;
        text-decoration: none;
        transition: all 0.3s ease;
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
        border: 1px solid #eee;
    }
    .tool-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        color: #3280fc; /* ZUI Blue */
        text-decoration: none;
    }

    /* --- 核心修改区域：使用 Flexbox 完美居中图标 --- */
    .tool-icon-box {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: #f5f5f5;
        margin: 0 auto 10px auto; /* 保持盒子本身在卡片中居中 */
        font-size: 24px;
        transition: background 0.3s;

        /* 新增：Flex 布局强制内容居中 */
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center;     /* 垂直居中 */
    }

    /* 防止图标自带的 margin/line-height 干扰 */
    .tool-icon-box i.icon {
        margin: 0;
        padding: 0;
        line-height: 1; /* 重置行高 */
        display: inline-block;
    }
    /* ----------------------------------------- */

    .tool-card:hover .tool-icon-box {
        background: #e6f0ff;
        color: #3280fc;
    }
    .tool-title {
        font-size: 14px;
        font-weight: 600;
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    /* 移动端微调 */
    @media (max-width: 768px) {
        .tool-card { padding: 10px 2px; }
        .tool-icon-box {
            width: 40px;
            height: 40px;
            /* Flex布局下无需重设 line-height，自动居中 */
            font-size: 20px;
        }
        .tool-title { font-size: 12px; }
    }
</style>

<#include "./import/navbar.ftl">
<div class="toolbar-section">
    <#assign tools = [
    {"name": "bmi计算", "icon": "icon-calculator", "url": "/tools/bmi"}
<#--    {"name": "图床上传", "icon": "icon-cloud-upload", "url": "/upload"},-->
<#--    {"name": "随机美图", "icon": "icon-picture", "url": "/images/random"},-->
<#--    {"name": "在线音乐", "icon": "icon-music", "url": "/music"},-->
<#--    {"name": "文章归档", "icon": "icon-list-alt", "url": "/archives"},-->
<#--    {"name": "在线聊天", "icon": "icon-chat-dot", "url": "/chat"},-->
<#--    {"name": "系统状态", "icon": "icon-dashboard", "url": "/status"},-->
<#--    {"name": "更多工具", "icon": "icon-th", "url": "/more"}-->
    ]>

    <div class="row">
        <div class="col-xs-12">
            <div style="margin-bottom: 10px; padding-left: 5px; border-left: 4px solid #38ee38; font-weight: bold;">
                快捷导航
            </div>
        </div>

        <!-- 遍历工具列表 -->
        <#list tools as tool>
            <!-- 移动端一行4个(col-xs-3)，PC端一行6个(col-md-2) -->
            <div class="col-xs-3 col-sm-3 col-md-2">
                <a href="${tool.url}" class="tool-card">
                    <div class="tool-icon-box">
                        <!-- 注意这里增加了 class="icon ..." 确保 ZUI 样式生效 -->
                        <i class="icon ${tool.icon}"></i>
                    </div>
                    <span class="tool-title">${tool.name}</span>
                </a>
            </div>
        </#list>
    </div>
</div>
<#include "./import/viewBottom.ftl">