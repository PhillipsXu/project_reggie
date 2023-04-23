package com.pf.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SMSUtil {

    private static final String sianName = "瑞吉外卖";
    private static final String templateCode = "SMS-*****";
    private static final String regionId = "cn-hangzhou";
    private static final String accessKeyId = "";
    private static final String secret = "";

    @CreateCache(area = "SMS", name = "phone_", expire = 15, timeUnit = TimeUnit.MINUTES)
    private static Cache<String, String> cache;

    public static void send(String phoneNumber) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, secret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId(regionId);
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(sianName);
        request.setTemplateCode(templateCode);

        String code = generateCode();
        cache.put(phoneNumber, code);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        log.info("SMS code       : {}", code);
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            log.info("SMS status     : 发送成功！");
        } catch (ClientException e) {
            log.error("SMS status     : 发送失败！");
            log.error("Exception      : {}", e.getLocalizedMessage());
        }
    }

    private static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    public static boolean verifyCode(String phoneNumber, String code) {
        String cacheCode = cache.get(phoneNumber);
        log.info("request code   : {}", code);
        log.info("cache code     : {}", cacheCode);
        return Objects.equals(cacheCode, code);
    }
}
