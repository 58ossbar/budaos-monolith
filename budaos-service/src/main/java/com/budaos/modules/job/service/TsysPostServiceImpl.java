package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TsysPostMapper;
import com.budaos.modules.job.persistence.TuserPostMapper;
import com.budaos.modules.sys.api.TsysPostService;
import com.budaos.modules.sys.domain.TsysPost;
import com.budaos.modules.sys.domain.TsysUserinfo;
import com.budaos.modules.sys.domain.TuserPost;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>岗位管理api实现类</p>
 * <p>Title: TsysPostServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: 湖南创蓝信息科技有限公司</p> 
 * @author huj
 * @date 2019年5月29日
 */
@Service
public class TsysPostServiceImpl implements TsysPostService{

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private TsysPostMapper tsysPostMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil loginUtils;
	@Autowired
	private TuserPostMapper tuserPostMapper;
	
	/**
	 * <p>查询列表(返回List<Bean>)</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@Override
	public R query( Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TsysPost> tsysPostList = tsysPostMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tsysPostList, "createUserId");
		convertUtil.convertUserId2RealName(tsysPostList, "updateUserId");
		convertUtil.convertParam(tsysPostList, "postType", "postType");
		PageUtils pageUtil = new PageUtils(tsysPostList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}

	/**
	 * <p>查询列表(返回List<Map<String, Object>)</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@Override
	public R queryForMap( Map<String, Object> params) {
		return R.ok("该方法未实现");
	}
	
	/**
	 * <p>无分页查询</p>
	 * @author huj
	 * @data 2019年6月13日
	 * @param params
	 * @return
	 */
	@Override
	public R queryNoPage( Map<String, Object> params) {
		params.put("parentPostid", "");
		Query query = new Query(params);
		// 查出所有顶级数据
		List<TsysPost> list = tsysPostMapper.selectListByParentPostidIsNull(query);
		build(list);
		convertUtil.convertUserId2RealName(list, "createUserId");
		convertUtil.convertUserId2RealName(list, "updateUserId");
		return R.ok().put(Constant.R_DATA, list);
	}
	
	public List<TsysPost> build(List<TsysPost> list){
		for (TsysPost post : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("parentPostid", post.getPostId()); 
			List<TsysPost> childrenList = tsysPostMapper.selectListByMap(map);
			for (TsysPost tsysPost : childrenList) {
				tsysPost.setParentName(post.getPostName());
			}
			post.setChildren(childrenList);
			if (childrenList.size() > 0) {
				build(childrenList);
			}
		}
		return list;
	}

