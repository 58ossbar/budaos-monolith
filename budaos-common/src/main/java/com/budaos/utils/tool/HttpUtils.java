package com.budaos.utils.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {

	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String encode = "UTF-8";

	/**
	 * @Description 
		发送post请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param content 请求参数
	 * @return String 结果信息
	 */
	public static String sendPost(String strUrl,String content){
		return sendPost(strUrl,content,null,15);
	}

	/**
	 * @Description 
		发送post请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param content 请求参数
	 * @return String 结果信息
	 */
	public static String sendPost(String strUrl,String content,Map<String,String> headParams){
		return sendPost(strUrl,content,headParams,15);
	}

	/**
	 * @Description 
		发送post请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param content 请求参数
	 * @param headParams 请求头部属性
	 * @param timeout 超时时间(s)
	 * @return String 结果信息
	 */
	public static String sendPost(String strUrl,String content,Map<String,String> headParams,int timeout){
		StringBuffer strResponse = new StringBuffer();
		HttpURLConnection connection = null;
		DataOutputStream out = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(strUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			if(headParams!=null){
				Iterator<String> heads = headParams.keySet().iterator();
				while(heads.hasNext()){
					String key = heads.next();
					connection.setRequestProperty(key, headParams.get(key));
				}
			}
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout*1000);
			connection.setReadTimeout(timeout*1000);
			connection.connect();
			out = new DataOutputStream(connection
					.getOutputStream());
			out.write(content.toString().getBytes(encode));
			out.flush();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encode));
			String line = "";
			while ((line = reader.readLine()) != null) {
				strResponse.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				};
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				};
			}
		}
		return strResponse.toString();
	}
	/**
	 * @Description 
		发送get请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @return String 结果信息
	 */
	public static String sendGet(String strUrl){
		return sendGet(strUrl, null, 15);
	}
	
	/**
	 * @Description 
		发送get请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @return String 结果信息
	 */
	public static String sendGet(String strUrl, int timeout){
		return sendGet(strUrl, null, timeout);
	}
	
	/**
	 * @Description 
		发送get请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param params 请求参数
	 * @return String 结果信息
	 */
	public static String sendGet(String strUrl,String params){
		strUrl = strUrl + (StrUtils.isEmpty(params)?"":params);
		return sendGet(strUrl, null, 15);
	}

	/**
	 * @Description 
		发送get请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param params 请求参数
	 * @param headParams 请求头部属性
	 * @return String 结果信息
	 */
	public static String sendGet(String strUrl,String params,Map<String,String> headParams){
		strUrl = strUrl+(StrUtils.isEmpty(params)?"":params);
		return sendGet(strUrl, headParams, 15);
	}

	/**
	 * @Description 
		发送get请求
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param params 请求参数
	 * @param headParams 请求头部属性
	 * @param timeout 超时时间(s)
	 * @return String 结果信息
	 */
	public static String sendGet(String strUrl,Map<String,String> headParams,int timeout){
		StringBuffer strResponse = new StringBuffer();
		URL url = null;
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			url = new URL(strUrl);
			connection = (HttpURLConnection) url.openConnection();
			if(headParams!=null){
				Iterator<String> heads = headParams.keySet().iterator();
				while(heads.hasNext()){
					String key = heads.next();
					connection.setRequestProperty(key, headParams.get(key));
				}
			}
			connection.setConnectTimeout(timeout*1000);
			connection.setReadTimeout(timeout*1000);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encode));
			String line = "";
			while ((line = reader.readLine()) != null) {
				strResponse.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return strResponse.toString();
	}
	public static byte[] httpGet(String url) throws Exception{
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
   	 	conn.setUseCaches(false);
   	    conn.setDoInput(true);
   	    conn.setDoOutput(true);
   	    conn.setRequestMethod("GET");
   	    conn.setInstanceFollowRedirects(true);
   	    conn.setConnectTimeout(60*1000);
   	    conn.setReadTimeout(60*1000);
   	    conn.setRequestProperty("Content-Type","application/json");
   	    conn.setRequestProperty("Accept-Charset", "utf-8");
   	    conn.setRequestProperty("contentType", "utf-8");
   	    InputStream input = null;
	    if (conn.getResponseCode() == 200) {
	      input = conn.getInputStream();
	    } else {
	      input = conn.getErrorStream();
	    }
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while ((len = input.read(buffer)) != -1) {
	      outStream.write(buffer, 0, len);
	    }
	    byte[] data = outStream.toByteArray();
	    outStream.close();
	    input.close();
	    //callback.invok(data);
	    
	    return data;
	}
	
	public static byte[] httpPost(String url,String paras) throws IOException{
		
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
   	 	conn.setUseCaches(false);
   	    conn.setDoInput(true);
   	    conn.setDoOutput(true);
   	    conn.setRequestMethod("POST");
   	    conn.setInstanceFollowRedirects(true);
   	    conn.setConnectTimeout(30000);
   	    conn.setReadTimeout(30000);
   	    conn.setRequestProperty("Content-Type","application/json");
   	    conn.setRequestProperty("Accept-Charset", "utf-8");
   	    conn.setRequestProperty("contentType", "utf-8");
   	 PrintWriter out = new PrintWriter(conn.getOutputStream()); 
   	   
     // 发送请求参数  
     out.print(paras);  
     // flush输出流的缓冲  
     out.flush();  
   	    InputStream input = null;
	    if (conn.getResponseCode() == 200) {
	      input = conn.getInputStream();
	    } else {
	      input = conn.getErrorStream();
	    }
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    int len = 0;
	    while ((len = input.read(buffer)) != -1) {
	      outStream.write(buffer, 0, len);
	    }
	    byte[] data = outStream.toByteArray();
	    outStream.close();
	    input.close();
	    //callback.invok(data);
	    
	    return data;

	}
	/**
	 * @Description 
		模拟前台表单上传文件
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param fileName 文件绝对路径
	 * @return String 结果信息
	 */
	public static String uploadFile(String strUrl, String fileName) {
		try {
			File file = new File(fileName);
			return uploadFile(strUrl, file.getName(), new FileInputStream(file), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
    }
	/**
	 * @Description 
		模拟前台表单上传文件
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param fileName 文件绝对路径
	 * @param param 其他表单参数
	 * @return String 结果信息
	 */
	public static String uploadFile(String strUrl, String fileName, Map<String,Object> param) {
		try {
			File file = new File(fileName);
			return uploadFile(strUrl, file.getName(), new FileInputStream(file), param);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
    }
	/**
	 * @Description 
		模拟前台表单上传文件
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param file 文件对象
	 * @return String 结果信息
	 */
	public static String uploadFile(String strUrl, File file) {
		try {
			return uploadFile(strUrl, file.getName(), new FileInputStream(file), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
    }
	/**
	 * @Description 
		模拟前台表单上传文件
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param file 文件对象
	 * @param param 其他表单参数
	 * @return String 结果信息
	 */
	public static String uploadFile(String strUrl, File file, Map<String,Object> param) {
		try {
			return uploadFile(strUrl, file.getName(), new FileInputStream(file), param);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
    }
	/**
	 * @Description 
		模拟前台表单上传文件
	 * @Author qiang.zhu
	 * @param strUrl url网址
	 * @param fileName 文件名称
	 * @param fis 输入流(文件)
	 * @return String 结果信息
	 */
	public static String uploadFile(String strUrl, String fileName, InputStream fis,Map<String,Object> param) {
		OutputStream out = null;
		HttpURLConnection conn = null;
		DataInputStream in = null;
		BufferedReader br = null;
		StringBuffer strResponse = new StringBuffer();
		try {
			// 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = Identities.uuid();
            // 服务器的域名
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            in = new DataInputStream(fis);
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行

            out.write(newLine.getBytes());
            if(param!=null){
				Iterator<String> heads = param.keySet().iterator();
				while(heads.hasNext()){
					String key = heads.next();
					StringBuilder sbb = new StringBuilder();
					sbb.append(boundaryPrefix);
					sbb.append(BOUNDARY);
					sbb.append(newLine);
					sbb.append("Content-Disposition: form-data; name=\""+key+"\"");
					sbb.append(newLine);
					sbb.append("Content-Type: text/plain; charset=UTF-8 ");
		            // 参数头设置完以后需要两个换行，然后才是参数内容
					sbb.append(newLine);
					sbb.append(newLine);
					sbb.append(param.get(key));
					sbb.append(newLine);
					out.write(sbb.toString().getBytes());
				}
			}
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), encode));
			String line = "";
			while ((line = br.readLine()) != null) {
				strResponse.append(line);
			}
        } catch (Exception e) {
            System.out.println("发送POST文件请求出现异常！" + e);
            e.printStackTrace();
        } finally {
        	if(out != null){
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
        	}
        	if(in != null){
        		try {
        			in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
        	}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				};
			}
        	if(conn != null){
        		conn.disconnect();
        	}
		}
		return strResponse.toString();
    }
}
