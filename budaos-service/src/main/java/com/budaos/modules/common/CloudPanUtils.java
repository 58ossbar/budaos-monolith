package com.budaos.modules.common;

import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CloudPanUtils {

	// 云盘专属上传路径
    @Value("${com.budaos.file-upload-path-cloud-pan:C:/uploads/cloud-pan}")
    public String uploadPathCloudPan;
    // 云盘专属显示路径
    @Value("${com.budaos.file-access-path-cloud-pan:/uploads/cloud-pan}")
    public String accessPath;
    
    public final List<String> imageSuffixList = Arrays.asList(".JPEG", ".PNG", ".JPG", ".GIF", ".BMP");
    public final List<String> videoSuffixList = Arrays.asList(".MP4", ".FLV", ".RMVB", ".AVI", ".WMV", ".MOV", ".QUICKTIME");
    public final List<String> audioSuffixList = Arrays.asList(".MP3", ".WAV", ".WMA", ".OGG", ".FLAC", ".AAC", ".M4A");
    public final List<String> blackSuffixList = Arrays.asList(".EXE", ".SH", ".BAT");
    public final List<String> docsSuffixList = Arrays.asList(".PDF", ".DOCX", ".DOC", ".TXT", ".PPTX", ".PPT", ".XLX", ".XLSX", ".CHM");

    public final List<String> unAllowedDirNameList = Arrays.asList("/", "\\", ":", "*", "?", "\"", "<", ">", "|");
    
    public final List<String> canBePdfSuffixList = Arrays.asList(".docx", ".doc", ".pptx", ".ppt");
    
    @Autowired
    private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
    @Autowired
    private TcloudPanFileMapper tcloudPanFileMapper;

    /**
     * 处理文件类型，如：image、video、audio、docs、null
     * @param name
     * @return
     */
    public String getFileType(String name){
        int pos = name.lastIndexOf(".");
        // 没有后缀名
        if (pos < 0) {
            return null;
        }
        String ext = name.substring(pos);
        // 文件类型
        String fileType = "file";
        if (imageSuffixList.contains(ext.toUpperCase())) {
            fileType = "image";
        } else if(videoSuffixList.contains(ext.toUpperCase())) {
            fileType = "video";
        } else if(audioSuffixList.contains(ext.toUpperCase())) {
            fileType = "audio";
        } else if (docsSuffixList.contains(ext.toUpperCase())) {
            fileType = "docs";
        } else {
            fileType = null;
        }
        return fileType;
    }
    
    /**
     * 获取上传地址前缀
     * @return 示例  d:/uploads/cloud-pan
     */
    public String getUploadPath() {
    	return uploadPathCloudPan;
    }
    
    /**
     * 获取显示地址的前缀
     * @return 示例  /uploads/cloud-pan
     */
    public String getAccessPath() {
		return accessPath;
	}
    

    /**
     * 重名的在后面拼接年月日时分秒
     * @param name
     * @param dirId
     * @param loginUserId
     * @param params
     * @return
     */
    public String handleDirectoryName(String name, String dirId, String loginUserId, Map<String, Object> params) {
    	String tempName = name;
    	params.put("parentId", dirId);
		params.put("createUserId", loginUserId);
		List<TcloudPanDirectory> list = tcloudPanDirectoryMapper.selectListByMap(params);
		if (list.stream().anyMatch(a -> a.getName().equals(tempName))) {
			// 则在后面追加_年月日_时分秒
			String now = DateUtils.getNow("yyyyMMdd_HHmmss");
			name = name + "_" + now;
		}
    	return name;
    }
    
    /**
     * 处理文件名称，重名时，返回    名称_年月日_时分秒
     * @param name
     * @param dirId 所在目录
     * @param loginUserId
     * @param params
     * @return
     */
    public String handleFileName(String name, String dirId, String loginUserId, Map<String, Object> params) {
        String tempName = name;
        params.put("dirId", dirId);
        params.put("createUserId", loginUserId);
        List<TcloudPanFile> list = tcloudPanFileMapper.selectListByMap(params);
        if (list.stream().anyMatch(a -> a.getName().equals(tempName))) {
            // 则在后面追加_年月日_时分秒
            String now = DateUtils.getNow("yyyyMMdd_HHmmss");
            // 判断是否有后缀名
            int pos = name.lastIndexOf(".");
            if (pos > 0) {
                String ext = name.substring(pos);
                name = name.substring(0, pos) + "_" + now + ext;
            } else {
                name = name + "_" + now;
            }
        }
        return name;
    }
    
    /**
     * 获取完整的上传路径（携带盘符）
     * @param dirId
     * @param loginUserId 必传参数
     * @param pkgId 必传参数，教学包id
     * @return 示例：D:/uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/实施计划
     */
    public String getPathBy(String dirId, String loginUserId, String pkgId) {
    	if (StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(pkgId)) {
    		return null;
    	}
    	// 最终返回路径
    	String finallyPath = uploadPathCloudPan;
        // 返回用户的根目录
        if (StrUtils.isEmpty(dirId)) {
            finallyPath = uploadPathCloudPan + "/" + pkgId;
            return finallyPath;
        }
        String path = "";
        String res = deep(dirId, path);
        if (res != null) {
            String[] split = res.split("/");
            for (int i=split.length-1; i>=0; i--) {
                path = path + "/" + split[i];
            }
        }
        finallyPath = uploadPathCloudPan + "/" + pkgId  + "/" + path;
        return finallyPath;
    }
    
    public String getPathBy(String dirId, String pkgId) {
    	if (StrUtils.isEmpty(pkgId)) {
    		return null;
    	}
    	// 最终返回路径
    	String finallyPath = uploadPathCloudPan;
        // 返回用户的根目录
        if (StrUtils.isEmpty(dirId)) {
            finallyPath = uploadPathCloudPan + "/" + pkgId;
            return finallyPath;
        }
        String path = "";
        String res = deep(dirId, path);
        if (res != null) {
            String[] split = res.split("/");
            for (int i=split.length-1; i>=0; i--) {
                path = path + "/" + split[i];
            }
        }
        finallyPath = uploadPathCloudPan + "/" + pkgId  + "/" + path;
        return finallyPath;
    }
    
    /**
     * 获取路径（不携带盘符）
     * @param dirId
     * @param pkgId 必传参数，教学包id
     * @return 示例：/489c90e6e5ca40f8a99f8898e5efe5ad/实施计划
     */
	public String getPath(String dirId, String pkgId) {
		if (StrUtils.isEmpty(pkgId)) {
			return null;
		}
		// 最终返回路径
		String finallyPath = pkgId;
	    // 返回用户的根目录
	    if (StrUtils.isEmpty(dirId)) {
	        finallyPath = "/" + pkgId;
	        return finallyPath;
	    }
	    String path = "";
	    String res = deep(dirId, path);
	    if (res != null) {
	        String[] split = res.split("/");
	        for (int i=split.length-1; i>=0; i--) {
	            path = path + "/" + split[i];
	        }
	    }
	    finallyPath = "/" + pkgId + path;
	    return finallyPath;
	}

    private String deep(String dirId, String path) {
        TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(dirId);
        if (directory != null) {
            //if (StrUtils.isNotEmpty(directory.getParentId())) {
        	if (!"0".equals(directory.getParentId())) {
                path += directory.getName() + "/";
                return deep(directory.getParentId(), path);
            } else {
                path += directory.getName();
                return path;
            }
        }
        return null;
    }
	
    /**
     * 获取显示地址（旧版）
     * @param dirId
     * @param loginUserId 必传参数
     * @param name 必传参数，文件名称
     * @return 示例：/uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈2/nginx.png
     */
	/*public String getAccessUrl(String dirId, String loginUserId, String name, String pkgId) {
	    if (StrUtils.isEmpty(dirId) || StrUtils.isEmpty(loginUserId) || StrUtils.isEmpty(name)) {
	        return null;
	    }
	    String path = "";
	    String res = deep(dirId, path);
	    if (res != null) {
	        String[] split = res.split("_");
	        for (int i=split.length-1; i>=0; i--) {
	            path = path + "/" + split[i];
	        }
	    }
	    String finallyPath = accessPath + "/" + pkgId + path + "/" + name;
	    return finallyPath;
	}*/
    
    /**
     * 获取显示地址（旧版）
     * @param path 必传参数，此值通过调用方法 cloudPanUtils.getPath(directoryId, loginUserId, pkgId);
     * @param name 必传参数，文件名称
     * @return 示例：/uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈2/nginx.png
     */
	/*public String getAccessUrl(String path, String name) {
	    if (StrUtils.isEmpty(path) || StrUtils.isEmpty(name)) {
	    	return null;
	    }
	    // path拼接了斜杠，无需再次拼接
	    return accessPath + path + "/" + name;
	}*/
    
    /**
     * 获取保存路径（带盘符）
     * @param dirId
     * @param loginUserId
     * @param name
     * @return 示例 D:/uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈1/nginx12.png
     */
    public String getSavePath(String dirId, String loginUserId, String name, String pkgId) {
    	return getPathBy(dirId, loginUserId, pkgId) + "/" + name;
    }
    
    /**
     * 
     * @param path 拼接了斜杠，无需再次拼接
     * @param name
     * @return 示例 D:/uploads/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈1/nginx12.png
     */
    public String getSavePath(String path, String name) {
    	return uploadPathCloudPan + path + "/" + name;
    }
    
    /**
     * 拼接显示路径
     * @param pkgId
     * @param value
     * @return
     */
	/*public String stitchingPath(Object pkgId, Object value) {
		if (StrUtils.isNull(pkgId) || StrUtils.isNull(value)) {
			return null;
		}
		String url = accessPath + "/" + pkgId + value;
		return url;
	}*/
    
    /**
     * 拼接上传路径
     * @param pkgId
     * @param savePath 表 t_cloud_pan_file的file_save_path
     * @return
     */
	/*public String stitchingAbsolutePath(Object pkgId, Object savePath) {
		if (StrUtils.isNull(pkgId) || StrUtils.isNull(savePath)) {
			return null;
		}
		String url = uploadPathCloudPan + "/" + pkgId + savePath;
		return url;
	}*/
    

    /**
     * 获取文件显示地址或磁盘路径（两个值存到数据库中的值其实已为一致了）
     * @param dirId 目录id，必传
     * @param name 文件名称，必传
     * @return 该方法返回的值是目录与文件名组成的字符传，详情请看笔记，<br>示例：/教学大纲/SpringBoot.ppt
     * @apiNote
     * <br>其中：83a2cc256e6047b8bde16e2f70aa732d 为教学包的主键<br>
     * <br>完整的访问路径fileAccessUrl示例：<br>/uploads/cloud-pan/83a2cc256e6047b8bde16e2f70aa732d/教学大纲/SpringBoot.ppt<br>
     * <br>完整的访问路径fileSavePath示例：<br>/mnt/cbstp/uploads/cloud-pan/83a2cc256e6047b8bde16e2f70aa732d/教学大纲/SpringBoot.ppt<br>
     * <br>所有云盘文件都会被上传至磁盘上的 /mnt/cbstp/uploads/cloud-pan目录下，<br>该值来源属性配置：com.budaos.file-upload-path-cloud-pan<br>
     * <br>accessUrl 数据库中的存的值是由目录名称和文件名称组成<br>
     * <br>---------变量------|教学包的主键|目录名称|目录名称|文件名<br>
     */
    public String getValue(String dirId, String name) {
    	if (StrUtils.isEmpty(dirId) || StrUtils.isEmpty(name)) {
            return null;
        }
    	String path = "";
        String res = deep(dirId, path);
        if (res != null) {
            String[] split = res.split("/");
            for (int i=split.length-1; i>=0; i--) {
                path = path + "/" + split[i];
            }
        }
        String finallyPath = path + "/" + name;
        return finallyPath;
    }
    
    /**
     * 获取拼接好的显示路径
     * @param pkgId 教学包id
     * @param value 
     * <br>① 该值可先通过cloudPanUtils.getValue(dirId, name)获取得到<br>
     * <br>② 或者取数据库记录中的file_access_url字段的值
     * @return 返回显示路径，<br>
     * <br>示例： /uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈1/nginx12.png<br>
     */
    public String getFileAccessUrlByValue(Object pkgId, Object value) {
    	if (StrUtils.isNull(pkgId) || StrUtils.isNull(value)) {
    		return null;
    	}
    	String url = accessPath + "/" + pkgId + value;
    	return url;
    }
    
    /**
     * （注意）这个是，获取完整的磁盘路径
     * @param pkgId 教学包id
     * @param value
     * <br>① 该值可先通过cloudPanUtils.getValue(dirId, name)获取得到<br>
     * <br>② 或者取数据库记录中的file_access_url（file_save_path）字段的值
     * @return
     * <br>Windows示例： D:/uploads/cloud-pan/489c90e6e5ca40f8a99f8898e5efe5ad/嘻嘻哈哈1/nginx12.png<br>
     * <br>Linux示例：/mnt/cbstp/uploads/cloud-pan/83a2cc256e6047b8bde16e2f70aa732d/教学大纲/SpringBoot.ppt<br>
     */
    public String getFileSavePathByValue(Object pkgId, Object value) {
    	if (StrUtils.isNull(pkgId) || StrUtils.isNull(value)) {
    		return null;
    	}
    	String url = uploadPathCloudPan + "/" + pkgId + value;
    	return url;
    }
    
    /**
     * （注意）这个是获取完整的文件显示地址，前端通过域名+这里返回的值即可访问
     * @param pkgId 教学包id
     * @param value
     * @return
     */
    public String getFilePathByValue(Object pkgId, Object value) {
    	if (StrUtils.isNull(pkgId) || StrUtils.isNull(value)) {
    		return null;
    	}
    	String url = accessPath + "/" + pkgId + value;
    	return url;
    }
    
    /**
     * 获取refDirId
     * @param name
     * @param refPkgId
     * @param refDirId
     * @param params
     * @return
     */
	/*public String getRefDirId(String name, String refPkgId, String refDirId, Map<String, Object> params) {
		String dirId = null;
		if (StrUtils.isNotEmpty(refPkgId)) {
			if (StrUtils.isNotEmpty(refDirId)) {
				params.clear();
				params.put("name", name);
				params.put("pkgId", refPkgId);
				//params.put("dirId", refDirId);
				params.put("parentId", refDirId);
				List<TcloudPanDirectory> refList = tcloudPanDirectoryMapper.selectListByMap(params);
				List<TcloudPanDirectory> collect = refList.stream().filter(a -> a.getName().equals(name)).collect(Collectors.toList());
				if (collect != null && collect.size() > 0) {
					refDirId = collect.get(0).getDirId();
				}	
			}
		}
		return dirId;
	}*/
    
    /**
	 * 获取目标在磁盘上的绝对路径
	 * @param targetId 目录id或文件id
	 * @param list
	 * @return
	 */
	public String getAbsolutePath(String targetId, List<Map<String, Object>> list, String pkgId) {
		// 取出目标信息
		List<Map<String, Object>> dataList = list.stream().filter(a -> a.get("id").equals(targetId)).collect(Collectors.toList());
		if (dataList == null || dataList.size() == 0) {
			return null;
		}
		Map<String, Object> data = dataList.get(0);
		// 如果是直接属于教学包id目录下的文件
		if (data.get("parentId").equals(pkgId)) {
			return uploadPathCloudPan + "/" + pkgId + "/" + data.get("name");
		}
		// 用于记录当前文件所在父级路径
		List<Object> pathList = new ArrayList<>();
		recursion(targetId, list, pathList, pkgId);
		String absolutePath = "";
		for (int i = pathList.size() - 1; i >= 0; i--) {
			absolutePath += pathList.get(i) + "/";
		}
		if (StrUtils.isNotEmpty(absolutePath)) {
			absolutePath = uploadPathCloudPan + "/" + pkgId + "/" + absolutePath.substring(0, absolutePath.length() - 1);
		}
		return absolutePath;
	}

	private void recursion(Object id, List<Map<String, Object>> list, List<Object> pathList, String pkgId) {
		// 取出目标信息
		List<Map<String, Object>> dataList = list.stream().filter(a -> a.get("id").equals(id)).collect(Collectors.toList());
		if (dataList == null || dataList.size() == 0) {
			return;
		}
		Map<String, Object> data = dataList.get(0);
		pathList.add(data.get("name"));
		if (!data.get("parentId").equals(pkgId) && !"0".equals(data.get("parentId"))) {
			recursion(data.get("parentId"), list, pathList, pkgId);
		}
	}
}

