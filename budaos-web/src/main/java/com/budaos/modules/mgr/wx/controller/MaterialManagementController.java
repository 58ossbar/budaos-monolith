package com.budaos.modules.mgr.wx.controller;

import com.alibaba.fastjson.JSON;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.enums.WxMaterialEnum;
import com.budaos.modules.evgl.weixin.api.WxMaterialService;
import com.budaos.modules.evgl.weixin.params.MaterialParams;
import com.budaos.modules.evgl.weixin.vo.AddMaterialResponseVo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 素材管理：图片、音频、视频，上传、查询
 * @author huj
 * @create 2021-12-29 15:25
 * @email 1552281464@qq.com
 */
@RestController
@RequestMapping("/api/wx/material")
public class MaterialManagementController {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WxMaterialService wxMaterialService;
	
	/**
	 * 微信公众号签名认证接口（成功验证）
	 * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
	 * @param timestamp 时间戳
	 * @param nonce 随机数 随机字符串
	 * @param echostr
	 * @return
	 * @apiNote 详见微信公众号官方文档 https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
	 * <br><br>生产环境验证访问<br>
	 * https://www.budaos.com/manage/api/wx/material/wx
	 * <br><br>测试环境验证访问<br>
	 * https://frp.budaos.com/mgr/api/wx/material/wx
	 */
	@RequestMapping("/wx")
	public String wx(String signature, String timestamp, String nonce, String echostr) {
		log.info("准备校验");
		log.info("signature => {}", signature);
		log.info("timestamp => {}", timestamp);
		log.info("nonce => {}", nonce);
		log.info("echostr => {}", echostr);
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (signature != null && WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
			log.info("验证成功，返回结果 => {}", echostr);
			return echostr;
		}
		return "budaos-666";
	}
	
	@RequestMapping("/getAccessTokenFromWechat")
	public R getAccessTokenFromWechat() {
		return R.ok().put(Constant.R_DATA, wxMaterialService.getAccessToken());
	}
	
	/**
	 * 根据条件查询记录
	 * @param params
	 * @return
	 */
	@PostMapping("/batchgetMaterial")
	public R batchgetMaterial(@RequestBody MaterialParams params) {
		return wxMaterialService.batchgetMaterial(params);
	}
	
