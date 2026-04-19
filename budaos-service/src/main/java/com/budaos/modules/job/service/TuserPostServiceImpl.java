package com.budaos.modules.job.service;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.job.persistence.TuserPostMapper;
import com.budaos.modules.sys.api.TuserPostService;
import com.budaos.modules.sys.domain.TuserPost;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Service
public class TuserPostServiceImpl implements TuserPostService {

	@Autowired
	private TuserPostMapper tuserPostMapper;
	
	/**
	 * <p>查询列表</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param map
	 * @return
	 */
	@Override
	public List<TuserPost> selectListByMap(Map<String, Object> map) {
		return tuserPostMapper.selectListByMap(map);
	}

	/**
	 * <p>查询明细</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param id
	 * @return
	 */
	@Override
	public TuserPost selectObjectById(String id) {
		return tuserPostMapper.selectObjectById(id);
	}

	/**
	 * <p>保存用户与岗位之间的关系</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param userId
	 * @param postIds
	 * @return
	 */
	@Override
	public R saveOrUpdate(String userId, List<String> postIds) {
		if(postIds.size() == 0){
			return R.ok();
		}
		tuserPostMapper.delete(userId);
		
		for(String postId : postIds){
			if(!postId.trim().isEmpty()){
				TuserPost post = new TuserPost(Identities.uuid());
				post.setPostId(postId);
				post.setUserId(userId);
				post.setIsMain("0");
				tuserPostMapper.insert(post);
			}
		}
		return R.ok();
	}

	/**
	 * <p>修改</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param tuserPost
	 * @return
	 */
	@Override
	public R update(TuserPost tuserPost) {
		try {
			tuserPostMapper.update(tuserPost);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("修改失败");
		}
		return R.ok("修改成功");
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
		try {
			tuserPostMapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.ok("删除成功");
	}

	/**
	 * <p>批量删除</p>
	 * @author huj
	 * @data 2019年5月29日
	 * @param ids
	 * @return
	 */
	@Override
	public R deleteBatch(@RequestBody String[] ids) {
		try {
			tuserPostMapper.deleteBatch(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
		return R.ok("删除成功");
	}
	
    /**
     * 根据用户id，查询岗位id
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> selectPostIdListByUserId(String userId) {
        return tuserPostMapper.selectPostIdListByUserId(userId);
    }

}
