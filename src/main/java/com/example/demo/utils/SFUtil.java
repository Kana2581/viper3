package com.example.demo.utils;

import com.sf.csim.express.service.CallExpressServiceTools;
import com.sf.csim.express.service.HttpClientUtil;
import com.sf.csim.express.service.IServiceCodeStandard;
import com.sf.csim.express.service.code.ExpressServiceCodeEnum;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SFUtil {
    private static final String CLIENT_CODE = "Y72HUTRX"; //此处替换为您在丰桥平台获取的顾客编码
    private static final String CHECK_WORD = "MQpU5D7tYK6bYhXZlYCTprQKbTvNPWIb";//此处替换为您在丰桥平台获取的校验码
    //沙箱环境的地址
    private static final String CALL_URL_BOX = "https://sfapi-sbox.sf-express.com/std/service";
    //生产环境的地址
    private static final String CALL_URL_PROD = "https://sfapi.sf-express.com/std/service";


    // 获取订单号
    public static ArrayList<String> getSFCode(String name, String tel, String address) throws UnsupportedEncodingException {
        IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_CREATE_ORDER; //下订单

        // 格式化日期为指定格式
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String formattedDate = currentDate.format(formatter);
        Random random = new Random();
        String timeStamp = String.valueOf(System.currentTimeMillis());


        CallExpressServiceTools tools = CallExpressServiceTools.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        String orderNum = formattedDate + (random.nextInt(900000) + 100000);
        String msgData = "{\"cargoDetails\":[{\"count\":1,\"unit\":\"个\",\"amount\":1,\"currency\":\"RMB\",\"name\":\"书\", \"sourceArea\":\"CHN\"}],\"contactInfoList\":[{\"address\":\"广东省深圳市南山区软件产业基地11栋\",\"contact\":\"小王\",\"contactType\":1,\"country\":\"CN\",\"tel\":\"4006789888\"},{\"address\":\"" + name + "\",\"company\":\"顺丰速运\",\"contact\":\"" + address + "\",\"contactType\":2,\"country\":\"CN\",\"tel\":\"" + tel + "\"}],\"language\":\"zh_CN\",\"orderId\":\"" + orderNum + "\"}";
        params.put("partnerID", CLIENT_CODE); // 顾客编码
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode", standardService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        params.put("msgDigest", tools.getMsgDigest(msgData, timeStamp, CHECK_WORD));

        String result = HttpClientUtil.post(CALL_URL_BOX, params);

        System.out.println(params);
        System.out.println("===调用地址 ===" + CALL_URL_BOX);
        System.out.println("===顾客编码 ===" + CLIENT_CODE);
        System.out.println("===返回结果：" + result);


        Pattern pattern = Pattern.compile("\\\"SF([0-9]*)");
        Matcher matcher = pattern.matcher(result);
        // 查找匹配项
        if (matcher.find()) {
            String waybillNo = matcher.group(1);
            ArrayList<String> strs = new ArrayList<String>();
            strs.add(orderNum);
            strs.add(waybillNo);
            return strs;
        }
        throw new UnsupportedEncodingException("no");
    }


    // 获取运单细节
    public static String GetOrderDetail(String OrderId) throws UnsupportedEncodingException{
        IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_SEARCH_ROUTES;//查路由

        // 时间戳
        String timeStamp = String.valueOf(System.currentTimeMillis());

        // 顺丰服务参数编码工具
        CallExpressServiceTools tools = CallExpressServiceTools.getInstance();

        // 请求参数
        String msgData = "{\"trackingType\": \"1\",\"trackingNumber\":\""+OrderId+"\"}";

        // 参数列表
        Map<String, String> params = new HashMap<String, String>();
        params.put("partnerID",CLIENT_CODE);
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode",standardService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        params.put("msgDigest", tools.getMsgDigest(msgData, timeStamp, CHECK_WORD));

        return HttpClientUtil.post(CALL_URL_BOX, params);

    }
}
