package com.example.demo.Controllers;

import com.example.demo.entity.Info;
import com.example.demo.entity.RestBean;
import com.example.demo.service.InfoService;
import com.example.demo.utils.BaseUtils;
import com.example.demo.utils.SFUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@RestController
public class InfoApiController {
    @Resource
    InfoService infoService;

    @PostMapping("/info")
    public RestBean postInfo(@RequestBody Info info) {
        if (info.getId() == 0) {
            info.setCreateAt(BaseUtils.GetTimeString());
        }
        if (info.getOrderId() == null)
            try {
                ArrayList<String> ids = SFUtil.getSFCode(info.getName(), info.getTel(), info.getAddress());
                info.setOrderId("SF"+ids.get(1));
                info.setSfId(ids.get(0));

            } catch (UnsupportedEncodingException e) {
                return new RestBean(400, "error");
            }
        return new RestBean(200, "success", infoService.addInfo(info));
    }

    @DeleteMapping("/info/delete")
    public RestBean deleteInfo(@RequestParam("id") int id) {
        return new RestBean(200, "success", infoService.deleteInfo(id));
    }

    @GetMapping("/info/detail")
    public RestBean searchInfo(@RequestParam("id") String id,@RequestParam("name") String name,@RequestParam("tel") String tel) {
        Info info;
        if(id == "") {
            info = infoService.getInfoByNameAndTel("林琳琳", "18772435041")[0];
        }else {
            info = infoService.getInfo(id);
        }
        if(info == null)
            return new RestBean(400,"SFError");
        try {
            return new RestBean(200, "success", SFUtil.GetOrderDetail(info.getOrderId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new RestBean(400,"SFError");
        }
    }

    @GetMapping("/info/search")
    public RestBean searchInfo(@RequestParam("name") String name, @RequestParam("tel") String tel, @RequestParam("orderId") String orderId, @RequestParam("page") int page, @RequestParam("limit") int limit) {
        page--;
        int offset = page * limit;
        String another = orderId.equals("") ? "or isnull(order_id)" : "";
        return new RestBean(200, "" + infoService.getInfoSum(name, tel, orderId, another), infoService.getInfo(name, tel, orderId, offset, limit, another));
    }

    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportDataToExcel(@RequestParam("name") String name, @RequestParam("tel") String tel, @RequestParam("orderId") String orderId) throws IOException {
        String another = orderId.equals("") ? "or isnull(order_id)" : "";
        Info[] dataList = infoService.getInfo(name, tel, orderId, 0, 10000, another);

        ByteArrayInputStream in = infoService.exportDataToExcel(dataList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + BaseUtils.GetTimeString() + ".xlsx");

        byte[] bytes = new byte[in.available()];
        in.read(bytes);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(bytes));
    }
}