	/**
	 * 文件上传
	 * @param multipartFile
	 * @param type
	 * @param title 上传音频/视频输入的标题
	 * @param introduction 描述
	 * @return
	 * @apiNote 详见微信公众号官方文档https://developers.weixin.qq.com/doc/offiaccount/Asset_Management/Adding_Permanent_Assets.html
	 */
	@RequestMapping("/upload")
	public R upload(@RequestPart(value = "file") MultipartFile multipartFile, String type, String title, String introduction) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return R.error("请选择要上传的文件");
		}
		type = StrUtils.isEmpty(type) ? WxMaterialEnum.TYPE_IMAGE.getCode() : type;
		int i = multipartFile.getOriginalFilename().lastIndexOf(".");
		if (i <= 0) {
			return R.error("文件格式不支持");
		}
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		String suffix = multipartFile.getOriginalFilename().substring(i);
		switch (type) {
			case "image":
				if (multipartFile.getSize() > 1024 * 1024 * 10) {
					return R.error("大小不能超过10M");
				}
				List<String> imageSuffixList = Arrays.asList(".BMP", ".JPEG", ".PNG", ".JPG", ".GIF");
				if (!imageSuffixList.contains(suffix.toUpperCase())) {
					return R.error("仅支持bmp/png/jpeg/jpg/gif格式");
				}
				break;
			case "voice":
				if (StrUtils.isEmpty(title)) {
					return R.error("标题不能为空且长度不能超过30字");
				}
				if (multipartFile.getSize() > 1024 * 1024 * 200) {
					return R.error("文件大小不能超过200M");
				}
				List<String> audioSuffixList = Arrays.asList(".MP3", ".WMA", ".WAV", ".AMR", ".M4A");
				if (!audioSuffixList.contains(suffix.toUpperCase())) {
					return R.error("仅支持mp3、wma、wav、amr、m4a格式");
				}
				data.put("title", title);
				data.put("introduction", introduction);
				map.put("description", JSON.toJSONString(data));
				break;
			case "video":
				if (StrUtils.isEmpty(title)) {
					return R.error("标题不能为空且长度不能超过30字");
				}
				if (StrUtils.isNotEmpty(introduction) && introduction.trim().length() > 300) {
					return R.error("视频介绍不能超过300个字");
				}
				if (multipartFile.getSize() > 1024 * 1024 * 20) {
					return R.error("视频大小不能超过20M，超出限制的视频请到腾讯视频上传");
				}
				List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".F4V", ".WEBM", ".M4V", ".MOV", ".3GP", ".3G2", ".RM", ".RMVB", ".WMV", ".AVI", ".ASF", ".MPG", ".MPEG", ".MPE", ".TS", ".DIV", ".DV", ".DIVX");
				// 其它格式
				List<String> otherSuffixList = Arrays.asList(".VOB", ".DAT", ".MKV", ".LAVF", ".CPK", ".DIRAC", ".RAM", ".QT", ".FLI", ".FLC", ".MOD");
				if (!videoSuffixList.contains(suffix.toUpperCase()) && !otherSuffixList.contains(suffix.toUpperCase())) {
					List<String> list = new ArrayList<>();
					list.addAll(videoSuffixList);
					list.addAll(otherSuffixList);
					return R.error("仅支持 " + list.toString() + " 中的格式");
				}
				data.put("title", title);
				data.put("introduction", introduction);
				map.put("description", JSON.toJSONString(data));
				break;
			default:
				break;
		}
		try {
			String fileName = Identities.uuid() + suffix;
			InputStream inputStream = multipartFile.getInputStream();
			map.put("access_token", wxMaterialService.getAccessToken());
			map.put("type", WxMaterialEnum.TYPE_IMAGE.getCode());
			// 图片上传成功，返回结果：{"media_id":"JvzCEUYQxkSI7zUJpZQi9JEMPNh_tSfc_nXfwnnqxYo","url":"http:\/\/mmbiz.qpic.cn\/mmbiz_jpg\/0l7WQX8ib9y4coQw1dCnk7q6EdEEqfWcE1xzFymdcG0KjTZqXzZfMQMyVgxEzsPDYYzIVFdDVIfamoCBBvkeiaRw\/0?wx_fmt=jpeg","item":[]}
			// 音频上传成功，返回结果：{"media_id":"JvzCEUYQxkSI7zUJpZQi9DJEGqG10YmHCPyK842e7vg","item":[]}
			// 视频上传成功，返回结果：{"media_id":"JvzCEUYQxkSI7zUJpZQi9Kquyp0dRe3PttmsJ2mGCG8","item":[]}
			String responseString = uploadFile("https://api.weixin.qq.com/cgi-bin/material/add_material", fileName, inputStream, map);
			log.debug("微信响应结果 => {}", responseString);
			AddMaterialResponseVo addMaterialResponseVo = JSON.parseObject(responseString, AddMaterialResponseVo.class);
			inputStream.close();
			return R.ok().put(Constant.R_DATA, addMaterialResponseVo);
		} catch (Exception e) {
			String msg = "系统错误，文件上传失败";
			log.error(msg + e);
			return R.error(msg);
		}
	}
	
	/**
	 * 文件上传
	 * @param strUrl http请求地址
	 * @param fileName 文件名
	 * @param fis 文件流
	 * @param param
	 * @return
	 */
	public static String uploadFile(String strUrl, String fileName, InputStream fis, Map<String, Object> param) {
		OutputStream out = null;
		HttpURLConnection conn = null;
		DataInputStream in = null;
		BufferedReader br = null;
		StringBuffer strResponse = new StringBuffer();

		try {
			String newLine = "\r\n";
			String boundaryPrefix = "--";
			String BOUNDARY = Identities.uuid();
			URL url = new URL(strUrl);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			out = new DataOutputStream(conn.getOutputStream());
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"" + "\r\n");
			sb.append("Content-Type:application/octet-stream");
			sb.append("\r\n");
			sb.append("\r\n");
			out.write(sb.toString().getBytes());
			in = new DataInputStream(fis);
			byte[] bufferOut = new byte[1024];
			boolean var15 = false;

			int bytes;
			while((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}

			out.write("\r\n".getBytes());
			String line;
			if (param != null) {
				Iterator heads = param.keySet().iterator();

				while(heads.hasNext()) {
					line = (String)heads.next();
					StringBuilder sbb = new StringBuilder();
					sbb.append("--");
					sbb.append(BOUNDARY);
					sbb.append("\r\n");
					sbb.append("Content-Disposition: form-data; name=\"" + line + "\"");
					sbb.append("\r\n");
					sbb.append("Content-Type: text/plain; charset=UTF-8 ");
					sbb.append("\r\n");
					sbb.append("\r\n");
					sbb.append(param.get(line));
					sbb.append("\r\n");
					out.write(sbb.toString().getBytes());
				}
			}

			byte[] end_data = ("--" + BOUNDARY + "--" + "\r\n").getBytes();
			out.write(end_data);
			out.flush();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			line = "";

			while((line = br.readLine()) != null) {
				strResponse.append(line);
			}
		} catch (Exception var35) {
			System.out.println("发送POST文件请求出现异常！" + var35);
			var35.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException var34) {
					var34.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException var33) {
					var33.printStackTrace();
				}
			}

			if (br != null) {
				try {
					br.close();
				} catch (IOException var32) {
				}
			}

			if (conn != null) {
				conn.disconnect();
			}

		}

		return strResponse.toString();
	}
	

	/**
	 * 删除永久素材
	 * @param media_id
	 * @return
	 */
	@PostMapping("/delMaterial")
	public R delMaterial(String media_id) {
		return wxMaterialService.delMaterial(media_id);
	}
	
}
