package com.budaos.modules.job;

import com.budaos.modules.common.CloudPanUtils;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.utils.tool.OfficeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("cloudPanJob")
public class CloudPanJob {

	private Logger log = LoggerFactory.getLogger(getClass());
	// 外网域名
	@Value("${com.budaos.loginBackUrl}")
	private String domain;
	// 云盘专属显示路径
    @Value("${com.budaos.file-access-path-cloud-pan:/uploads/cloud-pan}")
    private String accessPath;
	
	public final List<String> docsSuffixList = Arrays.asList(".docx", ".doc", ".pptx", ".ppt");
	
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private CloudPanUtils cloudPanUtils;
	
	public void doJob() {
		// 首先找到path字段为空的且后缀属于
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("suffixList", docsSuffixList);
		List<Map<String,Object>> list = new ArrayList<>();//tcloudPanFileMapper.selectSimpleInfoList(params);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> item = list.get(i);
				String dirId = item.get("dirId").toString();
				TcloudPanDirectory directory = tcloudPanDirectoryMapper.selectObjectById(dirId);
				if (directory == null) {
					continue;
				}
				String pkgId = directory.getPkgId();
				String fileId = item.get("fileId").toString();
				String name = item.get("name").toString();
				String existedUrl = item.get("fileAccessUrl").toString();
				String value = cloudPanUtils.getValue(dirId, name);
				String fileAccessUrl = cloudPanUtils.getFileAccessUrlByValue(pkgId, value);
				String fileSavePath = cloudPanUtils.getFileSavePathByValue(pkgId, item.get("fileSavePath").toString());
				try {
					int pos = fileSavePath.lastIndexOf(".");
					if (pos < 0) {
						continue;
					}
					String str = fileSavePath.substring(0, pos);
					String savePath = str + ".pdf";
					log.debug("文档访问的url:" + domain + fileAccessUrl);
					log.debug("pdf保存的绝对路径：" + savePath);
					//先使用A方案
					boolean isSuccess = OfficeUtils.officeToPdfPlanA(domain + fileAccessUrl, savePath);
					//如果A方案失败，尝试一下B方案
					if (!isSuccess) {
						isSuccess = OfficeUtils.officeToPdfPlanB(domain + fileAccessUrl, savePath);
					}
					if (isSuccess) {
						TcloudPanFile t = new TcloudPanFile();
						t.setFileId(fileId);
						t.setPath(existedUrl.substring(0, existedUrl.lastIndexOf(".")) + ".pdf");
						log.debug("外网PDF访问:" + t.getPath());
						tcloudPanFileMapper.update(t);
					}
				} catch (Exception e) {
					log.error("文件：" + domain + fileAccessUrl);
					log.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
	}
	
}
