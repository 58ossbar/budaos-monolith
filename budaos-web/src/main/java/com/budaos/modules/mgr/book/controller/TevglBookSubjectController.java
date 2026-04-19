package com.budaos.modules.mgr.book.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import com.budaos.modules.evgl.book.api.BookService;
import com.budaos.modules.evgl.book.api.TevglBookSubjectService;
import com.budaos.modules.evgl.book.domain.TevglBookChapter;
import com.budaos.modules.evgl.book.domain.TevglBookSubject;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@RestController
@RequestMapping("/api/book/tevglbooksubject")
public class TevglBookSubjectController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglBookSubjectController.class);
	@Autowired
	private TevglBookSubjectService tevglBookSubjectService;
	@Autowired
	private BookService bookService;
	/**
	 * 查询列表(返回List<Bean>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/query")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:query')")
	@SysLog("查询列表(返回List<Bean>)")
	public R query(@RequestParam Map<String, Object> params) {
		params.put("isSubjectRefNull", "Y");
		return tevglBookSubjectService.selectListByMapForMgr(params);
	}
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param Map
	 * @return R
	 */
	@GetMapping("/queryForMap")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:query')")
	@SysLog("查询列表(返回List<Map<String, Object>)")
	public R queryForMap(@RequestParam Map<String, Object> params) {
		return tevglBookSubjectService.queryForMap(params);
	}

	/**
	 * 查看明细
	 */
	@GetMapping("/view/{id}")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:view')")
	@SysLog("查看明细")
	public R view(@PathVariable("id") String id) {
		return tevglBookSubjectService.view(id);
	}
	
	/**
	 * 执行数据新增
	 * 
	 */
	@PostMapping("/saveorupdate")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:add') or hasAuthority('book:tevglbooksubject:edit')")
	@SysLog("执行数据新增")
	public R saveOrUpdate(@RequestBody(required = false) TevglBookSubject tevglBookSubject) {
		if(StrUtils.isEmpty(tevglBookSubject.getSubjectId())) { //新增
			return tevglBookSubjectService.save(tevglBookSubject);
		} else {
			return tevglBookSubjectService.update(tevglBookSubject);
		}
	}
	
	/**
	 * 单条删除
	 */
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:delete')")
	@SysLog("单条删除")
	public R delete(@PathVariable("id") String id) {
		return tevglBookSubjectService.delete(id);
	}
	
	/**
	 * 批量删除
	 */
	@PostMapping("/deletes")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:delete')")
	@SysLog("批量删除")
	public R deleteBatch(@RequestBody(required = true) String[] ids) {
		return tevglBookSubjectService.deleteBatch(ids);
	}
	
	/**
	 * <p>更新状态</p>
	 * @author huj
	 * @data 2019年7月28日
	 * @param tevglTchTeacher
	 * @return
	 */
	@PostMapping("/updateStatus")
	public R updateStatus(@RequestBody(required = false) TevglBookSubject tevglBookSubject) {
		return tevglBookSubjectService.updateState(tevglBookSubject);
	}
	
	/**
	 * <p>从字典获取活教材封面图</p>
	 * @author huj
	 * @data 2019年8月6日
	 * @return
	 */
	@GetMapping("/getSubjectLogo")
	public R getSubjectLogo() {
		return R.ok().put(Constant.R_DATA, tevglBookSubjectService.getSubjectLogo());
	}
	
	/**
	 * 课程章节树
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/getTreeForMgr")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:content')")
	public R getTreeForMgr(String subjectId) {
		return tevglBookSubjectService.getTreeForMgr(subjectId);
	}
	
	@PostMapping("/saveChapterInfo")
	public R saveChapterInfo(@RequestBody TevglBookChapter tevglBookChapter) {
		if (StrUtils.isEmpty(tevglBookChapter.getChapterId())) {
			return bookService.saveChapterInfo(tevglBookChapter);
		} else {
			return bookService.rename(tevglBookChapter.getChapterId(), tevglBookChapter.getChapterName());
		}
	}
	
	@PostMapping("/remove")
	public R remove(String chapterId) {
		return bookService.remove(chapterId);
	}
	
	@PostMapping("/saveChapterContent")
	public R saveChapterContent(@RequestBody TevglBookChapter tevglBookChapter) {
		if (!StrUtils.isEmpty(tevglBookChapter.getChapterId())) {
			return bookService.saveChapterContent(tevglBookChapter);
		}
		return R.ok("保存成功");
	}
	
	/**
	 * 查看明细
	 */
	@GetMapping("/viewChapter/{id}")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:view')")
	@SysLog("查看明细")
	public R viewChapter(@PathVariable("id") String id) {
		return bookService.viewChapter(id);
	}
	
	/**
	 * 查看无分页的课程列表数据
	 */
	@GetMapping("/querySubjectList")
	@PreAuthorize("hasAuthority('book:tevglbooksubject:query')")
	@SysLog("查看无分页的课程列表数据")
	public R querySubjectList(@RequestParam Map<String, Object> map) {
		return R.ok().put(Constant.R_DATA, tevglBookSubjectService.querySubjectList(map));
	}
}
