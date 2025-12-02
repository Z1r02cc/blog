package com.zero.blog.controller;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.zero.blog.entity.LeaveMessage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.dto.article.ArticlePageDto;
import com.zero.blog.dto.user.UserInfoDto;
import com.zero.blog.dto.user.UserRegInfo;
import com.zero.blog.entity.*;
import com.zero.blog.service.*;
import com.zero.blog.util.CommonPage;
import com.zero.blog.util.CommonResult;
import com.zero.blog.util.CommonUtils;
import com.zero.blog.vo.ArticleVo;
import com.zero.blog.vo.CommentVo;
import com.zero.blog.vo.LeaveMessageVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.zero.blog.util.CommonUtils.verifyCode;

@Controller
public class ViewController {


    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;
    @Autowired
    LeaveMessageService leaveMessageService;
    @Autowired
    CommentService commentService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    ArticleToTagService articleToTagService;
    /**
     *首页
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        List<Article> articleList = articleService.list(Wrappers.<Article>lambdaQuery()
                .orderByDesc(Article::getArticleCreateTime)
                .last("limit 9")
        );
        model.addAttribute("articleList", articleList);
        List<LeaveMessage> leaveMessageList = leaveMessageService.list(Wrappers.<LeaveMessage>lambdaQuery().orderByDesc(LeaveMessage::getLeaveMessageTime).last("limit 5"));
        model.addAttribute("leaveMessageList", leaveMessageList);
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/index";
    }

    /**
     * 文章
     *
     * @param request
     * @param articleId
     * @param model
     * @return
     */
    @GetMapping("/article")
    public String articleView(HttpServletRequest request, String articleId, Model model) {

        ArticleVo articleVo = articleService.getArticle(articleId);
        if (Objects.isNull(articleVo)) {
            return "redirect:/";
        }


        Article article = articleService.getOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, articleVo.getArticleId()).select(Article::getArticleId, Article::getArticleBrowseTime), false);

        Integer articleBrowseTime = article.getArticleBrowseTime();
        if (Objects.isNull(articleBrowseTime) || articleBrowseTime < 0) {
            articleBrowseTime = 0;
        }
        ++articleBrowseTime;
        article.setArticleBrowseTime(articleBrowseTime);
        articleService.updateById(article);


        //隐藏作者用户名
        String userName = articleVo.getUserName();
        if (StrUtil.isNotBlank(userName)) {
            articleVo.setUserName(CommonUtils.getHideMiddleStr(userName));
        }

        //文章
        model.addAttribute("article", articleVo);
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        return "/view/article";
    }

    /**
     * 全部文章
     * @param articlePageDto
     * @param model
     * @return
     */
    @GetMapping("/article/list")
    public String articleListView(HttpServletRequest request, @Valid ArticlePageDto articlePageDto, Model model) {
        Integer pageNum = articlePageDto.getPageNum();
        String articleTitle = articlePageDto.getArticleTitle();
        Page<ArticleVo> articlePage = new Page<>(pageNum, 24);
        IPage<ArticleVo> articleIPage = articleService.selectPage(articlePage, articleTitle);
        model.addAttribute("articlePage", articleIPage);
        model.addAttribute("articlePageSize", articleIPage.getRecords().size());
        if (StrUtil.isNotBlank(articleTitle)) {
            model.addAttribute("articleTitle", articleTitle);
        }
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        return "/view/articleList";
    }

    /**
     * 获取图像验证码
     *
     * @throws IOException
     */
    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CircleCaptcha captcha = CommonUtils.getCaptcha(request);
        captcha.write(response.getOutputStream());
    }

    /**
     * 用户注册页面
     *
     * @param request
     * @return
     */
    @GetMapping("/register")
    public String register(HttpServletRequest request,Model model) {
        if (Objects.nonNull(request.getSession().getAttribute("user"))) {
            return "redirect:/";
        }
        return "/view/register";
    }

    /**
     * 用户注册方法
     *
     * @param request
     * @param userRegInfo
     * @return
     */
    @PostMapping("/userRegister")
    @ResponseBody
    public CommonResult userRegister(HttpServletRequest request, UserRegInfo userRegInfo) {
        System.out.println(userRegInfo.getUserEmail());
        HttpSession session = request.getSession();
        String verifyCode = userRegInfo.getVerifyCode();
        if (!verifyCode(session, verifyCode)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码错了啊，铁咩");
        }

        //用户名和密码是否相同
        if (userRegInfo.getUserName().equals(userRegInfo.getUserPassword())) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名和密码不能相同啊，铁咩");
        }

        //用户名是否已经被使用
        if (userService.count(Wrappers.<User>lambdaQuery().eq(User::getUserName, userRegInfo.getUserName())) > 0) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名已经存在了啊，铁咩");
        }

        if (userService.count(Wrappers.<User>lambdaQuery().eq(User::getUserEmail, userRegInfo.getUserEmail())) > 0) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("邮箱已经存在了啊，铁咩");
        }
        User user = new User();
        BeanUtils.copyProperties(userRegInfo, user);
        user.setUserId(IdUtil.simpleUUID());
        user.setUserRegisterTime(DateUtil.date());
        user.setUserPassword(SecureUtil.md5(user.getUserId() + user.getUserPassword()));
        user.setUserEmail(userRegInfo.getUserEmail());
        user.setUserFreezing(0);
        System.out.println(user);
        if (userService.save(user)) {
            return CommonResult.success("终于注册成功了啊啊啊啊！");
        }

        return CommonResult.failed("我不懂啊，怎么注册失败了");
    }

    /**
     * 用户登陆页面
     *
     * @param request
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        if (Objects.nonNull(request.getSession().getAttribute("user"))) {
            return "redirect:/";
        }
        model.addAttribute("referer", request.getHeader("referer"));
        return "/view/login";
    }


    /**
     * 用户登录方法
     *
     * @param request
     * @param userInfoDto
     * @return
     */
    @PostMapping("/userLogin")
    @ResponseBody
    public CommonResult userLogin(HttpServletRequest request, UserInfoDto userInfoDto) {
        HttpSession session = request.getSession();
        String verifyCode = userInfoDto.getVerifyCode();
        if (!verifyCode(session, verifyCode)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码错啦！！！");
        }
        //用户名和密码是否相同
        if (userInfoDto.getUserName().equals(userInfoDto.getUserPassword())) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名和密码不能相同哦");
        }

        //获取用户
        User userDb = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, userInfoDto.getUserName()), false);
        if (Objects.isNull(userDb)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名错误");
        }
        if (Objects.nonNull(userDb.getUserFreezing()) && userDb.getUserFreezing() == 1) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("你的号被禁止登入了，想想自己有没有做了什么好事！");
        }

        if (!SecureUtil.md5(userDb.getUserId() + userInfoDto.getUserPassword()).equals(userDb.getUserPassword())) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("密码错误");
        }
        session.setAttribute("user", userDb);
        return CommonResult.success("登录成功");

    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    /**
     * 留言界面
     * @return
     */
    @GetMapping("/leaveMessage")
    public String leaveMessage(){
        return "/view/leaveMessage";
    }

    /**
     * 获取留言信息
     * @param request
     * @param pageNumber
     * @return
     */
    @PostMapping("/leaveMessage/list")
    @ResponseBody
    public CommonResult leaveMessageList(HttpServletRequest request,  Integer pageNumber) {
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<LeaveMessageVo> leaveMessageVoPage = new Page<>(pageNumber, 5);
        IPage<LeaveMessageVo> leaveMessageVoIPage = leaveMessageService.getLeaveMessageList(leaveMessageVoPage);
        User user = (User) request.getSession().getAttribute("user");
        if (ObjUtil.isNotNull(leaveMessageVoIPage)) {
            //判断留言是否是自己发出，是自己发出就不匿名显示
            if (ObjUtil.isNotNull(user)){
                leaveMessageVoIPage.getRecords().stream().forEach(commentVo -> {
                    if (!(commentVo.getUserName().equals(user.getUserName()))) {
                        commentVo.setUserName(CommonUtils.getHideMiddleStr(commentVo.getUserName()));
                    }
                });
            }else {
                leaveMessageVoIPage.getRecords().stream().forEach(commentVo -> {
                    commentVo.setUserName(CommonUtils.getHideMiddleStr(commentVo.getUserName()));
                });
            }
        }
        return CommonResult.success(CommonPage.restPage(leaveMessageVoIPage));
    }

    /**
     * 获取评论列表
     * @param request
     * @param articleId
     * @param pageNumber
     * @return
     */
    @PostMapping("/comment/list")
    @ResponseBody
    public CommonResult commentList(HttpServletRequest request, String articleId, Integer pageNumber) {
        if (StrUtil.isBlank(articleId)) {
            return CommonResult.failed("程序出现错误，请刷新页面重试");
        }
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<CommentVo> commentVoPage = new Page<>(pageNumber, 5);
        IPage<CommentVo> commentVoIPage = commentService.getArticleCommentList(commentVoPage, articleId);
        User user = (User) request.getSession().getAttribute("user");
        if (ObjUtil.isNotNull(commentVoIPage)) {
            //判断评论是否是自己发出，是自己发出就不匿名显示
            if (ObjUtil.isNotNull(user)){
                commentVoIPage.getRecords().stream().forEach(commentVo -> {
                    if (!(commentVo.getUserName().equals(user.getUserName()))) {
                        commentVo.setUserName(CommonUtils.getHideMiddleStr(commentVo.getUserName()));
                    }
                });
            }else {
                commentVoIPage.getRecords().stream().forEach(commentVo -> {
                    commentVo.setUserName(CommonUtils.getHideMiddleStr(commentVo.getUserName()));
                });
            }
        }
        return CommonResult.success(CommonPage.restPage(commentVoIPage));
    }


    /**
     * 搜索文章
     *
     * @param request
     * @param articleTitle
     * @return
     */
    @GetMapping("/article/search")
    public String articleSearch(HttpServletRequest request, Integer pageNumber, String articleTitle, Model model) {
        if (StrUtil.isBlank(articleTitle)) {
            return "/";
        }
        if (articleTitle.equals("zjy")){
            return "/view/test2";
        }
        articleTitle = articleTitle.trim();
        model.addAttribute("articleTitle", articleTitle);
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
//        if (articleTitle.equals("rena")){
//            return "/view/bit";
//        }
        String ipAddr = CommonUtils.getIpAddr(request);
        ServletContext servletContext = request.getServletContext();
        ConcurrentMap<String, Long> articleSearchMap = (ConcurrentMap<String, Long>) servletContext.getAttribute("articleSearchMap");
        if (CollUtil.isEmpty(articleSearchMap) || Objects.isNull(articleSearchMap.get(ipAddr))) {
            articleSearchMap = new ConcurrentHashMap<>();
            articleSearchMap.put(ipAddr, DateUtil.currentSeconds());
        } else {
            if ((articleSearchMap.get(ipAddr) + 1 > DateUtil.currentSeconds())) {
                return "/view/searchError";
            }
        }
        //查询到的文章列表
        List<Article> articleList = new ArrayList<>();

        //拆分搜索词,查询标签
        List<Word> words = WordSegmenter.seg(articleTitle);
        List<String> titleList = words.stream().map(Word::getText).collect(Collectors.toList());
        titleList.add(articleTitle);
        List<String> articleTagIdList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery()
                .in(ArticleTag::getArticleTagName, titleList)
                .select(ArticleTag::getArticleTagId)).stream().map(ArticleTag::getArticleTagId).collect(Collectors.toList());
        List<String> articleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(articleTagIdList)) {
            articleIdList = articleToTagService.list(Wrappers.<ArticleToTag>lambdaQuery()
                            .in(ArticleToTag::getArticleTagId, articleTagIdList)
                            .select(ArticleToTag::getArticleId)).stream()
                    .map(ArticleToTag::getArticleId).collect(Collectors.toList());

        }

        //分页查询
        IPage<Article> articlePage = new Page<>(pageNumber, 12);
        LambdaQueryWrapper<Article> queryWrapper = Wrappers.<Article>lambdaQuery()
                .like(Article::getArticleTitle, articleTitle)
                .select(Article::getArticleId,
                        Article::getArticleCoverUrl,
                        Article::getArticleBrowseTime,
                        Article::getArticleCreateTime,
                        Article::getArticleTitle);
        if (CollUtil.isNotEmpty(articleIdList)) {
            queryWrapper.or().in(Article::getArticleId, articleIdList);
        }

        IPage<Article> articleIPage = articleService.page(articlePage, queryWrapper);
        model.addAttribute("articleIPage", CommonPage.restPage(articleIPage));

        //保持搜索时间
        articleSearchMap.put(ipAddr, DateUtil.currentSeconds());
        servletContext.setAttribute("articleSearchMap", articleSearchMap);

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);


        return "/view/articleSearch";
    }

    /**
     * 标签搜索文章
     * @param request
     * @param pageNumber
     * @param articleTagId
     * @param model
     * @return
     */
    @GetMapping("/tag/article")
    public String tagSearch(HttpServletRequest request, Integer pageNumber, String articleTagId, Model model) {
        if (StrUtil.isBlank(articleTagId)) {
            return "redirect:/";
        }
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<ArticleVo> articlePage = new Page<>(Objects.isNull(pageNumber) ? 1 : pageNumber, 24);
        IPage<ArticleVo> articleVoIPage = articleService.tagArticleList(articlePage, articleTagId);
        model.addAttribute("articleVoIPage", CommonPage.restPage(articleVoIPage));

        //获取标签类型
        ArticleTag articleTag = articleTagService.getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleTagId, articleTagId));
        if (Objects.nonNull(articleTag)) {
            model.addAttribute("articleTagName", articleTag.getArticleTagName());
        }

        model.addAttribute("articleTagId", articleTagId);

        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/view/tagArticleList";
    }

    /**
     * 文章点赞
     *
     * @param request
     * @param articleId
     * @return
     */
    @PostMapping("/articlePraise")
    @ResponseBody
    public CommonResult articlePraise(HttpServletRequest request, String articleId) {
        HttpSession session = request.getSession();
        if (Objects.nonNull(session.getAttribute("articlePraiseNum"))) {
            return CommonResult.failed("您已经点过啦，太懒了只能让你点一次，嘿嘿");
        }

        Article article = articleService.getById(articleId);
        Integer articlePraiseNum = article.getArticlePraiseNum();
        ++articlePraiseNum;
        article.setArticlePraiseNum(articlePraiseNum);
        if (articleService.updateById(article)) {
            session.setAttribute("articlePraiseNum", true);
            return CommonResult.success("你成功使用了唯一一次点赞机会！");
        }

        return CommonResult.failed("点赞失败");
    }


    @GetMapping("/tanc")
    public String tanC(Model model){
        return "/view/test";
    }
    @GetMapping("/zjjjj")
    public String zjjjj(Model model){
        return "/view/test2";
    }




}
