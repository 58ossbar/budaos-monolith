package com.budaos.modules.evgl.web.controller.typora;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.common.config.BudaosConfig;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huj
 * @create 2022-01-25 17:06
 * @email hujun@budaos.com
 */
@RestController
@RequestMapping("/typora")
public class TyporaImageController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Value("${com.budaos.baseUrl}")
    private String baseUrl;

    @RequestMapping("/uploadImg")
    public Map<String, Object> uploadImg(@RequestBody  String base64String) {
        if (StrUtils.isEmpty(base64String)) {
            return R.error("参数为空");
        }
        Map<String, Object> map = new HashMap<>();
        String uuid = Identities.uuid();
        String fileName = uuid + ".png";
        base64String = base64String.split(",")[1];//去掉base64码的data:image/png;base64
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(base64String);

            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            String path = BudaosConfig.getUploadPathTypora();
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            String abso = path + fileName;
            OutputStream out = new FileOutputStream(abso);
            out.write(bytes);
            out.flush();
            out.close();
            logger.debug("uuid {}", uuid);
            logger.debug("文件名称 {}", fileName);
            logger.debug("绝对路径 {}", abso);
        } catch (Exception e) {
            System.out.println("系统异常" + e);
            logger.error("上传失败，系统异常 {}", e);
        }
        map.put("code", 200);
        map.put("message", "上传成功");
        Map<String, Object> data = new HashMap<>();
        //data.put("url", budaosConfig.getUploadPathTypora() + "/" + fileName);
        data.put("url", BudaosConfig.getTyporaAccessPath() + "/" + fileName);
        data.put("uuid", uuid);
        map.put("data", data);
        return map;
    }


}
