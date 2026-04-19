package com.budaos.modules.evgl.pkg.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.CloudPanUtils;
import com.budaos.modules.common.GlobalActivity;
import com.budaos.modules.common.PkgUtils;
import com.budaos.modules.common.UploadPathUtils;
import com.budaos.modules.evgl.activity.domain.TevglActivityBrainstormingAnswerFile;
import com.budaos.modules.evgl.activity.domain.TevglActivityVoteQuestionnaireTraineeAnswerFile;
import com.budaos.modules.evgl.activity.persistence.*;
import com.budaos.modules.evgl.book.persistence.TevglBookChapterMapper;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanDirectory;
import com.budaos.modules.evgl.cloudpan.domain.TcloudPanFile;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanDirectoryMapper;
import com.budaos.modules.evgl.cloudpan.persistence.TcloudPanFileMapper;
import com.budaos.modules.evgl.cloudpan.service.TcloudPanDirectoryServiceImpl;
import com.budaos.modules.evgl.pkg.api.DeletePackageService;
import com.budaos.modules.evgl.pkg.domain.TevglPkgActivityRelation;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.*;
import com.budaos.modules.evgl.question.domain.*;
import com.budaos.modules.evgl.question.persistence.*;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 删除教学包 专用接口
 * @author huj
 *
 */
