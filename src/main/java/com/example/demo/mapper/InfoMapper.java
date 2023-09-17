package com.example.demo.mapper;

import com.example.demo.entity.Info;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface InfoMapper {

    @Select("select * from info limit #{limit} offset #{offset}")
    public Info[] findAllInfos(@Param("offset")int offset,@Param("limit")int limit);

    @Select("select * from info where id=#{id}")
    public Info findInfoById(@Param("id") String id);

    @Select("select COUNT(*) from info where name like '%${name}%' and tel like '%${tel}%' and (order_id like '%${orderId}%' ${another})")
    public int findInfoSum(@Param("name")String name, @Param("tel")String tel, @Param("orderId")String orderId,@Param("another")String another);

//    @Insert("insert into info(name,tel,address,order_id,create_at) value(#{name},#{tel},#{address},#{orderId},#{createAt})")
    @Insert("insert into info(id,name,tel,address,order_id,create_at,sf_id) value(#{id},#{name},#{tel},#{address},#{orderId},#{createAt},#{sfId}) ON DUPLICATE KEY UPDATE\n" +
            "name = VALUES(name), tel = VALUES(tel),address=values(address),order_id=values(order_id),create_at=values(create_at),sf_id=values(sf_id);")
    public boolean loadInfo(Info info);

    @Delete("delete from info where id=#{id}")
    public Boolean dropInfoById(@Param("id")int id);

    @Select("select * from info where name like '%${name}%' and tel like '%${tel}%' and (order_id like '%${orderId}%' ${another}) order by id desc limit #{limit} offset #{offset}")
    public Info[] findInfoByNameAndTelAndAddress(@Param("name")String name, @Param("tel")String tel, @Param("orderId")String orderId,@Param("offset")int offset,@Param("limit")int limit,@Param("another")String another);

    @Select("select * from info where name like '%${name}%' and tel like '%${tel}%' order by id desc")
    public Info[] findInfoByNameAndTel(@Param("name")String name, @Param("tel")String tel);

}
