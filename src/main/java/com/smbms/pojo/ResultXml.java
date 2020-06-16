package com.smbms.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by Hunter on 2020-05-29.
 */
@XmlRootElement
public class ResultXml {

    //@XmlElement
    private User user;

    //@XmlElement 默认添加了get+属性名的方法
    @XmlElementWrapper(name = "roleList")
    @XmlElement(name="role")
    private List<Role> roles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //@XmlTransient，用来防止映射使用@XmlTransient注释的字段或者javabean的属性，从而解决名称冲突。
    @XmlTransient
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
