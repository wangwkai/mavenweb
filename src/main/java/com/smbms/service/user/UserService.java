package com.smbms.service.user;

import com.smbms.pojo.User;
import com.smbms.utils.PageSupport;

/**
 * Created by Hunter on 2020-05-20.
 */
public interface UserService {

    public User login(String userCode, String userPassword) throws Exception;

    //获取用户列表
    public void getUsersPage(PageSupport pageSupport, String userName, Integer roleId) throws Exception;

    //通过用户id获取用户
    public User getUserById(int uid) throws Exception;


    //添加用户
    public int addUser(User user) throws Exception;

    //修改用户
    public int modifyUser(User user) throws Exception;

    public int deleteUser(int id) throws Exception;

    //通过用户编码获取用户
    public User getUserByUserCode(String userCode) throws Exception;
}
