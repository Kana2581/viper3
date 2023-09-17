package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Info;
import com.example.demo.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {
    @Resource
    AdminMapper adminMapper;

    // 管理员列表
    public Admin[] getAllAdmins(int offset,int limit)
    {
        return adminMapper.findAllAdmins(offset,limit);
    }

    // 新增管理员
    public boolean addAdmin(Admin admin)
    {
        return adminMapper.loadAdmin(admin);
    }

    // 删除管理员
    public boolean deleteAdmin(int id)
    {
        return adminMapper.dropAdminsById(id);
    }

    // 根据账号或角色查管理员
    public Admin[] getAdmin(String account,String role,int page,int limit)
    {
        return adminMapper.findAdminsByAccountAndRole(account,role,(page-1)*limit,limit);
    }

    // 总数
    public String getCount(String account,String role){
        return adminMapper.countAdminsByAccountAndRole(account,role);
    }

    // 管理员登录
    public Admin AdminLogin(String account, String psw){ return adminMapper.findAdminByAccountAndPsw(account,psw); }
}
