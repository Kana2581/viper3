package com.example.demo.mapper;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Info;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {
    @Select("select * from admins limit #{limit} offset #{offset}")
    public Admin[] findAllAdmins(@Param("offset")int offset,@Param("limit")int limit);

    @Select("SELECT * FROM admins WHERE account LIKE '%${account}%' AND role LIKE '%${role}%' ORDER BY id DESC LIMIT #{page}, #{limit};")
    public Admin[] findAdminsByAccountAndRole(@Param("account") String account,@Param("role")String role,@Param("page")int page,@Param("limit")int limit);

    @Select("SELECT count(*) FROM admins WHERE account LIKE '%${account}%' AND role LIKE '%${role}%' ;")
    public String countAdminsByAccountAndRole(@Param("account") String account,@Param("role")String role);


    @Insert("INSERT INTO admins (id, account, name, psw, role, status, create_at) VALUES (#{id}, #{account},#{name},#{psw},#{role},#{status},#{createAt}) ON DUPLICATE KEY UPDATE account = VALUES(account), name = VALUES(name), psw = VALUES(psw), role = VALUES(role), status = VALUES(status), create_at = VALUES(create_at);")
    public Boolean loadAdmin(Admin admin);

    @Delete("delete from admins where id=#{id}")
    public Boolean dropAdminsById(@Param("id")int id);

    @Select("select * from admins where account=#{account} and psw=#{psw}")
    public Admin findAdminByAccountAndPsw(@Param("account") String account,@Param("psw")String psw);
}
