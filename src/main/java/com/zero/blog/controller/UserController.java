package com.zero.blog.controller;

import cn.hutool.core.annotation.Link;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.dto.article.PublishArticleActionDto;
import com.zero.blog.entity.*;
import com.zero.blog.service.*;
import com.zero.blog.util.CommonResult;
import com.zero.blog.vo.ArticleVo;
import com.zero.blog.vo.CommentVo;
import com.zero.blog.vo.LeaveMessageVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UploadFileListService uploadFileListService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    UserService userService;
    @Autowired
    LeaveMessageService leaveMessageService;
    @Autowired
    CommentService commentService;
    /**
     * 全局
     * @param request
     * @param model
     */
    @ModelAttribute
    public void pre(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (ObjUtil.isNotNull(user)){
            model.addAttribute("user",user);
        }
    }
    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(HttpServletRequest request, MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        return uploadFileListService.getUploadFileUrl(file);
    }


    @GetMapping("/profile")
    public String userProfile(HttpServletRequest request, Model model){
        return "/user/userManager";
    }
    @GetMapping("/publishArticle")
    public String releaseArticle(HttpServletRequest request, Model model, String articleId) {
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return "redirect:/";
        }

        Article article = articleService.getOne(Wrappers.<Article>lambdaQuery().eq(Article::getUserId, user.getUserId()).eq(Article::getArticleId, articleId));
        if (Objects.nonNull(article)) {
            model.addAttribute("article", article);

            //获取文章标签
            List<ArticleTag> articleTagLists = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery()
                    .eq(ArticleTag::getArticleTagId, article.getArticleId())
                    .select(ArticleTag::getArticleTagId));

            if (CollUtil.isNotEmpty(articleTagLists)) {
                List<String> articleTagIdList = articleTagLists.stream().map(ArticleTag::getArticleTagId).collect(Collectors.toList());
                model.addAttribute("articleTagIdList", articleTagIdList);
            }

        }
        //获取标签
        List<ArticleTag> articleTagList = articleTagService.list();
        model.addAttribute("articleTagList", articleTagList);


        return "/user/publishArticle";
    }

    @PostMapping("/publishArticleAction")
    @ResponseBody
    public CommonResult publishArticleAction(HttpServletRequest request, @Valid PublishArticleActionDto publishArticleActionDto, MultipartFile articleCoverFile) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (Objects.isNull(user)){
            return CommonResult.failed("登录过期了，重新登录下好吗。");
        }
        User serviceById = userService.getById(user.getUserId());
        if(Objects.isNull(serviceById)){
            session.setAttribute("user",serviceById);
        }
        if (Objects.nonNull(articleCoverFile)) {
            //判断是否上传的图片，是否是我们指定的像素
            BufferedImage read = ImageIO.read(articleCoverFile.getInputStream());
            if (Objects.isNull(read)) {
                return CommonResult.failed("上传一下图片，求你啦!");
            }
            int width = read.getWidth();
            int height = read.getHeight();

//            if(width>=600 || height>=300){
//                return CommonResult.failed("长不能超过600px，宽不能超过300px");
//            }
            publishArticleActionDto.setArticleCoverUrl(uploadFileListService.getUploadFileUrl(articleCoverFile));
        }

        return articleService.publishArticleAction(request, publishArticleActionDto);
    }

    @GetMapping("/myArticleList")
    public String myArticleList(HttpServletRequest request, Integer PageNum, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(PageNum) || PageNum < 1) {
            PageNum = 1;
        }
        Page<ArticleVo> articlePage = new Page<>(PageNum, 24);
        IPage<ArticleVo> articleVoIPage = articleService.userArticleList(articlePage, null, user.getUserId());
        model.addAttribute("articleVoIPage", articleVoIPage);

        return "/user/myArticleList";
    }

    @PostMapping("/saveComment")
    @ResponseBody
    public CommonResult userSaveComment(HttpServletRequest request, String articleId, String commentContent,String commentId) {
        if (StrUtil.isBlank(articleId) || StrUtil.isBlank(commentContent)) {
            return CommonResult.failed("你是不是没写评论内容啊？");
        }
        if (commentContent.length() < 1 || commentContent.length() > 800) {
            return CommonResult.failed("评论内容太多了啊，顶不住(1-800)");
        }
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return CommonResult.failed("登录过期了，重新登录下好吗。");
        }
        String userId = user.getUserId();


        Comment comment = commentService.getOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getCommentUser, userId).select(Comment::getCommentTime).orderByDesc(Comment::getCommentTime), false);
        if (Objects.nonNull(comment) && Objects.nonNull(comment.getCommentTime())) {
            if ((comment.getCommentTime().getTime() + 1000) > System.currentTimeMillis()) {
                return CommonResult.failed("慢点评论，慢点评论");
            }
        }

        Comment comment1 = new Comment();
        comment1.setCommentId(DateUtil.format(new Date(),"yyyyMMddHHmmss")+userId);
        comment1.setArticleId(articleId);
        comment1.setCommentUser(userId);
        comment1.setCommentContent(commentContent);
        comment1.setCommentTime(DateUtil.date());

        if (commentService.save(comment1)) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment1, commentVo);
            commentVo.setUserName(
                    userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, comment1.getCommentUser()).select(User::getUserName)).getUserName()
            );
            commentVo.setCommentTime(DateUtil.format(comment1.getCommentTime(),"yyyy-MM-dd HH:mm:ss"));

            return CommonResult.success(commentVo);
        }
        return CommonResult.failed("评论失败");
    }

    @PostMapping("/delArticle")
    @ResponseBody
    public CommonResult delArticle(String articleId){
        try {
            return articleService.delArticle(articleId);
        }catch (Exception e){
            return CommonResult.failed("删除失败");
        }
    }

    @PostMapping("/leaveMessage")
    @ResponseBody
    public CommonResult userLeaveMessage(HttpServletRequest request,String leaveMessage) {
        if ( StrUtil.isBlank(leaveMessage)) {
            return CommonResult.failed("你是不是没写评论内容啊？");
        }
        if (leaveMessage.length() < 1 || leaveMessage.length() > 800) {
            return CommonResult.failed("留言内容太多了啊，顶不住(1-800)");
        }
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return CommonResult.failed("登录过期了，重新登录下好吗。");
        }
        String userId = user.getUserId();


        LeaveMessage LeaveMessageDelay = leaveMessageService.getOne(Wrappers.<LeaveMessage>lambdaQuery().eq(LeaveMessage::getUserId, userId).select(LeaveMessage::getLeaveMessageTime).orderByDesc(LeaveMessage::getLeaveMessageTime), false);
        if (Objects.nonNull(LeaveMessageDelay) && Objects.nonNull(LeaveMessageDelay.getLeaveMessageTime())) {
            if ((LeaveMessageDelay.getLeaveMessageTime().getTime() + 1000) > System.currentTimeMillis()) {
                return CommonResult.failed("慢点评论，慢点评论");
            }
        }

        LeaveMessage leaveMessage1 = new LeaveMessage();
        leaveMessage1.setLeaveMessageId(DateUtil.format(new Date(),"yyyyMMddHHmmss")+userId);
        leaveMessage1.setUserId(userId);
        leaveMessage1.setLeaveMessageText(leaveMessage);
        leaveMessage1.setLeaveMessageTime(DateUtil.date());

        if (leaveMessageService.save(leaveMessage1)) {
            LeaveMessageVo leaveMessageVo = new LeaveMessageVo();
            BeanUtils.copyProperties(leaveMessage1, leaveMessageVo);
            leaveMessageVo.setUserName(
                    userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, leaveMessage1.getUserId()).select(User::getUserName)).getUserName()
            );
            leaveMessageVo.setLeaveMessageTime(DateUtil.format(leaveMessage1.getLeaveMessageTime(),"yyyy-MM-dd HH:mm:ss"));

            return CommonResult.success(leaveMessageVo);
        }
        return CommonResult.failed("评论失败");
    }


}

