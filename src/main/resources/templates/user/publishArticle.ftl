<#include "./import/top.ftl">
<#include "./import/navbar.ftl">

<style>
    .label:hover {
        cursor: pointer
    }
</style>
<div class="col-xs-12">
    <div class="panel" style="margin-bottom: 5px;">
        <div class="panel-heading">
            <i class="icon-edit"></i> 发布文章
        </div>
        <div class="panel-body">
            <div class="col-xs-12 col-sm-3" style="padding: 2px;">
                <#if article.articleCoverUrl??>
                    <img src="${article.articleCoverUrl}" style="width: 100%;">
                </#if>
            </div>
            <div class="col-xs-12 col-sm-9" style="padding: 2px;">
                <form class="form-horizontal">
                    <input type="hidden" id="articleId" name="articleId" value="${(article.articleId)!}">
                    <div class="form-group">
                        <label for="articleTitle" class="col-xs-2 col-sm-1">标题</label>
                        <div class="col-xs-10 col-sm-10">
                            <input type="text" class="form-control" value="${(article.articleTitle)!}"
                                   name="articleTitle"
                                   id="articleTitle" placeholder="标题">
                        </div>
                    </div>
                    <div class="form-group">
                        <input type="hidden" id="articleCoverUrl" name="articleCoverUrl"
                               value="${(article.articleCoverUrl)!}">
                        <label for="articleCoverFile" class="col-xs-2 col-sm-1">封面</label>
                        <div class="col-xs-10 col-sm-5">
                            <input type="file" class="form-control" accept="image/*" name="articleCoverFile"
                                   id="articleCoverFile">文件
                        </div>
                    </div>

                    <#if articleTagList?? && articleTagList?size gt 0 >
                        <div class="form-group">
                            <label for="exampleInputPassword4" class="col-xs-2 col-sm-1">标签</label>
                            <div class="col-xs-10" id="articleTagListBox">
                                <#list articleTagList as articleTag>
                                    <#if articleTagIdList?? && articleTagIdList?size gt 0>
                                        <#if articleTagIdList?seq_contains(articleTag.articleTagId)>
                                            <span class="label label-badge label-success"
                                                  onclick="selectArticleTag('${(articleTag.articleTagId)}')"
                                                  id="${(articleTag.articleTagId)}">${(articleTag.articleTagName)}</span>
                                        <#else >
                                            <span class="label label-badge"
                                                  onclick="selectArticleTag('${(articleTag.articleTagId)}')"
                                                  id="${(articleTag.articleTagId)}">${(articleTag.articleTagName)}</span>
                                        </#if>
                                    <#else >
                                        <span class="label label-badge "
                                              onclick="selectArticleTag('${(articleTag.articleTagId)}')"
                                              id="${(articleTag.articleTagId)}">${(articleTag.articleTagName)}</span>
                                    </#if>
                                </#list>
                            </div>
                        </div>
                    </#if>

                </form>
            </div>
        </div>
    </div>
</div>
<div class="col-xs-12">
    <div class="panel">
        <div class="panel-body">
            <textarea id="articleContext" maxlength="150000" minlength="5">${(article.articleContext)!}</textarea>
            <div class="form-group" style="margin-top: 15px;text-align: right;">
                <button onclick="publishArticle()" type="button" class="btn btn-success"><i
                            class="icon-edit-sign"></i> 发布
                </button>
            </div>
        </div>
    </div>
</div>
<script src="/HandyEditor-master/HandyEditor.min.js"></script>
<script>
    let articleTagIds = [];

    var he = HE.getEditor('articleContext', {
        width: '100%',
        height: '200px',
        autoHeight: true,
        autoFloat: false,
        topOffset: 0,
        uploadPhoto: true,
        uploadPhotoHandler: '/user/uploadFile',
        uploadPhotoSize: 1024,
        uploadPhotoType: 'gif,png,jpg,jpeg',
        uploadPhotoSizeError: '不能上传大于1024KB的图片',
        uploadPhotoTypeError: '只能上传gif,png,jpg,jpeg格式的图片',
        uploadParam: {},
        lang: 'zh-jian',
        skin: 'HandyEditor',
        externalSkin: '',
        item: ['bold', 'italic', 'strike', 'underline', 'fontSize', 'fontName', 'paragraph', 'color', 'backColor', '|', 'center', 'left', 'right', 'full', 'indent', 'outdent', '|', 'link', 'unlink', 'textBlock', 'code', 'selectAll', 'removeFormat', '|', 'image', 'expression', 'horizontal', 'orderedList', 'unorderedList', '|', 'undo', 'redo', '|', 'html']
    });

    function publishArticle() {
        let articleId = $("#articleId").val();
        let articleCoverUrl = $("#articleCoverUrl").val();
        let articleTitle = $("#articleTitle").val();
        let articleContext = he.getHtml();

        if (!checkNotNull(articleTitle)) {
            zuiMsg("标题总该有一个吧");
            return;
        }
        if (!checkNotNull(articleCoverUrl) && !checkNotNull($("#articleCoverFile").val())) {
            zuiMsg("封面没选啊!");
            return;
        }
        if (!checkNotNull(articleContext)) {
            zuiMsg("没内容不行啊");
            return;
        }
        if (articleContext.length<5 || articleContext.length>1500000){
            zuiMsg("文章内容在5-1500000字符之间");
            return;
        }
        if (!checkNotNull(articleTagIds)){
            zuiMsg("看你心情选个标签吧");
            return;
        }

        let formData = new FormData();
        formData.append("articleCoverFile", $("#articleCoverFile")[0].files[0]);
        formData.append("articleId", articleId);
        formData.append("articleCoverUrl", articleCoverUrl);
        formData.append("articleTitle", articleTitle);
        formData.append("articleTagIds", articleTagIds);
        formData.append("articleContext", articleContext);
        $.ajax({
            url: "/user/publishArticleAction",
            type: 'POST',
            data: formData,
            // 告诉jQuery不要去处理发送的数据
            processData: false,
            // 告诉jQuery不要去设置Content-Type请求头
            contentType: false,
            beforeSend: function () {
                console.log("正在进行，请稍候");
            },
            success: function (data) {
                if (data.code == 200) {
                    alert(data.message);
                    window.location.href = "/user/myArticleList";
                    return;
                }
                zuiMsg(data.message);
            },
            error: function (responseStr) {
                console.log("error");
            }
        });

    }


    function selectArticleTag(articleTagId) {
        let index = articleTagIds.indexOf(articleTagId);
        if (index > -1) {
            articleTagIds.splice(index, 1);
            $("#" + articleTagId).removeClass("label-success");
        } else {
            articleTagIds[articleTagIds.length] = articleTagId;
            $("#" + articleTagId).addClass("label-success");
        }

        console.log(articleTagIds);
    }



    $(function () {
        let selectedTags = $("span.label-success");
        for (let i = 0; i < selectedTags.length; i++) {
            articleTagIds[i] = selectedTags[i].id;
        }
    });

</script>
<#include "./import/viewBottom.ftl">
