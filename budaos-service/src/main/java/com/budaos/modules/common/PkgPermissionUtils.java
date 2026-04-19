package com.budaos.modules.common;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeam;
import com.budaos.modules.evgl.pkg.domain.TevglBookpkgTeamDetail;
import com.budaos.modules.evgl.pkg.domain.TevglPkgInfo;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamDetailMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglBookpkgTeamMapper;
import com.budaos.modules.evgl.pkg.persistence.TevglPkgInfoMapper;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RefreshScope
public class PkgPermissionUtils {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TevglPkgInfoMapper tevglPkgInfoMapper;
	@Autowired
	private TevglBookpkgTeamMapper tevglBookpkgTeamMapper;
	@Autowired
	private TevglBookpkgTeamDetailMapper tevglBookpkgTeamDetailMapper;
	
	/**
	 * 当前登录用户不是此教学包的创建者时校验权限（只会校验主表）
	 * 主表t_evgl_bookpkg_team没有此人此教学包的记录，认为没有权限
	 * @param pkgId 必传参数，当前被校验的教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @param pkgCreateUserId 必传参数，当前被校验的教学包
	 * @return {@link Boolean} 返回true，表示有权限
	 */
	public boolean hasPermission(String pkgId, String loginUserId, String pkgCreateUserId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(pkgCreateUserId) || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		// 当前登录用户不是此教学包的创建者时校验
		if (!loginUserId.equals(pkgCreateUserId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pkgId", pkgId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(map);
			if (teamList == null || teamList.size() == 0) {
				log.debug("用户["+loginUserId+"]主表没有教学包["+pkgId+"]的记录");
				return false;
			}	
		}
		return true;
	}
	
	/**
	 * 更换教学包时的校验，返回true才能更换课堂所使用的教学包
	 * @param tevglTchClassroom
	 * @param targetTevglPkgInfo
	 * @param loginUserId
	 * @return
	 */
	public boolean hasPermissionForChangePackageVersion(TevglTchClassroom tevglTchClassroom, TevglPkgInfo targetTevglPkgInfo, String loginUserId) {
		if (tevglTchClassroom == null || targetTevglPkgInfo == null || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		// 如果是免费的
		if (targetTevglPkgInfo.getPkgLimit().equals("2")) {
			return true;
		}
		// 如果教学包没有被移交
		if (StrUtils.isEmpty(targetTevglPkgInfo.getReceiverUserId())) {
			// 不是创建者的话
			if (!loginUserId.equals(targetTevglPkgInfo.getCreateUserId())) {
				String pkgId = targetTevglPkgInfo.getPkgId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pkgId", pkgId);
				map.put("userId", loginUserId);
				List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(map);
				if (teamList == null || teamList.size() == 0) {
					log.debug("用户["+loginUserId+"]主表没有教学包["+pkgId+"]的记录，判定为没有权限，无法使用该教学包");
					return false;
				}
			}
		}
		if (StrUtils.isNotEmpty(targetTevglPkgInfo.getReceiverUserId())) {
			if (!loginUserId.equals(targetTevglPkgInfo.getReceiverUserId())) {
				String pkgId = targetTevglPkgInfo.getPkgId();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pkgId", pkgId);
				map.put("userId", loginUserId);
				List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(map);
				if (teamList == null || teamList.size() == 0) {
					log.debug("用户["+loginUserId+"]主表没有教学包["+pkgId+"]的记录，判定为没有权限，无法使用该教学包");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * <b>此方法已过时，详见hasPermissionAv2()</b><br>
	 * <b>验证当前登录用户，是否能此教学包下，新增修改删除活动操作</b><br>
	 * <b>兼容2种身份，教学包创建者、教学包共建者</b><br>
	 * @param pkgId 必传参数，被校验的教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @param chapterId 选传参数，如果传了，则表示共建者需要该章节有权限才能进行相关操作
	 * @return
	 */
	@Deprecated
	public boolean hasPermissionA(String pkgId, String loginUserId, String chapterId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			log.debug("["+pkgId+"]不存在记录");
			return false;
		}
		// 当前登录用户不是此教学包的创建者时校验
		if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			return doFun(pkgId, loginUserId, chapterId);
		}
		return true;
	}
	
	/**
	 * <b>验证当前登录用户，是否能此教学包下，新增修改删除活动操作</b><br>
	 * <b>hasPermissionA()方法的升级版本</b><br>
	 * <b>兼容3种身份，教学包创建者、教学包共建者、教学包接管者</b><br>
	 * @param pkgId 必传参数，被校验的教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @param chapterId 选传参数，如果传了，则表示共建者需要该章节有权限才能进行相关操作
	 * @return
	 */
	public boolean hasPermissionAv2(String pkgId, String loginUserId, String chapterId) {
		if (StrUtils.isEmpty(pkgId) || StrUtils.isEmpty(loginUserId)) {
			return false;
		}
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			log.debug("["+pkgId+"]不存在记录");
			return false;
		}
		// 如果教学包没有被移交
		if (StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId())) {
			// 当前登录用户不是此教学包的创建者时校验
			if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
				// 否则判断登录用户是否被授权了
				return doFun(pkgId, loginUserId, chapterId);
			}
		}
		// 如果教学包被移交了
		if (StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId())) {
			// 如果当前用户是接管者，直接提前返回，拥有权限
			if (loginUserId.equals(tevglPkgInfo.getReceiverUserId())) {
				return true;
			} else {
				// 否则判断登录用户是否被授权了
				return doFun(pkgId, loginUserId, chapterId);
			}
		}
		return true;
	}

