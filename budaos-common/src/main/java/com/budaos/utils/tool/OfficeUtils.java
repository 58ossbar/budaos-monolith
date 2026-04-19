package com.budaos.utils.tool;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * office工具类
 * @author zhuq
 *
 */
public class OfficeUtils {


	private static Logger log = LoggerFactory.getLogger(OfficeUtils.class);
	
	/**
	 * 将office文档(暂只支持word和ppt)转换成pdf
	 * @param fileUrl 文档访问的url（保证外网可直接访问）
	 * @param savePath pdf保存的绝对路径
	 * @return
	 */
	public static boolean officeToPdfPlanA(String fileUrl, String savePath) {
		log.debug("正在转换：" + fileUrl);
		String wordType = fileUrl.substring(fileUrl.lastIndexOf("."));
		if(wordType.toUpperCase().indexOf("DOC") >= 0) {
			wordType = "word";
		}else if(wordType.toUpperCase().indexOf("XLS") >= 0) {
			wordType = "excel";
		}else if(wordType.toUpperCase().indexOf("PPT") >= 0) {
			wordType = "ppt";
		}
		try {
			HttpUtils.sendGet(fileUrl);
			fileUrl = URLEncoder.encode(fileUrl, "utf-8");
			String result = HttpUtils.sendGet("https://officeonline.superlib.com/op/view.aspx?src=" + fileUrl, 60*1000);
			Pattern pt = Pattern.compile(".*WOPISrc=(.*?)'.*");
	    	Matcher match = pt.matcher(result);
	    	while(match.find()) {
				String url = match.group(1);
				url = decodeUnicode(url);
				log.debug("已获取得WOPISrc：" + url);
				log.debug("转换文档类型：" + wordType);
				JSONObject obj = new JSONObject();
				obj.put("useNamedAction", false);
				obj.put("presentationId", "WOPISrc=" + url);
				Map<String, String> map = new HashMap<>();
				map.put("Content-Type", "application/json");
				if("ppt".equals(wordType)) {
					String res = HttpUtils.sendPost("https://officeonline.superlib.com/p/ppt/view.svc/jsonAnonymous/Print", obj.toJSONString(), map, 60*1000);
					log.debug("pdf资源json：" + res);
					JSONObject po = JSONObject.parseObject(res);
					String strUrl = po.getJSONObject("Result").getString("PrintUrl");
					log.debug("已获取得pdf资源url：" + url);
					byte[] httpGet = HttpUtils.httpGet("https://officeonline.superlib.com/p" + strUrl.substring(1));
					if(httpGet.length <= 2048) {
						log.error("获取pdf文件失败：" + new String(httpGet));
						return false;
					}
					FileOutputStream fos = new FileOutputStream(new File(savePath));
					fos.write(httpGet);
					fos.close();
					log.debug("转换成功！");
					return true;
				}else if("word".equals(wordType)) {
					byte[] httpGet = HttpUtils.httpGet("https://officeonline.superlib.com/wv/WordViewer/request.pdf?type=printpdf&WOPIsrc=" + url);
					if(httpGet.length <= 2048) {
						log.error("获取pdf文件失败：" + new String(httpGet));
						return false;
					}
					FileOutputStream fos = new FileOutputStream(new File(savePath));
					fos.write(httpGet);
					fos.close();
					log.debug("转换成功！");
					return true;
				}else if("excel".equals(wordType)) {
					
				}
				
			}
		}catch(Exception e) {
			log.error("转换失败！", e);
	    }
    	return false;
	}

