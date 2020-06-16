package com.smbms.controller;

import com.alibaba.fastjson.JSON;
import com.smbms.pojo.Role;
import com.smbms.pojo.User;
import com.smbms.service.role.RoleService;
import com.smbms.service.user.UserService;
import com.smbms.utils.Constants;
import com.smbms.utils.PageSupport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created by Hunter on 2020-05-22.
 */
@RequestMapping("/user")
@Controller
public class UserController {

    Logger logger = Logger.getLogger(UserController.class);


    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    //删除用户
    //@ResponseBody
    @RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("uid") Integer id) throws Exception {
        int count = userService.deleteUser(id);
        Map<String, String> map = new HashMap<>();
        if(count>0){
            map.put("delResult", "true");
        }else{
            map.put("delResult", "false");
        }

        return JSON.toJSONString(map);
    }

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public Object view(@PathVariable("uid") Integer id, Model model) throws Exception {

        User user = userService.getUserById(id);
        return user;
    }


    @RequestMapping(value = "/toUserModify", method = RequestMethod.GET)
    public String toUserModify(@RequestParam(name = "uid") Integer id, Model model) throws Exception {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        //获取角色列表
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        return "user/usermodify";
    }

//    @RequestMapping("/userModify")
    @RequestMapping(value = {"/",""}, method = RequestMethod.PUT)
    public String userModify(User user, @RequestParam("uid") Integer id) throws Exception {
        //通过用户id获取用户信息
        User userOriginal = userService.getUserById(id);
        //更新用户新
        userOriginal.setUserName(user.getUserName());
        userOriginal.setGender(user.getGender());
        userOriginal.setBirthday(user.getBirthday());
        userOriginal.setPhone(user.getPhone());
        userOriginal.setAddress(user.getAddress());
        userOriginal.setUserRole(user.getUserRole());
        int count = userService.modifyUser(userOriginal);
        if(count>0){
            return "redirect: /user/list";
        }else{
            return "toUserModify";
        }
    }

    @RequestMapping("/toAddUser")
    public String toAddUser(Model model) throws Exception {
        ///WEB-INF/jsp/ xxx .jsp

        //查询角色列表
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        return "user/useradd";
    }

    /**
     * resultful 风格来实现 保存用户信息
     * @return
     */
    @RequestMapping(value = {"/",""}, method = RequestMethod.POST)
    public String userAdd(User user, HttpSession session,
                          HttpServletRequest request,
                          @RequestParam(value = "a_idPicPath", required = false) MultipartFile file) throws Exception {

        //用户的添加时间
        user.setCreationDate(new Date());
        //添加人
        User sessionUser = (User)session.getAttribute(Constants.USER_SESSION);
        if(sessionUser != null){
            user.setCreatedBy(sessionUser.getId());
        }
        //保存图片
        if(!file.isEmpty()){
            //生成图片路径
            String path = session.getServletContext().getRealPath("/upload");
            File filePath = new File(path);
            //判断路径是否存在
            if(!filePath.exists()){
                //创建目录
                filePath.mkdirs();
            }
          //1.创建文件名
          String oldName = file.getOriginalFilename();
          String suffix = oldName.substring(oldName.lastIndexOf(".")+1,oldName.length());//原文件后缀
          //String newFileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000,9999)+suffix;
          String newFileName = UUID.randomUUID().toString()+"."+suffix;
            System.out.println("===========================================newFilePath:"+path+File.separator+newFileName);
          file.transferTo(new File(path+File.separator+newFileName));

          //第2步：给user的idPacPath赋值
          String protocal = request.getScheme();
          String serverName = request.getServerName();
          int port = request.getServerPort();
          String appName = request.getContextPath();
          user.setIdPicPath(protocal+"://"+serverName+":"+port+File.separator+appName+File.separator+"upload"+File.separator+newFileName);
        }

        int count = userService.addUser(user);

        logger.info("==========================================================");
        logger.info(count);
        if(count>0){
            return "redirect: /user/list";
        }else{
            return "/toAddUser";
        }
    }


    @RequestMapping("/list")
    public String userlist(@RequestParam(value = "queryname", required = false) String userName,
                           @RequestParam(value = "queryUserRole", required = false)Integer roleId,
                           @RequestParam(value = "pageIndex", required = false)Integer pageIndex,
                           Model model) throws Exception {
        PageSupport pageSupport = new PageSupport();
        if(pageIndex != null){
            pageSupport.setCurrentPageNo(pageIndex);
        }
        if(roleId==null){
            roleId = 0;
        }
        userService.getUsersPage(pageSupport, userName, roleId);

        //查询角色列表
        List<Role> roleList = roleService.getRoleList();

        //把数据返回给页面
        model.addAttribute("userList", pageSupport.getList());
        model.addAttribute("totalPageCount", pageSupport.getTotalPageCount());
        model.addAttribute("totalCount", pageSupport.getTotalCount());
        model.addAttribute("currentPageNo", pageSupport.getCurrentPageNo());
        //把角色列表放入model
        model.addAttribute("roleList", roleList);
        //把用户名和角色id返回给页面回显
        model.addAttribute("queryUserName", userName);
        model.addAttribute("queryUserRole", roleId);

        // /WEB-INF/jsp/  xxx   .jsp
        return "user/userlist";
    }

    //校验用户编码是否存在
    @ResponseBody
    @RequestMapping(value = "/ucexist", method = RequestMethod.GET)
    public String ucexist(@RequestParam("userCode") String userCode) throws Exception {
        User user = userService.getUserByUserCode(userCode);
        Map<String, String> map = new HashMap<>();
        if(user != null){
            map.put("userCode", "exist");
        }else {
            map.put("userCode", "notExist");
        }
        return JSON.toJSONString(map);
    }

    @ResponseBody
    //@RequestMapping(value = "/test", produces = {"text/html;charset=UTF-8"})
    @RequestMapping(value = "/test", produces = {"application/json;charset=UTF-8"})
    public String test() throws Exception {
        //return "你好";
        //Map<String, String> map = new HashMap<>();
        //map.put("hello", "你好");

        List<Role> roleList = roleService.getRoleList();
        return JSON.toJSONString(roleList);
    }

    @ResponseBody
    @RequestMapping(value = "/test2")
    public String test2() throws Exception {
        List<Role> roleList = roleService.getRoleList();
        return JSON.toJSONString(roleList);
    }

    @ResponseBody
    @RequestMapping(value = "/view/{uid}", method = RequestMethod.GET)
    public Object view2(@PathVariable("uid") Integer id, Model model) throws Exception {

        User user = userService.getUserById(id);
        return user;
    }

}