	private boolean doFun(String pkgId, String loginUserId, String chapterId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkgId", pkgId);
		map.put("userId", loginUserId);
		List<TevglBookpkgTeam> teamList = tevglBookpkgTeamMapper.selectListByMap(map);
		if (teamList == null || teamList.size() == 0) {
			log.debug("用户["+loginUserId+"]主表没有教学包["+pkgId+"]的记录");
			return false;
		}
		if (StrUtils.isNotEmpty(chapterId)) {
			map.clear();
			map.put("chapterId", chapterId);
			map.put("teamIds", teamList.stream().map(a -> a.getTeamId()).collect(Collectors.toList()));
			List<TevglBookpkgTeamDetail> detailList = tevglBookpkgTeamDetailMapper.selectListByMap(map);
			if (detailList == null || detailList.size() == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证当前登录用户是否拥有此教学包权限且是否有章节权限
	 * @param pkgId 必传参数，当前被校验的教学包
	 * @param loginUserId 必传参数，当前登录用户
	 * @param chapterId 必传参数，当前被校验的章节
	 * @return
	 */
	@Deprecated
	public R hasPermissionChapter(String pkgId, String loginUserId, Object chapterId) {
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			return R.error("无效的记录");
		}
		if (!loginUserId.equals(tevglPkgInfo.getCreateUserId())) {
			if (StrUtils.isEmpty(loginUserId) || StrUtils.isNull(chapterId)) {
				return R.error("没有权限");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("pgkId", pkgId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
			if (list == null || list.size() == 0) {
				log.debug("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "主表没有权限记录");
				return R.error("暂无权限，操作失败");
			}
			// 本章节是否有权限
			String teamId = list.get(0).getTeamId();
			map.clear();
			map.put("teamId", teamId);
			map.put("chapterId", chapterId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeamDetail> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(map);
			if (tevglBookpkgTeamDetailList == null || tevglBookpkgTeamDetailList.size() == 0) {
				log.debug("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "章节["+chapterId+"] 没有权限");
				return R.error("暂无权限，操作失败");
			}
		}
		return R.ok();
	}
	
	/**
	 * 验证当前登录用户是否拥有此教学包权限且是否有章节权限
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @param chapterId
	 * @return
	 */
	public R hasPermissionChapterV2(TevglPkgInfo tevglPkgInfo, String loginUserId, Object chapterId) {
		return doHasPermissionChapterV2(tevglPkgInfo, loginUserId, chapterId);
	}
	
	/**
	 * 验证当前登录用户是否拥有此教学包权限且是否有章节权限
	 * @param pkgId
	 * @param loginUserId
	 * @param chapterId
	 * @return
	 */
	public R hasPermissionChapterV2(String pkgId, String loginUserId, Object chapterId) {
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		return doHasPermissionChapterV2(tevglPkgInfo, loginUserId, chapterId);
	}
	
	/**
	 * 【实际处理】验证当前登录用户是否拥有此教学包权限且是否有章节权限
	 * @param tevglPkgInfo
	 * @param loginUserId
	 * @param chapterId
	 * @return
	 */
	private R doHasPermissionChapterV2(TevglPkgInfo tevglPkgInfo, String loginUserId, Object chapterId) {
		if (tevglPkgInfo == null || StrUtils.isEmpty(loginUserId) || StrUtils.isNull(chapterId)) {
			return R.error("没有权限");
		}
		String pkgId = tevglPkgInfo.getPkgId();
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		if (!isCreator && !isReceiver) {
			Map<String, Object> map = new HashMap<>();
			map.put("pgkId", pkgId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeam> list = tevglBookpkgTeamMapper.selectListByMap(map);
			if (list == null || list.size() == 0) {
				log.error("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "主表没有权限记录");
				return R.error("暂无权限，操作失败");
			}
			// 本章节是否有权限
			String teamId = list.get(0).getTeamId();
			map.clear();
			map.put("teamId", teamId);
			map.put("chapterId", chapterId);
			map.put("userId", loginUserId);
			List<TevglBookpkgTeamDetail> tevglBookpkgTeamDetailList = tevglBookpkgTeamDetailMapper.selectListByMap(map);
			if (tevglBookpkgTeamDetailList == null || tevglBookpkgTeamDetailList.size() == 0) {
				log.error("教学包["+pkgId+"]中，" + "用户["+loginUserId+"]，" + "章节["+chapterId+"] 没有权限");
				return R.error("暂无权限，操作失败");
			}
		}
		return R.ok();
	}
	
	/**
	 * 验证是否有删除活动的权限，
	 * 1.教学包创建者可以删除任意活动
	 * 2.共建者只可以删除自己创建的活动，和被授权的章节对应的活动
	 * @param pkgId 活动对应的教学包
	 * @param activityCreateUserId 活动d的创建者create_user_id字段的值
	 * @param loginUserId 当前登录用户
	 * @param chapterId 活动对应的章节
	 * @return
	 */
	public boolean hasPermissionDeleteActivity(String pkgId, String activityCreateUserId, String loginUserId, String chapterId) {
		TevglPkgInfo tevglPkgInfo = tevglPkgInfoMapper.selectObjectById(pkgId);
		if (tevglPkgInfo == null) {
			log.debug("无效的教学包["+pkgId+"],无法进行下一步操作");
			return false;
		}
		// 如果当前登录用户为教学包创建者
		boolean isCreator = StrUtils.isEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getCreateUserId());
		boolean isReceiver = StrUtils.isNotEmpty(tevglPkgInfo.getReceiverUserId()) && loginUserId.equals(tevglPkgInfo.getReceiverUserId());
		if (isCreator || isReceiver) {
			return true;
		} else {
			// 如果此活动是当前共建者创建
			if (loginUserId.equals(activityCreateUserId)) {
				return true;
			} else {
				// 否则判断此章节是否权限
				//R r = hasPermissionChapter(pkgId, loginUserId, chapterId);
				R r = hasPermissionChapterV2(tevglPkgInfo, loginUserId, chapterId);
				if (!r.get("code").equals(0)) {
					log.debug(r.toString());
					return false;
				}
			}
		}
		return false;
	}
	
}
