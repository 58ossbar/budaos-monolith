package com.budaos.modules.common;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.service.TsysSettingsServiceImpl;
import com.budaos.modules.sys.domain.TsysSettings;
import com.budaos.utils.tool.HttpUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序二维码工具类
 *
 */
@Component
public class QrcodeUtils {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	// 上传地址
	@Value("${com.budaos.file-upload-path}")
	private String budaosFieUploadPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TsysSettingsServiceImpl tsysSettingsServiceImpl;
	

	/**
	 * 生成小程序二维码
	 * @param args 小程序二维码后面携带的参数(示例：name1=value1&name2=value2)
	 * @param index 属性文件中文件上传目录对应的下标，默认为0，二维码会被上传至对应的目录
	 * @return 存储的二维码图片文件名称(示例：qrcode.png)
	 */
	public String getQrcode(String args, String index) {
		index = StrUtils.isEmpty(index) ? "0" : index;
		String newFileName = Identities.uuid() + ".png";
		return getQrcode(args, budaosFieUploadPath + uploadPathUtils.getPathByParaNo(index), newFileName) ? newFileName : null;
	}
	
	/**
	 * 生成小程序二维码
	 * @param args 小程序二维码后面携带的参数(示例：name1=value1&name2=value2)
	 * @param storePath 二维码保存路径(示例：d://uploads)
	 * @param storeName 二维码保存文件名称(示例：qrcode.png)
	 * @return boolean 成功/失败
	 */
	private boolean getQrcode(String args, String storePath, String storeName) {
		byte[] result = getQrcodeNoStore(args);
		try {
			JSONObject obj = JSONObject.parseObject(new String(result, "UTF-8"));
			log.debug("二维码生成失败：" + obj.toJSONString());
			return false;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			File file = new File(storePath + File.separator + storeName);
			FileOutputStream os = new FileOutputStream(file);
            os.write(result);
            os.flush();
            os.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 生成小程序二维码
	 * @param args 小程序二维码后面携带的参数(示例：name1=value1&name2=value2)
	 * @return byte[] 二维码图片数据流字节数组
	 * @apiNote
	 * 官方文档地址：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html#HTTPS%20%E8%B0%83%E7%94%A8
	 */
	private byte[] getQrcodeNoStore(String args) {
		String params = "?access_token=" + getAccessToken();
		JSONObject obj = new JSONObject();
		obj.put("scene", args);
		//obj.put("page", "pages/home/index/index"); // 以前小程序未分包时的路径地址
		obj.put("page", "pages/tabbar/home/home"); // 分包后
		obj.put("width", 860);
		obj.put("auto_color", true);
		log.debug("获取小程序二维码url：" + "https://api.weixin.qq.com/wxa/getwxacodeunlimit" + params);
		log.debug("获取小程序二维码参数：" + obj.toJSONString());
		try {
			return HttpUtils.httpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit" + params, obj.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	private String getAccessToken() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("settingType", "wx");
		map.put("settingCode", "wxAccessToken");
		R r = tsysSettingsServiceImpl.querySetting(map);
		@SuppressWarnings("unchecked")
		List<TsysSettings> list = (List<TsysSettings>) r.get("data");
		if (list != null && list.size() > 0) {
			return list.get(0).getSettingValue();
		}
		return "35_4C-ZHgxjyuIslJ9XmnLyzfSMn5p_gBwDYJSyh5nanpciJVk30RZt2y9Lhqffcp_RHi4lGkXP3kUcj8rsK9qMoIhtb0ffIxtgprpHi5RuJLghY3_oR4J7lZvd4MvqS1XgDopvcShJMrH0Me1TPLEdAHALHA";
	}
	
}
