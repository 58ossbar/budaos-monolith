package com.budaos.modules.job;

import com.budaos.modules.evgl.site.domain.TevglSiteNews;
import com.budaos.modules.evgl.site.persistence.TevglSiteNewsMapper;
import com.budaos.modules.evgl.site.vo.TevglSiteNewsVo;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号，资讯
 * @author huj
 *
 */
@Component("newsViewNumJob")
public class NewsViewNumJob {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private TevglSiteNewsMapper tevglSiteNewsMapper;

	public void plusNum() {
		doActualExecution();
	}

	public void plusNum(String params) {
		log.debug("形参 {}", params);
		doActualExecution();
	}

	private void doActualExecution() {
		log.debug("======================================== 更新新闻浏览量 begin ============================================================");
		Map<String, Object> map = new HashMap<>();
		map.put("status", "2");
		List<TevglSiteNewsVo> tevglSiteNewsVos = tevglSiteNewsMapper.selectListByMapForOfficial(map);
		if (tevglSiteNewsVos != null && tevglSiteNewsVos.size() > 0) {
			TevglSiteNews t = new TevglSiteNews();
			tevglSiteNewsVos.stream().forEach(item -> {
				t.setNewsid(item.getNewsid());
				t.setViewNum(RandomUtils.nextInt(1, 100));
				tevglSiteNewsMapper.plusNum(t);
			});
		}
		log.debug("======================================== 更新新闻浏览量 end ============================================================");
	}
	
	/**
	 * 定时更新微信公众号那边的浏览量
	 */
	public void updateWeChatOfficialAccountNewsViewNum() {
		Map<String, Object> map = new HashMap<>();
		map.put("status", "2");
		map.put("scene", "1");
		List<TevglSiteNewsVo> tevglSiteNewsVos = tevglSiteNewsMapper.selectListByMapForOfficial(map);
		if (tevglSiteNewsVos != null && tevglSiteNewsVos.size() > 0) {
			TevglSiteNews t = new TevglSiteNews();
			tevglSiteNewsVos.stream().forEach(item -> {
				t.setNewsid(item.getNewsid());
				t.setViewNum(RandomUtils.nextInt(1, 100));
				tevglSiteNewsMapper.plusNum(t);
			});
		}
	}
	
}
