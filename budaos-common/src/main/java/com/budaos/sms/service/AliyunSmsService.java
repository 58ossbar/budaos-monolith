package com.budaos.sms.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 阿里云短信服务类
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Service(value = "AliyunSmsService")
public class AliyunSmsService {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${com.budaos.accessKeyId:}")
    private String accessKeyId;

    @Value(value = "${com.budaos.accessKeySecret:}")
    private String accessKeySecret;

    @Value(value = "${com.budaos.signName:}")
    private String signName;

    @Value(value = "${com.budaos.regionId:cn-hangzhou}")
    private String regionId;

    /**
     * 发送短信
     *
     * @param mobile         手机号码
     * @param templateCode   模版ID
     * @param templateParam  模版参数
     * @return 是否发送成功
     */
    public boolean sendSms(String mobile, String templateCode, String templateParam) {
        return sendSms(mobile, templateCode, templateParam, accessKeyId, accessKeySecret, signName);
    }

    /**
     * 发送短信
     *
     * @param mobile         手机号码
     * @param templateCode   模版ID
     * @param templateParam  模版参数
     * @param accessKeyId    访问ID
     * @param accessKeySecret 访问密钥
     * @param signName       短信签名
     * @return 是否发送成功
     */
    public boolean sendSms(String mobile, String templateCode, String templateParam,
            String accessKeyId, String accessKeySecret, String signName) {
        try {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret)
                    .setEndpoint("dysmsapi.aliyuncs.com")
                    .setRegionId(regionId);

            Client client = new Client(config);

            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(mobile)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam(templateParam);

            SendSmsResponse response = client.sendSms(request);

            log.debug("短信发送结果状态：{}", response.getBody().getCode());
            log.debug("短信发送结果信息：{}", response.getBody().getMessage());

            if ("OK".equals(response.getBody().getCode())) {
                return true;
            }
        } catch (Exception e) {
            log.error("发送短信失败", e);
        }
        return false;
    }
}