	/**
	 * 将office文档(暂只支持word和ppt)转换成pdf
	 * @param fileUrl 文档访问的url（保证外网可直接访问）
	 * @param savePath pdf保存的绝对路径
	 * @return
	 */
	public static boolean officeToPdfPlanB(String fileUrl, String savePath) {
		log.debug("正在转换：" + fileUrl);
		String wordType = fileUrl.substring(fileUrl.lastIndexOf("."));
		if(wordType.toUpperCase().indexOf("DOC") >= 0) {
			wordType = "word";
		}else if(wordType.toUpperCase().indexOf("XLS") >= 0) {
			wordType = "excel";
		}else if(wordType.toUpperCase().indexOf("PPT") >= 0) {
			wordType = "ppt";
		}
		try {
			HttpUtils.sendGet(fileUrl);
			fileUrl = URLEncoder.encode(fileUrl, "utf-8");
			String result = HttpUtils.sendGet("https://view.officeapps.live.com/op/view.aspx?src=" + fileUrl, 60*1000);
			Pattern pt = Pattern.compile(".*WOPISrc=(.*?)'.*");
	    	Matcher match = pt.matcher(result);
	    	while(match.find()) {
				String url = match.group(1);
				url = decodeUnicode(url);
				log.debug("已获取得WOPISrc：" + url);
				log.debug("转换文档类型：" + wordType);
				JSONObject obj = new JSONObject();
				obj.put("useNamedAction", true);
				obj.put("presentationId", "WOPIsrc=" + url);
				Map<String, String> map = new HashMap<>();
				map.put("Content-Type", "application/json");
				if("ppt".equals(wordType)) {
					String res = HttpUtils.sendPost("https://phk1-powerpoint.officeapps.live.com/p/ppt/view.svc/jsonAnonymous/Print", obj.toJSONString(), map, 60*1000);
					log.debug("pdf资源json：" + res);
					JSONObject po = JSONObject.parseObject(res);
					String strUrl = po.getJSONObject("Result").getString("PrintUrl");
					log.debug("已获取得pdf资源url：" + url);
					byte[] httpGet = HttpUtils.httpGet("https://phk1-powerpoint.officeapps.live.com/p" + strUrl.substring(1));
					if(httpGet.length <= 2048) {
						log.error("获取pdf文件失败：" + new String(httpGet));
						return false;
					}
					FileOutputStream fos = new FileOutputStream(new File(savePath));
					fos.write(httpGet);
					fos.close();
					log.debug("转换成功！");
					return true;
				}else if("word".equals(wordType)) {
					byte[] httpGet = HttpUtils.httpGet("https://phk1-word-view.officeapps.live.com/wv/WordViewer/request.pdf?type=printpdf&WOPIsrc=" + url);
					if(httpGet.length <= 2048) {
						log.error("获取pdf文件失败：" + new String(httpGet));
						return false;
					}
					FileOutputStream fos = new FileOutputStream(new File(savePath));
					fos.write(httpGet);
					fos.close();
					log.debug("转换成功！");
					return true;
				}else if("excel".equals(wordType)) {
					
				}
				
			}
		}catch(Exception e) {
			log.error("转换失败！", e);
	    }
    	return false;
	}
	
	public static String decodeUnicode(String str) {
		Charset set = Charset.forName("UTF-16");
		Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
		Matcher m = p.matcher(str);
		int start = 0;
		int start2 = 0;
		StringBuffer sb = new StringBuffer();
		while (m.find(start)) {
			start2 = m.start();
			if (start2 > start) {
				String seg = str.substring(start, start2);
				sb.append(seg);
			}
			String code = m.group(1);
			int i = Integer.valueOf(code, 16);
			byte[] bb = new byte[4];
			bb[0] = (byte) ((i >> 8) & 0xFF);
			bb[1] = (byte) (i & 0xFF);
			ByteBuffer b = ByteBuffer.wrap(bb);
			sb.append(String.valueOf(set.decode(b)).trim());
			start = m.end();
		}
		start2 = str.length();
		if (start2 > start) {
			String seg = str.substring(start, start2);
			sb.append(seg);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		//officeToPdfPlanA("https://www.budaos.com/bds/uploads/cloud-pan/9a2938f775ed475f9b4ce7d8ea112a6d/讲义PPT/第8章 Docker容器云配置实战.pptx", "d:/1.pdf");
	}
}
