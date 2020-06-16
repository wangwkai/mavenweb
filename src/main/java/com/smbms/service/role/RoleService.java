package com.smbms.service.role;

import com.smbms.pojo.Role;

import java.util.List;

/**
 * Created by Hunter on 2020-05-25.
 */
public interface RoleService {

    public List<Role> getRoleList() throws Exception;
}
