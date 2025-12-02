package com.zero.blog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.dto.article.ArticlePageDto;
import com.zero.blog.dto.user.UserDto;
import com.zero.blog.dto.user.UserListPageDto;
import com.zero.blog.entity.*;
import com.zero.blog.service.*;
import com.zero.blog.util.CommonResult;
import com.zero.blog.vo.ArticleVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.zero.blog.util.CommonUtils.verifyCode;

@Controller
@RequestMapping("/zer02/")
public class AdminController {
    @Autowired
    private LinkService linkService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ArticleToTagService articleToTagService;
    @Autowired
    private AdminService adminService;

    @RequestMapping("/zer02")
    public String adminIndex(Model model){
        //系统信息
        OsInfo osInfo = SystemUtil.getOsInfo();
        HostInfo hostInfo = SystemUtil.getHostInfo();
        model.addAttribute("osName",osInfo.getName());
        model.addAttribute("address",hostInfo.getName());
        //文章数量
        Integer articleCount = articleService.ArticleCount();
        Integer articleTagCount = articleTagService.ArticleTagCount();
        model.addAttribute("articleCount",articleCount);
        model.addAttribute("articleTagCount",articleTagCount);


        //用户数量
        Integer userCount = userService.UserCount();
        model.addAttribute("userCount",userCount);

        return "admin/index";
    }

    /**
     *文章列表
     * @param articlePageDto
     * @param model
     * @return
     */
    @GetMapping("/article/list")
    public String articleList(@Valid ArticlePageDto articlePageDto, Model model){
        Integer pageNum = articlePageDto.getPageNum();
        Integer pageSize = articlePageDto.getPageSize();
        String articleTitle = articlePageDto.getArticleTitle();
        Page<ArticleVo> articlePage = new Page<>(pageNum, pageSize);
        IPage<ArticleVo> articleIPage = articleService.selectPage(articlePage, articleTitle);
        model.addAttribute("articlePage",articleIPage);
        model.addAttribute("articlePageSize",articleIPage.getRecords().size());
        if (StrUtil.isNotBlank(articlePageDto.getArticleTitle())) {
            model.addAttribute("articleTitle", articlePageDto.getArticleTitle());
        }
        return "/admin/articleList";
    }

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    @PostMapping("/article/del")
    @ResponseBody
    public CommonResult articleDel(String articleId) {
        try {
            return articleService.delArticle(articleId);
        }catch (Exception e){
            return CommonResult.failed("删除失败");
        }
    }

    /**
     * 用户列表
     * @param userListPageDto
     * @param model
     * @return
     */
    @GetMapping("/user/list")
    public String userList(@Valid UserListPageDto userListPageDto, Model model){
        Integer pageNum = userListPageDto.getPageNum();
        String userName = userListPageDto.getUserName();
        Integer pageSize = userListPageDto.getPageSize();

        Page<User> userPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery().orderByDesc(User::getUserRegisterTime);
        if (StringUtils.isNotBlank(userName)){
            userLambdaQueryWrapper.like(User::getUserName,userName);
            model.addAttribute("userName",userName);
        }
        IPage<User> userIPage = userService.selectPage(userPage, userLambdaQueryWrapper);
        System.out.println(userIPage.getRecords());
        model.addAttribute("userPage",userIPage);
        model.addAttribute("userPageSize",userIPage.getRecords().size());
        return "/admin/userList";
    }