@Service
public class DeletePackageServiceImpl implements DeletePackageService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	@Autowired
	private TevglPkgActivityRelationMapper tevglPkgActivityRelationMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireMapper tevglActivityVoteQuestionnaireMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionMapper tevglActivityVoteQuestionnaireQuestionMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireQuestionOptionMapper tevglActivityVoteQuestionnaireQuestionOptionMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerMapper tevglActivityVoteQuestionnaireTraineeAnswerMapper;
	@Autowired
	private TevglActivityVoteQuestionnaireTraineeAnswerFileMapper tevglActivityVoteQuestionnaireTraineeAnswerFileMapper;
	@Autowired
	private TevglActivityBrainstormingMapper tevglActivityBrainstormingMapper;
	@Autowired
	private TevglActivityBrainstormingTraineeAnswerMapper tevglActivityBrainstormingTraineeAnswerMapper;
	@Autowired
	private TevglActivityBrainstormingAnswerFileMapper tevglActivityBrainstormingAnswerFileMapper;
	@Autowired
	private TevglActivityAnswerDiscussMapper tevglActivityAnswerDiscussMapper;
	@Autowired
	private TevglPkgResgroupMapper tevglPkgResgroupMapper;
	@Autowired
	private TevglPkgResMapper tevglPkgResMapper;
	@Autowired
	private TevglBookChapterMapper tevglBookChapterMapper;
	@Autowired
	private TcloudPanDirectoryMapper tcloudPanDirectoryMapper;
	@Autowired
	private TcloudPanFileMapper tcloudPanFileMapper;
	@Autowired
	private TcloudPanDirectoryServiceImpl tcloudPanDirectoryServiceImpl;
	@Autowired
	private TepExamineDynamicPaperMapper tepExamineDynamicPaperMapper;
	@Autowired
	private TepExamineHistoryPaperMapper tepExamineHistoryPaperMapper;
	@Autowired
	private TepExaminePaperInfoMapper tepExaminePaperInfoMapper;
	@Autowired
	private TepExaminePaperQuestionsDetailMapper tepExaminePaperQuestionsDetailMapper;
	@Autowired
	private TepExaminePaperQuestionsRandomMapper tepExaminePaperQuestionsRandomMapper;
	@Autowired
	private TepExaminePaperQuestionsOptionRandomMapper tepExaminePaperQuestionsOptionRandomMapper;
	@Autowired
	private TepExaminePaperScoreMapper tepExaminePaperScoreMapper;
	@Autowired
	private TepExaminePaperScoreGapfillingMapper tepExaminePaperScoreGapfillingMapper;
	
	@Autowired
	private PkgUtils pkgUtils;
	@Autowired
	private CloudPanUtils cloudPanUtils;
	@Autowired
	private UploadPathUtils uploadPathUtils;
	
	/**
	 * 物理删除
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 */
	@Override
	@Transactional
	@CacheEvict(value = "room_book", allEntries = true)
	public R deletePkgInfo(String pkgId, String loginUserId) {
		// 处理需求变更引起的旧数据问题（专升本的大学英语等）
		if ("c0ea18bb858547a39283053169c03270".equals(pkgId)) {
			return R.error("抱歉，这个教学包不允许删除");
		}
		// 合法性校验
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("删除失败");
		}
		// 没有权限，无法操作教学包
		boolean hasOperatingAuthorization = pkgUtils.hasOperatingAuthorization(tevglPkgInfo, loginUserId);
		if (!hasOperatingAuthorization) {
			return R.error("没有权限，无法操作教学包");
		}
		Integer it = pkgUtils.countHowManyPeopleUseIt(pkgId);
		if (it != null && it > 0) {
			return R.error("当前有"+it+"人正在使用此教学包，暂不允许删除");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		// 1.删除教学包共建权限的数据
		doDeleteTeamDatas(pkgId, params);
		// 2.删除活动基本数据、学生作答产生的数据、教学包与活动的关系
		doDeleteActivityDatas(pkgId, params);
		// 3.删除分组标签与资源
		doDeleteRegroupAndRes(pkgId, params);
		// 4.删除章节与教材基本信息
		doDeleteSubjectAndChapter(tevglPkgInfo.getSubjectId(), params);
		// 5.删除云盘数据
		//doDeleteCloudPanDatas(pkgId, loginUserId, params);
		doDeleteCloudPanDatasNew(pkgId, loginUserId, params);
		// 6.删除教学包基本数据
		tevglPkgInfoMapper.delete(pkgId);
		return R.ok("删除成功");
	}
	
	private void doDeleteCloudPanDatasNew(String pkgId, String loginUserId, Map<String, Object> params) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 删除教学包时，删除云盘相关数据中 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		// 额外情况，查询文件表，dir_id值等于教学包id的文件，这种是直接上传到根目录下（也就是教学包id下）
		params.clear();
		params.put("dirId", pkgId);
		List<TcloudPanFile> tcloudPanFileList = tcloudPanFileMapper.selectListByMap(params);
		if (tcloudPanFileList != null && tcloudPanFileList.size() > 0) {
			// 磁盘文件删除
			for (TcloudPanFile tcloudPanFile : tcloudPanFileList) {
				String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
				if (StrUtils.isNotEmpty(absolutePath)) {
					File file = new File(absolutePath);
					if (file.exists() && file.isFile()) {
						file.delete();
					}
				}
			}
			// 数据库记录删除
			List<String> fileIdList = tcloudPanFileList.stream().map(a -> a.getFileId()).collect(Collectors.toList());
			tcloudPanFileMapper.deleteBatch(fileIdList.stream().toArray(String[]::new));
		}
		// 查询当前被删教学包的所有目录
		params.clear();
		params.put("pkgId", pkgId);
		params.put("sidx", "dict_code");
		params.put("order", "asc, create_time desc");
		List<Map<String, Object>> dirList = tcloudPanDirectoryMapper.selectSimpleListMap(params);
		if (dirList != null && dirList.size() > 0) {
			// 根据目录id查询对应所有的文件
			List<String> dirIdList = dirList.stream().map(a -> a.get("dirId").toString()).collect(Collectors.toList());
			params.clear();
			params.put("dirIds", dirIdList);
			List<TcloudPanFile> fileList = tcloudPanFileMapper.selectListByMap(params);
			if (fileList != null && fileList.size() > 0) {
				// 先删除磁盘文件
				for (TcloudPanFile tcloudPanFile : fileList) {
					String absolutePath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getFileSavePath());
					log.debug("被删除文件的绝对路径：" + absolutePath);
					if (StrUtils.isNotEmpty(absolutePath)) {
						File file = new File(absolutePath);
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					// 处理删除pdf文件
					if (!StrUtils.isNull(tcloudPanFile.getFileSuffix())) {
						if (cloudPanUtils.canBePdfSuffixList.contains(tcloudPanFile.getFileSuffix())) {
							if (!StrUtils.isNull(tcloudPanFile.getPath())) {
								String pdfPath = cloudPanUtils.getFileSavePathByValue(pkgId, tcloudPanFile.getPath());
								File pdfFile = new File(pdfPath);
								if (pdfFile.exists()) {
									pdfFile.delete();
								}
							}
						}
					}
				}
				// 紧接删除数据库记录
				List<String> fileIdList = fileList.stream().map(a -> a.getFileId()).collect(Collectors.toList());
				tcloudPanFileMapper.deleteBatch(fileIdList.stream().toArray(String[]::new));
			}
			// 先获取目录树
			List<Map<String, Object>> result = build("0", dirList);
			for (Map<String, Object> map : result) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> children = (List<Map<String, Object>>)map.get("children");
				del(map, children, loginUserId);
			}
			// 再删除数据库中文件夹记录
			tcloudPanDirectoryMapper.deleteBatch(dirIdList.stream().toArray(String[]::new));
		}
		// 删除磁盘上教学包目录
		String uploadPath = cloudPanUtils.getUploadPath();
		String pkgDirUrl = uploadPath + "/" + pkgId;
		File f = new File(pkgDirUrl);
		if (f.exists() && f.isDirectory()) {
			f.delete();
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 删除教学包时，删除云盘相关数据中 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
		
	}
	
	private void del(Map<String, Object> info, List<Map<String, Object>> childrenList, String loginUserId) {
		if (childrenList != null && childrenList.size() > 0) {
			for (Map<String, Object> map : childrenList) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> children = (List<Map<String, Object>>)map.get("children");
				del(map, children, loginUserId);
				// 磁盘文件删除目录
				if (!StrUtils.isNull(map.get("name"))) {
					String pathBy = cloudPanUtils.getPathBy(map.get("dirId").toString(), loginUserId, map.get("pkgId").toString());
					File file = new File(pathBy);
					if (file.exists() && file.isDirectory()) {
						file.delete();
					}
				}
			}
		}
		// 磁盘文件删除目录
		if (!StrUtils.isNull(info.get("name"))) {
			String pathBy = cloudPanUtils.getPathBy(info.get("dirId").toString(), loginUserId, info.get("pkgId").toString());
			File file = new File(pathBy);
			if (file.exists() && file.isDirectory()) {
				file.delete();
			}
		}
	}
	
	private List<Map<String, Object>> build(String parentId, List<Map<String, Object>> allList) {
		if (allList == null || allList.size() == 0) {
			return null;
		}
		// 筛选出匹配的节点
		List<Map<String, Object>> nodeList = allList.stream().filter(a -> a.get("parentId").equals(parentId)).collect(Collectors.toList());
		if (nodeList != null && nodeList.size() > 0) {
			for (int i = 0; i < nodeList.size(); i++) {
				Map<String, Object> node = nodeList.get(i);
				// 递归
				List<Map<String, Object>> list = build(node.get("id").toString(), allList);
				if (list != null && list.size() > 0) {
					node.put("children", list);
				} else {
					node.put("children", null);
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * 删除云盘相关数据
	 * @param pkgId
	 * @param params
	 */
	@Deprecated
	private void doDeleteCloudPanDatas(String pkgId, String loginUserId, Map<String, Object> params) {
		try {
			log.debug("============================== 开始删除云盘相关数据中 begin ==============================");
			// 查询教学包的目录
			params.clear();
			params.put("pkgId", pkgId);
			List<TcloudPanDirectory> directoryList = tcloudPanDirectoryMapper.selectListByMap(params);
			if (directoryList != null && directoryList.size() > 0) {
				List<String> dirIds = directoryList.stream().map(a -> a.getDirId()).collect(Collectors.toList());
				params.put("dirIds", dirIds);
				List<TcloudPanFile> fileList = tcloudPanFileMapper.selectListByMap(params);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("pkgId", pkgId);
				JSONArray jsonArray = new JSONArray();
				directoryList.stream().forEach(directory -> {
					JSONObject o = new JSONObject();
					o.put("id", directory.getDirId());
					o.put("type", "1");
					jsonArray.add(o);
				});
				fileList.stream().forEach(fileInfo -> {
					JSONObject o = new JSONObject();
					o.put("id", fileInfo.getFileId());
					o.put("type", "2");
					jsonArray.add(o);
				});
				jsonObject.put("list", jsonArray);
				R r = tcloudPanDirectoryServiceImpl.deletesNew(jsonObject, loginUserId);
				log.debug("删除结果：" + r);
				// 磁盘上直接删除此教学包主目录
				File f = new File(cloudPanUtils.getUploadPath() + "/" + pkgId);
				if (f.exists() && f.isDirectory()) {
					log.debug("被删除的教学包主目录路径为：" + f);
					f.delete();
				}
			}
			log.debug("============================== 结束删除云盘相关数据中 end ==============================");
		} catch (Exception e) {
			log.debug("云盘数据删除失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 4.删除章节与教材基本信息（删除教学包对应的教材）
	 * @param subjectId pkgId表记录中对应的subjectId
	 * @param params
	 */
	private void doDeleteSubjectAndChapter(String subjectId, Map<String, Object> params) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 删除教学包的分组和资源相关数据中 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		params.clear();
		params.put("subjectId", subjectId);
		List<String> chapterIdList = tevglBookChapterMapper.selectChapterIdList(subjectId);
		if (chapterIdList != null && chapterIdList.size() > 0) {
			tevglBookChapterMapper.deleteBatch(chapterIdList.stream().toArray(String[]::new));
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 删除教学包的分组和资源相关数据中 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}
	
	/**
	 * 3.删除分组标签与资源
	 * @param pkgId
	 * @param params
	 */
	private void doDeleteRegroupAndRes(String pkgId, Map<String, Object> params) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 删除教学包的分组和资源相关数据中 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		try {
			params.clear();
			params.put("pkgId", pkgId);
			List<String> resgroupIdList = tevglPkgResgroupMapper.selectResgroupIdListByMap(params);
			if (resgroupIdList == null || resgroupIdList.size() == 0) {
				return;
			}
			params.clear();
			params.put("resgroupIds", resgroupIdList);
			List<String> resIdList = tevglPkgResMapper.selectResIdListByMap(params);
			if (resIdList != null && resIdList.size() > 0) {
				tevglPkgResMapper.deleteBatch(resIdList.stream().toArray(String[]::new));
			}
			log.error("删除教学包，删除分组和资源成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包，删除分组和资源失败", e);
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 删除教学包的分组和资源相关数据中 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}
	
	/**
	 *2. 删除活动基本数据、学生作答产生的数据、教学包与活动的关系
	 * @param pkgId
	 */
	private void doDeleteActivityDatas(String pkgId, Map<String, Object> params) {
		// 查出当前教学包活动
		params.clear();
		params.put("pkgId", pkgId);
		List<TevglPkgActivityRelation> tevglPkgActivityRelationList = tevglPkgActivityRelationMapper.selectListByMap(params);
		log.debug("当前教学包的活动：" + tevglPkgActivityRelationList.size());
		// 1.删除投票问卷相关数据
		toDeleteActivity1(tevglPkgActivityRelationList, params);
		// 2.删除头脑风暴相关数据
		toDeleteActivity2(tevglPkgActivityRelationList, params);
		// 3.删除答疑讨论相关数据
		toDeleteActivity3(tevglPkgActivityRelationList, params);
		// 4.删除测试活动相关数据
		toDeleteActivity4(pkgId, tevglPkgActivityRelationList, params);
		// 后续请在此在此累加
		// 最终删除教学包与活动的关系
		List<String> paIdList = tevglPkgActivityRelationList.stream().map(a -> a.getPaId()).collect(Collectors.toList());
		if (paIdList != null && paIdList.size() > 0) {
			tevglPkgActivityRelationMapper.deleteBatch(paIdList.stream().toArray(String[]::new));
		}
	}
	
	/**
	 * 删除投票问卷
	 * @param tevglPkgActivityRelationList 等待删除的活动
	 * @param params
	 * @apiNote 
	 *  1.删除学生作答产生的附件记录<br>
	 *  2.删除学生的作答数据<br>
	 *  3.删除问卷的选项，与题目<br>
	 *  4.最后再删除问卷基本信息
	 */
	private void toDeleteActivity1(List<TevglPkgActivityRelation> tevglPkgActivityRelationList, Map<String, Object> params) {
		try {
			// 如果没有活动直接返回
			if (tevglPkgActivityRelationList == null || tevglPkgActivityRelationList.size() == 0) {
				return;
			}
			// 取出投票问卷
			List<String> activityIdList = tevglPkgActivityRelationList.stream()
			.filter(a -> a.getActivityType().equals(GlobalActivity.ACTIVITY_1_VOTE_QUESTIONNAIRE))
			.map(a -> a.getActivityId())
			.distinct()
			.collect(Collectors.toList());
			// 如果没有投票问卷，直接返回
			if (activityIdList == null || activityIdList.size() == 0) {
				return;
			}
			// 查题目和题目选项
			params.clear();
			params.put("activityIds", activityIdList);
			List<String> questionIdList = tevglActivityVoteQuestionnaireQuestionMapper.selectQuestionIdListByMap(params);
			if (questionIdList != null && questionIdList.size() > 0) {
				params.clear();
				params.put("questionIds", questionIdList);
				List<String> optionIdList = tevglActivityVoteQuestionnaireQuestionOptionMapper.selectOptionIdListByMap(params);
				// 删除题目和选项
				if (optionIdList != null && optionIdList.size() > 0) {
					tevglActivityVoteQuestionnaireQuestionOptionMapper.deleteBatch(optionIdList.stream().toArray(String[]::new));
				}
				tevglActivityVoteQuestionnaireQuestionMapper.deleteBatch(optionIdList.stream().toArray(String[]::new));
			}
			// 查询作答和附件
			params.clear();
			params.put("activityIds", activityIdList);
			List<String> idList = tevglActivityVoteQuestionnaireTraineeAnswerMapper.selectIdListByMap(params);
			if (idList != null && idList.size() > 0) {
				params.put("ids", idList);
				List<TevglActivityVoteQuestionnaireTraineeAnswerFile> fileList = tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.selectListByMap(params);
				if (fileList != null && fileList.size() > 0) {
					// 磁盘上删除文件
					for (TevglActivityVoteQuestionnaireTraineeAnswerFile t : fileList) {
						String absolutePath = uploadPathUtils.getAbsolutePath(t.getUrl(), "20");
						File file = new File(absolutePath);
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					// 数据库中删除记录
					List<String> fileIdList = fileList.stream().map(a -> a.getFileId()).collect(Collectors.toList());
					tevglActivityVoteQuestionnaireTraineeAnswerFileMapper.deleteBatch(fileIdList.stream().toArray(String[]::new));
				}
				tevglActivityVoteQuestionnaireTraineeAnswerMapper.deleteBatch(idList.stream().toArray(String[]::new));
			}
			tevglActivityVoteQuestionnaireMapper.deleteBatch(activityIdList.stream().toArray(String[]::new));
			log.debug("成功删除【投票问卷】");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包时，删除活动时，删除【投票问卷】失败");
		}
	}
		
	/**
	 * 删除头脑风暴
	 * @param tevglPkgActivityRelationList
	 * @param params
	 */
	private void toDeleteActivity2(List<TevglPkgActivityRelation> tevglPkgActivityRelationList, Map<String, Object> params) {
		try {
			// 如果没有活动直接返回
			if (tevglPkgActivityRelationList == null || tevglPkgActivityRelationList.size() == 0) {
				return;
			}
			// 取出
			List<String> activityIdList = tevglPkgActivityRelationList.stream()
			.filter(a -> a.getActivityType().equals(GlobalActivity.ACTIVITY_2_BRAINSTORMING))
			.map(a -> a.getActivityId())
			.distinct()
			.collect(Collectors.toList());
			// 如果没有，直接返回
			if (activityIdList == null || activityIdList.size() == 0) {
				return;
			}
			// 查询作答和附件
			params.clear();
			params.put("activityIds", activityIdList);
			List<String> anIdList = tevglActivityBrainstormingTraineeAnswerMapper.selectAnIdListByMap(params);
			if (anIdList != null && anIdList.size() > 0) {
				params.clear();
				params.put("anIds", anIdList);
				List<TevglActivityBrainstormingAnswerFile> fileList = tevglActivityBrainstormingAnswerFileMapper.selectListByMap(params);
				if (fileList != null && fileList.size() > 0) {
					// 磁盘上删除文件
					for (TevglActivityBrainstormingAnswerFile t : fileList) {
						String absolutePath = uploadPathUtils.getAbsolutePath(t.getUrl(), "21");
						File file = new File(absolutePath);
						if (file.exists() && file.isFile()) {
							file.delete();
						}
					}
					// 数据库中删除记录
					List<String> fileIdList = fileList.stream().map(a -> a.getFiId()).collect(Collectors.toList());
					tevglActivityBrainstormingAnswerFileMapper.deleteBatch(fileIdList.stream().toArray(String[]::new));
				}
				tevglActivityBrainstormingTraineeAnswerMapper.deleteBatch(anIdList.stream().toArray(String[]::new));
			}
			tevglActivityBrainstormingMapper.deleteBatch(activityIdList.stream().toArray(String[]::new));
			log.debug("成功删除【头脑风暴】");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包时，删除活动时，删除【头脑风暴】失败");
		}
	}
	

	/**
	 * 删除答疑讨论
	 * @param tevglPkgActivityRelationList
	 * @param params
	 */
	private void toDeleteActivity3(List<TevglPkgActivityRelation> tevglPkgActivityRelationList, Map<String, Object> params) {
		try {
			// 如果没有活动直接返回
			if (tevglPkgActivityRelationList == null || tevglPkgActivityRelationList.size() == 0) {
				return;
			}
			// 取出
			List<String> activityIdList = tevglPkgActivityRelationList.stream()
			.filter(a -> a.getActivityType().equals(GlobalActivity.ACTIVITY_3_ANSWER_DISCUSS))
			.map(a -> a.getActivityId())
			.distinct()
			.collect(Collectors.toList());
			// 如果没有，直接返回
			if (activityIdList == null || activityIdList.size() == 0) {
				return;
			}
			tevglActivityAnswerDiscussMapper.deleteBatch(activityIdList.stream().toArray(String[]::new));
			log.debug("成功删除【答疑讨论】");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包时，删除活动时，删除【答疑讨论】失败");
		}
	}
	
	/**
	 * 删除测试活动
	 * @param tevglPkgActivityRelationList
	 * @param params
	 */
	private void toDeleteActivity4(String pkgId, List<TevglPkgActivityRelation> tevglPkgActivityRelationList, Map<String, Object> params) {
		try {
			// 如果没有活动直接返回
			if (tevglPkgActivityRelationList == null || tevglPkgActivityRelationList.size() == 0) {
				return;
			}
			// 取出
			List<String> activityIdList = tevglPkgActivityRelationList.stream()
			.filter(a -> a.getActivityType().equals(GlobalActivity.ACTIVITY_4_TEST_ACT))
			.map(a -> a.getActivityId())
			.distinct()
			.collect(Collectors.toList());
			// 如果没有，直接返回
			if (activityIdList == null || activityIdList.size() == 0) {
				return;
			}
			// 查动态问卷
			params.clear();
			params.put("pkgId", pkgId);
			params.put("paperIds", activityIdList);
			List<TepExamineDynamicPaper> list = tepExamineDynamicPaperMapper.selectListByMap(params);
			List<String> dyIdList = list.stream().map(a -> a.getDyId()).collect(Collectors.toList());
			if (dyIdList == null || dyIdList.size() == 0) {
				return;
			}
			params.clear();
			params.put("dyIds", dyIdList);
			List<TepExamineHistoryPaper> historyPaperList = tepExamineHistoryPaperMapper.selectListByMap(params);
			if (historyPaperList != null && historyPaperList.size() > 0) {
				List<String> historyIdList = historyPaperList.stream().map(a -> a.getHistoryId()).collect(Collectors.toList());
				tepExamineHistoryPaperMapper.deleteBatch(historyIdList.stream().toArray(String[]::new));
				// 查询试卷成绩表
				params.clear();
				params.put("historyIds", historyIdList);
				List<TepExaminePaperScore> paperScoreList = tepExaminePaperScoreMapper.selectListByMap(params);
				if (paperScoreList != null && paperScoreList.size() > 0) {
					List<String> scoreIdList  = paperScoreList.stream().map(a -> a.getScoreId()).collect(Collectors.toList());
					tepExamineHistoryPaperMapper.deleteBatch(scoreIdList.stream().toArray(String[]::new));	
				}
				// 查询
				List<TepExaminePaperScoreGapfilling> paperScoreGapfillingList = tepExaminePaperScoreGapfillingMapper.selectListByMap(params);
				if (paperScoreGapfillingList != null && paperScoreGapfillingList.size() > 0) {
					List<String> idList = paperScoreGapfillingList.stream().map(a -> a.getId()).collect(Collectors.toList());
					tepExaminePaperScoreGapfillingMapper.deleteBatch(idList.stream().toArray(String[]::new));	
				}
			}
			// 查询题目乱序表，并删除
			params.clear();
			params.put("dyIds", dyIdList);
			List<TepExaminePaperQuestionsRandom> questionsRandomList = tepExaminePaperQuestionsRandomMapper.selectListByMap(params);
			if (questionsRandomList != null && questionsRandomList.size() > 0) {
				List<String> rdIdList = questionsRandomList.stream().map(a -> a.getRdId()).collect(Collectors.toList());
				tepExaminePaperQuestionsRandomMapper.deleteBatch(rdIdList.stream().toArray(String[]::new));
			}
			// 查询题目选项乱序表，并删除
			params.clear();
			params.put("dyIds", dyIdList);
			List<TepExaminePaperQuestionsOptionRandom> questionsOptionRandomList = tepExaminePaperQuestionsOptionRandomMapper.selectListByMap(params);
			if (questionsOptionRandomList != null && questionsOptionRandomList.size() > 0) {
				List<String> rdIdList = questionsOptionRandomList.stream().map(a -> a.getRdId()).collect(Collectors.toList());
				tepExaminePaperQuestionsOptionRandomMapper.deleteBatch(rdIdList.stream().toArray(String[]::new));
			}
			// 删除试卷题目
			params.clear();
			params.put("paperIds", activityIdList);
			List<TepExaminePaperQuestionsDetail> paperQuestionsDetailList = tepExaminePaperQuestionsDetailMapper.selectListByMap(params);
			if (paperQuestionsDetailList != null && paperQuestionsDetailList.size() > 0) {
				List<String> detailIdList = paperQuestionsDetailList.stream().map(a -> a.getDetailId()).collect(Collectors.toList());
				tepExaminePaperQuestionsDetailMapper.deleteBatch(detailIdList.stream().toArray(String[]::new));
			}
			// 删除试卷
			tepExaminePaperInfoMapper.deleteBatch(activityIdList.stream().toArray(String[]::new));
			log.debug("成功删除【测试活动】");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包时，删除活动时，删除【测试活动】失败");
		}
		
	}
	
	/**
	 * 删除授权的数据，关注t_evgl_bookpkg_team、t_evgl_bookpkg_team_detail
	 * @param pkgId 当前被删除的教学包
	 * @param params 一个空map作为查询
	 */
	private void doDeleteTeamDatas(String pkgId, Map<String, Object> params) {
		log.debug("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 删除授权相关数据 begin ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
		try {
			params.clear();
			params.put("pkgId", pkgId);
			List<String> teamIdList = tevglBookpkgTeamMapper.selectTeamIdListByMap(params);
			if (teamIdList == null || teamIdList.size() == 0) {
				return;
			}
			params.clear();
			params.put("teamIds", teamIdList);
			List<String> detailIdList = tevglBookpkgTeamDetailMapper.selectDetailIdListByMap(params);
			if (detailIdList != null && detailIdList.size() > 0) {
				tevglBookpkgTeamDetailMapper.deleteBatch(detailIdList.stream().toArray(String[]::new));
			}
			tevglBookpkgTeamMapper.deleteBatch(teamIdList.stream().toArray(String[]::new));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除教学包时，删除授权相关数据失败", e);
		}
		log.debug("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 删除授权相关数据 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
	}
	
}
