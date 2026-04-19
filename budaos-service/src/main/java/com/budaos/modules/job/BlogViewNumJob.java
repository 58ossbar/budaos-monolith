package com.budaos.modules.job;

import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;
import com.budaos.modules.evgl.forum.persistence.TevglForumBlogPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>每天凌晨1点执行一次：0 0 1 * * ?</b>
 * <b>更新整个更新浏览量50或100</b>
 * @author huj
 *
 */
@Component("blogViewNumJob")
public class BlogViewNumJob {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TevglForumBlogPostMapper tevglForumBlogPostMapper;
	
	public void doJob() {
		log.debug("执行无参方法，更新博客数量");
		Map<String, Object> map = new HashMap<>();
		// 随机本次更新数量
		List<String> list = tevglForumBlogPostMapper.selectIdListByMap(map);
		if (list != null && list.size() > 0) {
			/*map.put("postIdList", list);
			map.put("viewNum", 56);
			tevglForumBlogPostMapper.plusNumBatch(map);*/
			TevglForumBlogPost t = new TevglForumBlogPost();
			list.stream().forEach(postId -> {
				t.setPostId(postId);
				t.setViewNum(getRandomNum());
				tevglForumBlogPostMapper.plusNum(t);
			});
		}
	}
	
	public void doJob(String tempParams) {
		log.debug("执行有参方法，更新博客数量");
		Map<String, Object> map = new HashMap<>();
		map.put("state", "Y");
		// 随机本次更新数量
		List<String> list = tevglForumBlogPostMapper.selectIdListByMap(map);
		if (list.size() > 0) {
			/*map.put("postIdList", list);
			map.put("viewNum", 56);
			tevglForumBlogPostMapper.plusNumBatch(map);*/
			TevglForumBlogPost t = new TevglForumBlogPost();
			list.stream().forEach(postId -> {
				t.setPostId(postId);
				t.setViewNum(getRandomNum());
				tevglForumBlogPostMapper.plusNum(t);
			});
		}
	}
	
	private int getRandomNum() {
		int min = 1;
		int max = 100;
		int num = min + (int)(Math.random() * (max - min + 1));
		return num;
	}
}
