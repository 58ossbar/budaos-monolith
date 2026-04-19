package com.budaos.modules.mgr.tch.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.ConstantProd;
import com.budaos.modules.common.DictService;
import com.budaos.modules.common.enums.BizCodeEnume;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.book.api.TevglBookMajorService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.pkg.api.TevglPkgInfoService;
import com.budaos.modules.evgl.tch.api.TevglTchClassService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomService;
import com.budaos.modules.evgl.tch.api.TevglTchClassroomTraineeService;
import com.budaos.modules.evgl.tch.api.TevglTchTeacherService;
import com.budaos.modules.evgl.tch.domain.TevglTchClassroom;
import com.budaos.modules.evgl.tch.domain.TevglTchTeacher;
import com.budaos.modules.evgl.tch.vo.TevglTchClassroomTraineeVo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p> Title: 课堂管理</p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/tch/tevgltchclassroom")
public class TevglTchClassroomController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchClassroomController.class);
	@Autowired
	private TevglTchClassroomService tevglTchClassroomService;
	@Autowired
	private TevglPkgInfoService tevglPkgInfoService;
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private TevglBookMajorService tevglBookMajorService;
	@Autowired
	private TevglTchClassService tevglTchClassService;
	@Autowired
	private DictService dictService;
	@Autowired
	private TevglTchTeacherService tevglTchTeacherService;
	@Autowired
	private TevglTchClassroomTraineeService tevglTchClassroomTraineeService;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		return tevglTchClassroomService.query(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglTchClassroomService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		//return tevglTchClassroomService.view(id);
		return tevglTchClassroomService.viewClassroomBaseInfo(id, ConstantProd.ADMINISTRATOR);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:add') or hasAuthority('tch:tevgltchclassroom:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglTchClassroom tevglTchClassroom) {
		if(StrUtils.isEmpty(tevglTchClassroom.getCtId())) { //新增
			return tevglTchClassroomService.save(tevglTchClassroom);
		} else {
			return tevglTchClassroomService.update(tevglTchClassroom);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglTchClassroomService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglTchClassroomService.deleteBatch(ids);
	}
	
	/**
	 * 修改课堂
	 * @param tevglTchClassroom
	 * @return
	 */
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:edit')")
	@SysLog("修改课堂")
	@PostMapping("/save")
	public R saveClassroomInfo(@RequestBody TevglTchClassroom tevglTchClassroom) {
		return tevglTchClassroomService.updateClassroomInfo(tevglTchClassroom, ConstantProd.ADMINISTRATOR);
	}
	
	/**
     * 结束课堂
     * @param ctId
     * @return
     */
    @RequestMapping("/end")
    @PreAuthorize("hasAuthority('tch:tevgltchclassroom:end')")
	@SysLog("结束课堂")
    public R end(String ctId) {
        return tevglTchClassroomService.end(ctId, ConstantProd.ADMINISTRATOR);
    }
    
    /**
     * 移交课堂
     * @param ctId
     * @return
     */
    @PostMapping("/turnOver")
    @PreAuthorize("hasAuthority('tch:tevgltchclassroom:turnover')")
	@SysLog("移交课堂")
    public R turnOver(String ctId, String traineeId) {
        return tevglTchClassroomService.turnOver(ctId, traineeId);
    }
    
    /**
	 * 教学包下拉列表（包含自己创建的、被授权的、以及免费的）
	 * @param params {'subjectRef':''}
	 * @return
	 */
	@GetMapping("/listPkgInfoSelect")
	public R listPkgInfoSelect(@RequestParam Map<String, Object> params) {
		// 不展示该值为3的，可见性(来源字典:1私有or2公有3都不可见)(重点)
		params.put("displayNo", "3");
		// 且只展示发布状态的
		params.put("releaseStatus", "Y");
		List<Map<String,Object>> list = tevglPkgInfoService.listPkgInfoForSelect(params, ConstantProd.ADMINISTRATOR, true, true);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 课程下拉列表
	 * @param params
	 * @return
	 */
	@GetMapping("/listSelectSubject")
	public R listSelectSubject(@RequestParam Map<String, Object> params) {
		params.put("state", "Y"); // 状态(Y有效N无效)
		return tevglBookSubjectService.listSelectSubject(params);
	}
	
	/**
     * <p>职业路径下拉列表</p>
     * @author huj
     * @data 2019年8月20日
     * @return
     */
    @GetMapping("/getMajorList")
    public R getMajorList(@RequestParam Map<String, Object> map) {
        map.put("state", "Y"); // 状态(Y有效N无效)
        return R.ok().put(Constant.R_DATA, tevglBookMajorService.selectListByMap(map));
    }
    
    /**
     * <p>班级下拉列表</p>
     * @author huj
     * @data 2019年8月19日
     * @return
     */
    @GetMapping("/getClassList")
    public R getClassListData(@RequestParam Map<String, Object> params) {
        return tevglTchClassService.queryClassListData(params, ConstantProd.ADMINISTRATOR);
    }
    
    /**
     * 课堂状态
     * @return
     */
    @GetMapping("/listClassroomState")
    public R listClassroomState() {
        List<Map<String,Object>> dictList = dictService.getDictList("classroomState");
        return R.ok().put(Constant.R_DATA, dictList);
    }
    
    @GetMapping("/queryTeacherList")
    public R queryTeacherList(@RequestParam Map<String, Object> map) {
    	List<TevglTchTeacher> list = tevglTchTeacherService.queryNoPage(map);
        return R.ok().put(Constant.R_DATA, list);
    }
    
	/**
	 * 查询课堂成员
	 * @param map
	 * @return
	 */
	@GetMapping("/findClassroomTraineeList")
	@SysLog("查询课堂成员")
	public R findClassroomTraineeList(@RequestParam Map<String, Object> map) {
		if (StrUtils.isNull(map.get("ctId"))) {
			return R.error(BizCodeEnume.PARAM_MISSING.getCode(), BizCodeEnume.PARAM_MISSING.getMsg());
		}
		List<TevglTchClassroomTraineeVo> list = tevglTchClassroomTraineeService.findClassroomTraineeList(map);
		return R.ok().put(Constant.R_DATA, list);
	}
	
	/**
	 * 批量更新选中的数据，在课堂结束后，允许再进入课堂
	 * @param ids
	 * @param ctId
	 * @return
	 */
	@RequestMapping(value = "batchUpdateAccessState", method = RequestMethod.POST)
	@SysLog("批量更新选中的数据，在课堂结束后，允许再进入课堂")
	@PreAuthorize("hasAuthority('tch:tevgltchclassroom:set')")
	public R batchUpdateAccessState(@RequestBody List<String> ids, String ctId) {
		return tevglTchClassroomTraineeService.batchUpdateAccessState(ctId, ids);
	}
}
