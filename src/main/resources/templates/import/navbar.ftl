<div class="col-xs-6" style="padding-left: 2px;">
    <h2 style="color: #38ee38;padding: 0;margin: 2px;">你看见我了</h2>
    <small style="color: #808080;">&emsp;- Zer02 v120.0 -</small>
</div>
</div>
</div>

<!-- 导航栏 -->
<nav class="navbar navbar-inverse" role="navigation" style="margin-bottom: 15px;">
    <div class="container-fluid">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
                    <span class="sr-only">切换导航</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">首页</a>
            </div>
            <div class="collapse navbar-collapse navbar-collapse-example">
                <#if isMobile>
                    <ul class="nav navbar-nav">
                        <li><a href="/article/list"><i class="icon-book"></i> 全部文章</a></li>
                        <li><a href="/leaveMessage"><i class="icon-comments"></i> 留言面板</a></li>
                        <li><a href="/tools/list"><i class="icon-archive"></i> 工具箱</a></li>

                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <#if user??>
                            <li><a href="/user/publishArticle"><i class="icon-edit"></i> 发布文章</a></li>
                            <li><a href="/user/profile"><i class="icon-user"></i> 个人中心</a></li>
                            <li><a href="/logout" ><i class="icon-signout"></i> 退出登录</a></li>
                        <#else >
                            <li><a href="/login"><i class="icon-signin"></i> 登陆</a></li>
                            <li><a href="/register"><i class="icon-user"></i> 注册</a></li>
                        </#if>
                        <form class="navbar-form navbar-left" role="search" action="/article/search" method="get">
                            <div class="form-group">
                                <input type="text" value="${articleTitle!}" name="articleTitle" maxlength="25" class="form-control" placeholder="搜索">
                            </div>
                            <button type="submit" class="btn btn-default"><i class="icon-search"></i> 搜索</button>
                        </form>
                    </ul>
                <#else>
                    <ul class="nav navbar-nav">
                        <li><a href="/article/list"><i class="icon-book"></i> 全部文章</a></li>
                        <li><a href="/leaveMessage"><i class="icon-comments"></i> 留言面板</a></li>
                        <li><a href="/tools/list"><i class="icon-archive"></i> 工具箱</a></li>

                        <form class="navbar-form navbar-left" role="search" action="/article/search" method="get">
                            <div class="form-group">
                                <input type="text" value="${articleTitle!}" name="articleTitle" maxlength="25" class="form-control" placeholder="搜索">
                            </div>
                            <button type="submit" class="btn btn-default"><i class="icon-search"></i> 搜索</button>
                        </form>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <#if user??>
                            <li><a href="/user/profile"><i class="icon-user"></i> 个人中心</a></li>
                            <li><a href="/user/publishArticle"><i class="icon-edit"></i> 发布文章</a></li>
                            <li><a href="/logout" ><i class="icon-signout"></i> 退出登录</a></li>
                        <#else>
                            <li><a href="/register"><i class="icon-user"></i> 注册</a></li>
                            <li><a href="/login"><i class="icon-signin"></i> 登陆</a></li>
                        </#if>
                    </ul>
                </#if>
            </div>
        </div>
    </div>
</nav>

<!-- 主内容区域 -->
<div class="container">