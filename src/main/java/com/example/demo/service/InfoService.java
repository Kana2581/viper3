package com.example.demo.service;

import com.example.demo.entity.Info;
import com.example.demo.mapper.InfoMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class InfoService {
    @Resource
    InfoMapper infoMapper;

    public Info[] getAllInfos(int offset,int limit)
    {
        return infoMapper.findAllInfos(offset,limit);
    }

    public boolean addInfo(Info info)
    {

        return infoMapper.loadInfo(info);
    }

    public boolean deleteInfo(int id)
    {
        return infoMapper.dropInfoById(id);
    }


    public Info getInfo(String id)
    {
        return infoMapper.findInfoById(id);
    }

    public Info[] getInfoByNameAndTel(String name,String tel){
        return infoMapper.findInfoByNameAndTel(name,tel);
    }

    public int getInfoSum(String name,String tel,String orderId,String another)
    {
        return infoMapper.findInfoSum(name,tel,orderId,another);
    }

    public Info[] getInfo(String name,String tel,String orderId,int offset,int limit,String another)
    {
        return infoMapper.findInfoByNameAndTelAndAddress(name,tel,orderId,offset,limit,another);
    }

    public ByteArrayInputStream exportDataToExcel(Info[] dataList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("收件人");
        headerRow.createCell(1).setCellValue("联系电话");
        headerRow.createCell(2).setCellValue("收件地址");
        headerRow.createCell(3).setCellValue("订单编号");
        headerRow.createCell(4).setCellValue("创建时间");

        // 填充数据
        int rowNum = 1;
        for (Info data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getName());
            row.createCell(1).setCellValue(data.getTel());
            row.createCell(2).setCellValue(data.getAddress());
            row.createCell(3).setCellValue(data.getOrderId());
            row.createCell(4).setCellValue(data.getCreateAt());
        }

        // 将Workbook转换为字节数组流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
