package com.smbms.service.user;

import com.smbms.mapper.user.UserMapper;
import com.smbms.pojo.User;
import com.smbms.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hunter on 2020-05-20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User login(String userCode, String userPassword) throws Exception {
        return userMapper.getLoginUser(userCode, userPassword);
    }

    @Override
    public void getUsersPage(PageSupport pageSupport, String userName, Integer roleId) throws Exception {
        //1. 获取总页数
        int userCount = userMapper.getUserCount(userName, roleId);
        int totalPage = userCount % pageSupport.getPageSize() == 0 ?
                userCount / pageSupport.getPageSize() : userCount / pageSupport.getPageSize() + 1;

        //页码的兼容性处理
        if (pageSupport.getCurrentPageNo() > totalPage) {
            pageSupport.setCurrentPageNo(totalPage);
        } else if (pageSupport.getCurrentPageNo() < 1) {
            pageSupport.setCurrentPageNo(1);
        }

        //2. 获取当前页的用户列表
        int startIndex = (pageSupport.getCurrentPageNo() - 1) * pageSupport.getPageSize();
        List<User> userList = userMapper.getUserList(userName, roleId, startIndex, pageSupport.getPageSize());
        pageSupport.setList(userList);
        //把总页码放入pageSupport
        pageSupport.setTotalCount(userCount);
        pageSupport.setTotalPageCount(totalPage);
    }

    @Override
    public User getUserById(int uid) throws Exception {
        return null;
    }

    @Override
    public int addUser(User user) throws Exception {
        return userMapper.add(user);
    }

    @Override
    public int modifyUser(User user) throws Exception {
        return 0;
    }

    @Override
    public int deleteUser(int id) throws Exception {
        return 0;
    }

    @Override
    public User getUserByUserCode(String userCode) throws Exception {
        return null;
    }
}
