package com.budaos.utils.fastdfs;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

/**
 * fastfds工具类
 * 
 * @author zhuq
 *
 */
public class FastDfsUtils {

	/**
	 * 加载配置文件
	 */
	static {
		try {
			ClientGlobal.init("fastdfs.conf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param fileLocalName 文件绝对路径
	 * @return String[] 上传成功返回[0]为所在组，[1]为文件路径
	 */
	public static String[] uploadFile(String fileLocalName) {
		String[] results = null;
		TrackerServer trackerServer = null;
		try {
			TrackerClient tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			results = storageClient.upload_file(fileLocalName, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				trackerServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * 上传文件
	 * 
	 * @param fileLocalName 文件绝对路径
	 * @param nvps          文件属性
	 * @return String[] 上传成功返回[0]为所在组，[1]为文件路径
	 */
	public static String[] uploadFile(String fileLocalName, NameValuePair[] nvps) {
		String[] results = null;
		TrackerServer trackerServer = null;
		try {
			TrackerClient tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			results = storageClient.upload_file(fileLocalName, null, nvps);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				trackerServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * 上传文件
	 * 
	 * @param is          文件输入流
	 * @param fileExtName 文件后缀名(不带.号)
	 * @return String[] 上传成功返回[0]为所在组，[1]为文件路径
	 */
	public static String[] uploadFileInputStrean(InputStream is, String fileExtName) {
		String[] results = null;
		TrackerServer trackerServer = null;
		try {
			TrackerClient tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			results = storageClient.upload_file(is.toString(), fileExtName, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				trackerServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * 上传文件
	 * 
	 * @param is          文件输入流
	 * @param fileExtName 文件后缀名(不带.号)
	 * @param nvps        文件属性
	 * @return String[] 上传成功返回[0]为所在组，[1]为文件路径
	 */
	public static String[] uploadFileInputStrean(InputStream is, String fileExtName, NameValuePair[] nvps) {
		String[] results = null;
		TrackerServer trackerServer = null;
		try {
			TrackerClient tracker = new TrackerClient();
			trackerServer = tracker.getConnection();
			StorageClient storageClient = new StorageClient(trackerServer, null);
			results = storageClient.upload_file(is.toString(), fileExtName, nvps);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				trackerServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * 获取访问服务器的token，拼接到地址后面
	 *
	 * @param filepath 文件路径 group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @return 返回token，如： token=078d370098b03e9020b82c829c205e1f&ts=1508141521
	 */
	public static String getToken(String filepath) {
		int ts = (int) Instant.now().getEpochSecond();
		String token = "null";
		try {
			token = ProtoCommon.getToken(filepath, ts, ClientGlobal.g_secret_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("token=").append(token);
		sb.append("&ts=").append(ts);
		return sb.toString();
	}

	/**
	 * 获取访问服务器的token，拼接到地址后面
	 *
	 * @param filepath 文件路径 group1/M00/00/00/wKgzgFnkTPyAIAUGAAEoRmXZPp876.jpeg
	 * @return 返回String[]，[0]为token,[1]为ts
	 */
	public static String[] getTokenForArrays(String filepath) {
		int ts = (int) Instant.now().getEpochSecond();
		String token = null;
		try {
			token = ProtoCommon.getToken(filepath, ts, ClientGlobal.g_secret_key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { token, String.valueOf(ts) };
	}
}
