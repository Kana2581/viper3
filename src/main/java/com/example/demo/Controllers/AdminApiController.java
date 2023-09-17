package com.example.demo.Controllers;

import com.example.demo.entity.Admin;
import com.example.demo.entity.RestBean;
import com.example.demo.service.AdminService;
import com.example.demo.utils.BaseUtils;
import com.example.demo.utils.JwtUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminApiController {
    @Resource
    AdminService adminService;
    @GetMapping("/admins")
    public RestBean getAdmin(@RequestParam("page")int page,@RequestParam("limit")int limit){
        page--;
        int offset=page*limit;
        return new RestBean(200,"success",adminService.getAllAdmins(offset,limit));
    }
    @PostMapping("/admins")
    public RestBean postAdmin(@RequestBody Admin admin){
        if(Integer.parseInt(adminService.getCount(admin.getAccount(),"")) > 0 && admin.getId()==0){
            return new RestBean(402,"success","账号已存在");
        }
        if(admin.getId()==0){
            admin.setCreateAt(BaseUtils.GetTimeString());
        }
        return new RestBean(200,"success",adminService.addAdmin(admin));
    }
    @DeleteMapping("/admins/delete")
    public RestBean deleteAdmin(@RequestParam("id")int id){
        return new RestBean(200,"success",adminService.deleteAdmin(id));
    }

    @GetMapping("/admins/search")
    public RestBean searchInfo(@RequestParam("account") String account,@RequestParam("role")String role, @RequestParam("page") int page, @RequestParam("limit") int limit){
        return new RestBean(200,adminService.getCount(account,role),adminService.getAdmin(account,role,page,limit));
    }

    @GetMapping("/login")
    public RestBean adminLogin(@RequestParam("account")String account,@RequestParam("psw")String psw){
        Map<String,Object> map = new HashMap<>();
        try{
            Admin admin = adminService.AdminLogin(account,psw);
            Map<String,String> payload = new HashMap<>();
            payload.put("account",admin.getAccount());
            payload.put("role", admin.getRole());
            //生成JWT令牌
            String token = JwtUtils.getToken(payload);
            map.put("account",admin.getAccount());
            map.put("name",admin.getName());
            map.put("role",admin.getRole());
            map.put("token",token);//响应token
        } catch (Exception e) {
            map.put("state","false");
            map.put("msg",e.getMessage());
        }
        return new RestBean(200,"success",map);
    }
}
