package com.budaos.modules.job;

import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.activity.persistence.TevglActivityBrainstormingAnswerFileMapper;
import com.budaos.modules.evgl.activity.persistence.TevglActivityVoteQuestionnaireTraineeAnswerFileMapper;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 定时维护删除投票问卷、头脑风暴学员提交的附件（未绑定的）
 * @author huj
 *
 */
@Component("traineeAnswerFileJob")
public class TraineeAnswerFileJob {

	private Logger log = LoggerFactory.getLogger(getClass());
	
    // 文件上传路径
    @Value("${com.budaos.file-upload-path}")
    private String uploadPath;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerFileMapper tevglActivityVoteQuestionnaireTraineeAnswerFileMapper;
	@Autowired
	private TevglActivityBrainstormingAnswerFileMapper tevglActivityBrainstormingAnswerFileMapper;
	
	/**
	 * 定时删除冗余的数据
	 */
	public void doJob() {
		log.debug("执行删除学员提交的冗余附件......");
		deleteVoteQuestionnaireFiles();
		deleteBrainstormingFiles();
	}
	
	/**
	 * 删除投票文件学员提交的冗余数据
	 */
	private void deleteVoteQuestionnaireFiles() {
		List<Map<String, Object>> list = tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.queryFileInfoByIdIsNull();
		if (list != null && list.size() > 0) {
			list.stream().forEach(item -> {
                if (item.get("url") != null) {
                	// 从磁盘上传也删除该文件
                    String deletePath = uploadPath + uploadPathUtils.getPathByParaNo("20") + "/" + item.get("url");
                    if (StrUtils.isNotEmpty(deletePath)) {
                    	new File(deletePath).delete();
                    }
                    // 删除冗余记录
                    tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.delete(item.get("file_id"));
                }
            });
		}
	}
	
	/**
	 * 删除头脑风暴学员提交的冗余数据
	 */
	private void deleteBrainstormingFiles() {
		List<Map<String,Object>> list = tevglActivityBrainstormingAnswerFileMapper.queryFileInfoByIdIsNull();
		if (list != null && list.size() > 0) {
			list.stream().forEach(item -> {
                if (item.get("url") != null) {
                	// 从磁盘上传也删除该文件
                    String deletePath = uploadPath + uploadPathUtils.getPathByParaNo("21") + "/" + item.get("url");
                    if (StrUtils.isNotEmpty(deletePath)) {
                    	new File(deletePath).delete();
                    }
                    // 删除冗余记录
                    tevglActivityBrainstormingAnswerFileMapper.delete(item.get("fi_id"));
                }
            });
		}
	}
	
}
