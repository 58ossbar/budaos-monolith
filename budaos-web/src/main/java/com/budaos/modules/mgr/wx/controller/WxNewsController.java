package com.budaos.modules.mgr.wx.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.site.api.TevglSiteNewsService;
import com.budaos.modules.evgl.site.vo.TevglSiteNewsVo;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author huj
 * @create 2022-01-04 9:16
 * @email hujun@budaos.com
 */
@RestController
@RequestMapping("/api/wx/official/news")
public class WxNewsController {

    @Autowired
    private TevglSiteNewsService tevglSiteNewsService;

    /**
     * 根据条件查询记录
     * @param parmas
     * @return
     */
    @GetMapping("/queryForOfficial")
    @PreAuthorize("hasAuthority('official:tevglsitenews:query')")
    public R queryForOfficial(@RequestParam Map<String, Object> parmas) {
        return tevglSiteNewsService.queryForOfficial(parmas);
    }


    /**
     * 保存
     * @param vo
     * @return
     */
    @PostMapping("/saveForOfficial")
    @PreAuthorize("hasAuthority('official:tevglsitenews:add') or hasAuthority('official:tevglsitenews:edit')")
    public R saveForOfficial(@RequestBody TevglSiteNewsVo vo) {
    	if (StrUtils.isEmpty(vo.getNewsid())) {
    		return tevglSiteNewsService.saveForOfficial(vo);	
    	} else {
    		return tevglSiteNewsService.updateForOfficial(vo);
    	}
    }
    
    /**
     * 查看
     * @param newsid
     * @return
     */
    @GetMapping("/viewForOfficial/{id}")
    @PreAuthorize("hasAuthority('official:tevglsitenews:view')")
    public R viewForOfficial(@PathVariable("id") String newsid) {
        return tevglSiteNewsService.viewForOfficial(newsid);
    }

    /**
     * 更新状态
     * @param newsid
     * @param status
     * @return
     */
    @PostMapping("/release")
    @PreAuthorize("hasAuthority('official:tevglsitenews:release')")
    public R release(String newsid, String status) {
        return tevglSiteNewsService.release(newsid, status);
    }
    
    /**
     * 批量发布
     * @param newsidList
     * @return
     */
    @PostMapping("/releaseBatch")
    @PreAuthorize("hasAuthority('official:tevglsitenews:release')")
    public R release(@RequestBody String[] newsidList) {
        return tevglSiteNewsService.releaseBatch(newsidList, "Y");
    }

    /**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('official:tevglsitenews:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglSiteNewsService.deleteBatch(ids);
	}

}