	/**
	 * <p>查询明细</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@Override
	public R selectObjectById( String id) {
		if (id == null || "".equals(id)) {
			return R.error("无法获取id");
		}
		TsysPost tsysPost = tsysPostMapper.selectObjectById(id);
		if (tsysPost == null) {
			return R.error("无法获取具体信息");
		}
		return R.ok().put(Constant.R_DATA, tsysPost);
	}

	/**
	 * <p>新增</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tsysPost
	 * @return
	 */
	@Override
	public R save(TsysPost tsysPost) {
		try {
			if (tsysPost.getPostId() != null && !"".equals(tsysPost.getPostId())) {
				return R.error("非新增操作");
			}
			// 排序号唯一验证
			List<TsysPost> list = tsysPostMapper.selectListByMap(new HashMap<String, Object>());
			if (list.size() > 0 && list != null) {
				for (TsysPost post : list) {
					if (tsysPost.getSort().equals(post.getSort())) {
						return R.error("排序号需唯一");
					}
				}
			}
			tsysPost.setPostId(Identities.uuid());
			tsysPost.setCreateUserId(loginUtils.getLoginUserId());
			tsysPost.setCreateTime(DateUtils.getNowTimeStamp());
			tsysPostMapper.insert(tsysPost);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("新增岗位失败");
		}
		return R.ok("新增岗位成功");
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tsysPost
	 * @return
	 */
	@Override
	public R update( TsysPost tsysPost) {
		try {
			TsysUserinfo userInfo = loginUtils.getLoginUser();
			if(userInfo == null){
				return R.error("无法获取登陆信息");
			}
			if (tsysPost.getPostId() == null || "".equals(tsysPost.getPostId())) {
				return R.error("非修改操作");
			}
			// 排序号操作
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sort", tsysPost.getSort()); // 先获取已经存在的排序号
			List<TsysPost> list = tsysPostMapper.selectListByMap(map);
			if (list.size() > 0 && list != null) {
				TsysPost post = tsysPostMapper.selectObjectById(tsysPost.getPostId());
				list.get(0).setSort(post.getSort()); // 将修改的这个岗位的排序，赋值给被修改的岗位排序号
				tsysPostMapper.update(list.get(0));
			}
			tsysPost.setUpdateUserId(userInfo.getUserId()); // 修改人
			tsysPost.setUpdateTime(DateUtils.getNowTimeStamp()); // 创建时间
			tsysPostMapper.update(tsysPost);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改岗位失败");
		}
		return R.ok("修改岗位成功");
	}

	/**
	 * <p>删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@Override
	public R delete(String id) {
		tsysPostMapper.delete(id);
		return R.ok("删除岗位成功");
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param ids
	 * @return
	 */
	@Override
	public R deleteBatch( String[] ids) {
		List<TuserPost> list = tuserPostMapper.selectListMapByMap(ids);
		if (list.size() > 0 && list != null) {
			convertUtil.convertUserId2RealName(list, "userId");
			String msg = "";
			for (TuserPost tuserPost : list) {
				msg += "【"+tuserPost.getUserId()+"】" + "任职了" + "【" + tuserPost.getPostName() + "】，";
			}
			return R.error("删除失败，有用户" + msg.substring(0, msg.length()-1)+"。请先移除更换用户的岗位");
		}
		tsysPostMapper.deleteBatch(ids);
		return R.ok("删除岗位成功").put("data", list);
	}

	/**
	 * <p></p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param params
	 * @return
	 */
	@Override
	public List<TsysPost> selectListByMap(Map<String, Object> params) {
		Query query = new Query(params);
		return tsysPostMapper.selectListByMap(query);
	}

	/**
	 * <p>查询岗位parentPostid 为 "" 的数据</p>
	 * @author huj
	 * @data 2019年6月14日
	 * @param map
	 * @return
	 */
	@Override
	public List<TsysPost> selectListByParentPostidIsNull( Map<String, Object> map) {
		return tsysPostMapper.selectListByParentPostidIsNull(map);
	}

	/**
	 * <p>更新排序号</p>
	 * @author huj
	 * @data 2019年6月18日
	 * @param params
	 * @return
	 */
	@Override
	public R updateSort( Map<String, Object> params) {
		String currPostId = (String) params.get("currPostId"); // 当前岗位ID
		String targetPostId = (String) params.get("targetPostId"); // 目标岗位ID
		String currSort = (String) params.get("currSort"); // 当前排序号
		String targetSort = (String) params.get("targetSort"); // 目标排序号
		if (currPostId != null && !"".equals(currPostId) && targetPostId != null && !"".equals(targetPostId)) {
			Map<String, Object> map = new HashMap<>();
			if (currSort != null && !"".equals(currSort) && targetSort != null && !"".equals(targetSort)) {
				map.put("postId", currPostId);
				map.put("sort", targetSort);
				tsysPostMapper.update(map); // 更新
				map.clear();
				map.put("postId", targetPostId);
				map.put("sort", currSort);
				tsysPostMapper.update(map); // 更新
			} else {
				TsysPost currPost = tsysPostMapper.selectObjectById(currPostId);
				TsysPost targetPost = tsysPostMapper.selectObjectById(targetPostId);
				Integer cSort = currPost.getSort();
				Integer tSort = targetPost.getSort();
				currPost.setSort(tSort);
				targetPost.setSort(cSort);
				tsysPostMapper.updateSort(currPost);
				tsysPostMapper.updateSort(targetPost);
			}
		}
		return R.ok();
	}
	
	/**
	 * <p>获取最大的排序号</p>
	 * @author huj
	 * @data 2019年6月24日
	 * @return
	 */
	public int getMaxSort() {
		return tsysPostMapper.getMaxSort();
	}
}
