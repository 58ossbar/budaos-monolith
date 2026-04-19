package com.budaos.modules.evgl.pkg.persistence;

import com.budaos.core.baseclass.persistence.BaseSqlMapper;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.vo.TevglPkgInfoMgrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 教学包信息</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Mapper
public interface TevglPkgInfoMapper extends BaseSqlMapper<TevglPkgInfo> {
	/**
	 * <p>获取排序号</p>  
	 * @author znn
	 * @data 2020年1月16日	
	 * @param map
	 * @return
	 */
	int getMaxSortNum(Map<String, Object> map);
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return 
	 */
	List<Map<String, Object>> selectListMapByMap(Map<String, Object> map);
	
	/**
	 * 根据条件查询记录，返回List<Map>
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectListMapByMapUnionAll(Map<String, Object> map);
	
	List<Map<String, Object>> selectListMapByMapFromCtRoom(Map<String, Object> map);
	
	/**
	 * 根据条件查询，返回字段不够的话，自行补充。（返回对象驼峰命名）
	 * @author huj
	 * @data 2019年7月22日
	 * @param map
	 * @return
	 * @apiNote 注意：此方法不会查询字段display为3的数据。（display为3标识所有人都不可见，为系统创建）
	 */
	List<Map<String, Object>> selectSimpleList(Map<String, Object> map);
	
	/**
	 * 根据条件查询，返回字段不够的话，自行补充。（返回对象驼峰命名）
	 * @param map
	 * @return pkgId、refPkgId、pkgName、subjectId、pkgVersion、pkgLogo、createTime、updateTime
	 */
	List<Map<String, Object>> selectListByMapForSimple(Map<String, Object> map);
	
	/**
	 * 更新数量
	 * @param tevglPkgInfo
	 */
	void plusNum(TevglPkgInfo tevglPkgInfo);
	
	/**
	 * 查询对象
	 * @param pkgId
	 * @return
	 * @apiNote 查询返回了如下字段：pkgId教学包主键，orgId所属机构，pkgName教学包名称，pkgNo教学包编号，
	 * subjectId课程主键，pkgKey关键字，pkgDesc简介，pkglevel适用层次，pkgLimit适用限制，
	 * deployMainType发布方大类，deploySubType发布方小类，pkgRefCount引用数，pkgResCount资源数，
	 * pkgActCount活动数，pkgVersion版本号，pkgLogo教学包封面，pkgTraineeName联系人，pkgTraineeQq，
	 * pkgTraineeMobile，pkgTraineeWx，pkgTraineeEmail，viewNum查阅数，state状态，createUserId创建者，
	 * classScore学分
	 */
	Map<String, Object> selectObjectMapById(Object pkgId);
	
	/**
	 * 统计这个人的资源数
	 * @param createUserId
	 * @return
	 */
	Integer countPkgResCount(String createUserId);
	
	/**
	 * 统计这个人的活动数
	 * @param createUserId
	 * @return
	 */
	Integer countPkgActCount(String createUserId);
	
	/**
	 * 统计，当前有多少人使用了这个教学包，（人数去重，且包括自己）
	 * 这里的使用：指的是，课堂使用采用了这个教学包
	 * @param pkgId
	 * @return
	 */
	List<Map<String, Object>> selectHowManyPeopleUseIt(Object pkgId);
	
	/**
	 * 统计
	 * @param params
	 * @return 必传参数名together、createUserId、loginUserId
	 */
	List<Map<String, Object>> selectSubjectRefList(Map<String, Object> params);
	
	/**
	 * 根据条件查询记录
	 * @param map
	 * @return
	 */
	List<TevglPkgInfo> selectListByMapInnerJoin(Map<String, Object> map);
	
	/**
	 * 根据条件查询课堂所引用的教学包
	 * @param map
	 * @return
	 */
	List<String> queryCtRoomUsedRefPkgId(Map<String, Object> map);
	
	/**
	 * 查询当前课堂正在使用的教学包
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryCtRoomUsedRefPkgIdV2(Map<String, Object> map);
	
	/**
	 * 注意：会查询自己的，衍生版本，以及别人创建的免费的，以及被授权的
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryPkgListByUnionAllForSelect(Map<String, Object> map);
	
	/**
	 * 统计对应浏览量
	 * @param refPkgId
	 * @return
	 */
	Integer countSubjectViewNum(Object refPkgId);
	
	List<Map<String, Object>> countSubjectViewNumMap(Map<String, Object> map);
	
	/**
	 * 后台管理系统，移交教学包时，教学包列表数据
	 * @param map
	 * @return
	 */
	List<TevglPkgInfoMgrVo> queryPackageForChange(Map<String, Object> map);
	
	/**
	 * 更新接收者
	 * @param tevglPkgInfo
	 * @return
	 */
	int updateReceiverUserId(TevglPkgInfo tevglPkgInfo);

	/**
	 * 批量更新 接管者、创建者
	 * @param list
	 * @return
	 */
	int batchUpdateReceiverUserIdByCaseWhen(List<TevglPkgInfo> list);
	
	/**
	 * 找到目标教学包最新的子教学包
	 * @param pkgId
	 * @return
	 */
	String findTheLatestPackageLogo(String pkgId);
	
	List<String> findPkgIdListByRefPkgId(@Param("refPkgId") String refPkgId);
	
}