    /**
     * 文章标签
     * @param model
     * @return
     */
    @GetMapping("/article/tag/list")
    public String articleTagList(Model model ){
        List<ArticleTag> articleTags = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery().orderByDesc(ArticleTag::getArticleTagAddTime));
        model.addAttribute("articleTagList",articleTags);
        return "/admin/articleTagList";
    }

    /**
     * 添加标签
     * @param articleTag
     * @return
     */
    @PostMapping("/article/tag/add")
    @ResponseBody
    public CommonResult articleTagAdd(ArticleTag articleTag){
        servletContext.removeAttribute("articleTagList");
        String articleTagId = articleTag.getArticleTagId();
        if (StringUtils.isNotBlank(articleTagId)){
            if (articleTagService.updateById(articleTag)){
                return CommonResult.success("修改成功");
            }
            return CommonResult.failed("修改失败");
        }
        articleTag.setArticleTagAddTime(DateUtil.date());
        if (articleTagService.save(articleTag)){
            return CommonResult.success("文章标签添加成功");
        }
        return CommonResult.failed("文章标签添加失败");
    }

    /**
     * 删除标签
     * @param articleTagId
     * @return
     */
    @PostMapping("/article/tag/del")
    @ResponseBody
    public CommonResult articleTagDel(String articleTagId){
        if (StringUtils.isBlank(articleTagId)){
            return CommonResult.failed("删除失败");
        }
        if (articleToTagService.count(Wrappers.<ArticleToTag>lambdaQuery()
                .eq(ArticleToTag::getArticleTagId,articleTagId))>0){
            return CommonResult.failed("该标签已被使用,请先删除关联文章");
        }
        if (articleTagService.removeById(articleTagId)){
            return CommonResult.success("文章标签删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    /**
     * 登入页面
     */
    @GetMapping("/login")
    public String adminLogin(Model model, HttpServletRequest request){
        if (Objects.nonNull(request.getSession().getAttribute("admin"))) {
            return "redirect:/zer02/";
        }
        return "/admin/adminLogin";
    }

    /**
     * 管理员退出登入
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        return "redirect:/zer02/login";
    }

    /**
     * 验证管理员登入
     * @param request
     * @param adminName
     * @param adminPassword
     * @param verifyCode
     * @return
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public CommonResult adminLogin(HttpServletRequest request,
                                   String adminName,
                                   String adminPassword,
                                   String verifyCode) {
        HttpSession session = request.getSession();
        if (!verifyCode(session,verifyCode)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码不正确啊啊啊");
        }
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getAdminName, adminName)
                .eq(Admin::getAdminPassword, SecureUtil.md5(adminName + adminPassword)));
        if (Objects.isNull(admin)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名或者密码不正确");
        }
        session.setAttribute("admin", admin);
        return CommonResult.success("登录成功");
    }
    /**
     * 修改admin密码
     *
     * @param newPassword
     * @return
     */
    @PostMapping("/password/update")
    @ResponseBody
    public CommonResult passwordUpdate(HttpServletRequest request, String newPassword) {
        if (StrUtil.isNotBlank(newPassword)) {
            Admin admin = adminService.getOne(null, false);
            if (Objects.nonNull(admin)) {
                admin.setAdminPassword(SecureUtil.md5(admin.getAdminName() + newPassword));
                if (adminService.updateById(admin)) {
                    request.getSession().setAttribute("admin", admin);
                    return CommonResult.success("修改成功");
                }
            }
        }
        return CommonResult.failed("修改失败");
    }

    /**
     * 用户删除
     * @param userId
     * @return
     */
    @PostMapping("/user/del")
    @ResponseBody
    public CommonResult userDel(String userId){
        if (StringUtils.isNotBlank(userId)) {
            if (!(articleService.getArticleByUserId(userId).size()>0)){
                if(userService.delUser(userId)!=0){
                    return CommonResult.success("删除成功");
                }
                return CommonResult.failed("删除失败");
            }
            return CommonResult.failed("ID："+userId+"存在已发布文章，无法删除。");
        }
        return CommonResult.failed("ID为空");
    }

    /**
     * 用户修改
     * @param userDto
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody
    public CommonResult userUpdate(@Valid UserDto userDto){
        User user = userService.getById(userDto.getUserId());
        if (Objects.isNull(user)){
            return CommonResult.failed("用户ID不存在");
        }
        Date userRegisterTime = user.getUserRegisterTime();
        //用户密码=md5(注册时间+密码)
        if (userDto.getUserPassword().isBlank()){
            userDto.setUserPassword(user.getUserPassword());
        }else userDto.setUserPassword(SecureUtil.md5(userRegisterTime+userDto.getUserPassword()));
        BeanUtils.copyProperties(userDto,user);
        if (userService.updateById(user)){
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    /**
     * 友情链接
     * @return
     */
    @GetMapping("/link/list")
    public String linkList(Model model){
        List<Link> list = linkService.list(Wrappers.<Link>lambdaQuery().orderByDesc(Link::getLinkCreateTime));
        model.addAttribute("linkList",list);
        return "/admin/linkList";
    }

    @PostMapping("/link/updateOrAdd")
    @ResponseBody
    public CommonResult linkUpdateOrAdd(Link link){
        if (StringUtils.isBlank(link.getLinkId())){
            link.setLinkCreateTime(new Date());
            if (linkService.save(link)){
                return CommonResult.success("添加成功");
            }
            return CommonResult.failed("添加失败");
        }
        if (linkService.updateById(link)){
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @PostMapping("/link/del")
    @ResponseBody
    public CommonResult linkDel(Link link){
        if (linkService.removeById(link)){
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }
}