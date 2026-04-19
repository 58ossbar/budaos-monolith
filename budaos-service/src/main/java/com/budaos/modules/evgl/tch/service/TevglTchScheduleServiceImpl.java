package com.budaos.modules.evgl.tch.service;

import com.budaos.common.cbsecurity.logs.annotation.SysLog;
import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.common.utils.ServiceLoginUtil;
import com.budaos.common.validator.ValidatorUtils;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.tch.api.TevglTchScheduleService;
import com.budaos.modules.evgl.tch.domain.*;
import com.budaos.modules.evgl.tch.params.TevglTchScheduleParams;
import com.budaos.modules.evgl.tch.persistence.*;
import com.budaos.modules.evgl.trainee.domain.TevglTraineeInfo;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

@Service
@Transactional
public class TevglTchScheduleServiceImpl implements TevglTchScheduleService {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(TevglTchScheduleServiceImpl.class);
	@Autowired
	private TevglTchScheduleMapper tevglTchScheduleMapper;
	@Autowired
	private TevglTchScheduleBetweenMapper tevglTchScheduleBetweenMapper;
	@Autowired
	private TevglTchTeacherMapper tevglTchTeacherMapper;
	@Autowired
	private TevglTchScheduleTrainingRoomMapper tevglTchScheduleTrainingRoomMapper;
	@Autowired
	private TevglTchClasstraineeMapper tevglTchClasstraineeMapper;
	@Autowired
	private ConvertUtil convertUtil;
	@Autowired
	private ServiceLoginUtil serviceLoginUtil;
	@Autowired
	private TevglTchScheduleHistoryMapper tevglTchScheduleHistoryMapper;
	@Autowired
	private TevglTchScheduleClassMapper tevglTchScheduleClassMapper;
	
	/**
	 * 查询列表(返回List<Bean>)
	 * @param  params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Bean>)")
	public R query( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TevglTchSchedule> tevglTchScheduleList = tevglTchScheduleMapper.selectListByMap(query);
		convertUtil.convertUserId2RealName(tevglTchScheduleList, "createUserId", "updateUserId");
		convertUtil.convertUserId2RealName(tevglTchScheduleList, "createUserId", "updateUserId");
		PageUtils pageUtil = new PageUtils(tevglTchScheduleList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	
	/**
	 * 查询列表(返回List<Map<String, Object>)
	 * @param params
	 * @return R
	 */
	@SysLog(value="查询列表(返回List<Map<String, Object>)")
	public R queryForMap( Map<String, Object> params) {
		// 构建查询条件对象Query
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<Map<String, Object>> tevglTchScheduleList = tevglTchScheduleMapper.selectListMapByMap(query);
		convertUtil.convertUserId2RealName(tevglTchScheduleList, "create_user_id", "update_user_id");
		PageUtils pageUtil = new PageUtils(tevglTchScheduleList,query.getPage(),query.getLimit());
		return R.ok().put(Constant.R_DATA, pageUtil);
	}
	/**
	 * 新增
	 * @param tevglTchSchedule
	 * @throws BudaosException
	 */
	@SysLog(value="新增")
	public R save(TevglTchSchedule tevglTchSchedule) throws BudaosException {
		tevglTchSchedule.setScheduleId(Identities.uuid());
		tevglTchSchedule.setCreateUserId(serviceLoginUtil.getLoginUserId());
		tevglTchSchedule.setCreateTime(DateUtils.getNowTimeStamp());
		ValidatorUtils.check(tevglTchSchedule);
		tevglTchScheduleMapper.insert(tevglTchSchedule);
		return R.ok();
	}
	/**
	 * 修改
	 * @param tevglTchSchedule
	 * @throws BudaosException
	 */
	@SysLog(value="修改")
	public R update( TevglTchSchedule tevglTchSchedule) throws BudaosException {
	    tevglTchSchedule.setUpdateUserId(serviceLoginUtil.getLoginUserId());
	    tevglTchSchedule.setUpdateTime(DateUtils.getNowTimeStamp());
	    ValidatorUtils.check(tevglTchSchedule);
		tevglTchScheduleMapper.update(tevglTchSchedule);
		return R.ok();
	}
	/**
	 * 单条删除
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="单条删除")
	public R delete(String id) throws BudaosException {
		TevglTchSchedule tevglTchSchedule = tevglTchScheduleMapper.selectObjectById(id);
		if (tevglTchSchedule == null) {
			return R.error("已删除，请刷新数据");
		}
		TevglTchScheduleBetween tevglTchScheduleBetween = tevglTchScheduleBetweenMapper.selectObjectById(tevglTchSchedule.getTimeQuantumId());
		String targetTime = tevglTchSchedule.getSchedule() + " " + tevglTchScheduleBetween.getBeginTime() + ":00";
		boolean before = targetDateBeforeNowDate(targetTime);
		if (before) {
			return R.error("时间已过，此记录不再允许删除！");
		}
		tevglTchScheduleMapper.delete(id);
		// 删除管理的班级
		tevglTchScheduleMapper.deleteScheduleClass(id);
		return R.ok("删除成功");
	}
	/**
	 * 批量删除
	 * @param ids
	 * @throws BudaosException
	 */
	@SysLog(value="批量删除")
	public R deleteBatch(String[] ids) throws BudaosException {
		tevglTchScheduleMapper.deleteBatch(ids);
		return R.ok();
	}
	/**
	 * 查看明细
	 * @param id
	 * @throws BudaosException
	 */
	@SysLog(value="查看明细")
	public R view(String id) {
		return R.ok().put(Constant.R_DATA, tevglTchScheduleMapper.selectObjectById(id));
	}

	@Override
	public R queryScheduleData(Map<String, Object> map) {
		boolean emptyDate = StrUtils.isNull(map.get("beginDate")) || StrUtils.isNull(map.get("endDate"));
		if (emptyDate) {
			return R.error("必传参数为空");
		}
		// 类型为晚自习时，允许同一时间，同一个老师，在多个教室
		// 1上课2活动日3放假4自习5休息6其它
		Object type = map.get("type");
		// 以下为必传参数，如果没有，则直接返回空白数据
		Object beginDate = map.get("beginDate");
		Object endDate = map.get("endDate");
		Object roomId = map.get("roomId");
		Object teacherId = map.get("teacherId");
		Object classId = map.get("classId");
		// 获取两个日期之间的天数
		List<String> thisWeekDayList = getDays(beginDate.toString(), endDate.toString());
		// 最终返回数据
		Map<String, Object> returnData = new HashMap<>();
		// 已安排的班级授课数据
		List<TevglTchSchedule> existedClassScheDuleList = new ArrayList<>();
		// 已安排的老师授课数据
		List<TevglTchSchedule> existedTeacherScheDuleList = new ArrayList<>();
		//
		List<TevglTchSchedule> existedRoomScheDuleList = new ArrayList<>();
		// 1.查询时间段
		List<TevglTchScheduleBetween> timeSlotList = tevglTchScheduleBetweenMapper.selectListByMap(new HashMap<>());
		// 作为查询条件，班级和老师不能同时为空！日期更不能为空
		boolean emptyClassId = StrUtils.isNull(map.get("classId")) || StrUtils.isEmpty(map.get("classId").toString());
		boolean emptyTeacherId = StrUtils.isNull(map.get("teacherId")) || StrUtils.isEmpty(map.get("teacherId").toString());
		boolean emptyRoomId = StrUtils.isNull(map.get("roomId")) || StrUtils.isEmpty(map.get("roomId").toString());
		// 假定本周起止日期为 2021-11-08 至 2021-11-14
		//List<String> thisWeekDayList = Arrays.asList("2021-11-08", "2021-11-09", "2021-11-10","2021-11-11","2021-11-12","2021-11-13", "2021-11-14");
		// 2.查询
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		log.debug("当前查询条件 => {}", map);
		// 标识
		boolean flag = "Y".equals(map.get("flag"));
		boolean selectedRoomId = StrUtils.notNull(roomId);
		boolean selectedTeacherId = StrUtils.notNull(teacherId);
		boolean selectedClassId = StrUtils.notNull(teacherId);
		// 查询模式下
		if (!flag) {

			// 前提条件
			// 同一时间内，一个班级只会在一个教室上课
			// 同一时间内，一个老师只会在一个教室上课
			// 同一时间内，一个教室内，只会有一个老师、多个班级在此上课

			// 教室id可能为空，则直接返回空白数据，如果不为空，那么是可以查出相关数据并展示，因为一个教室内：只允许一个老师、单或多个班级同时上课
			// 如果查询条件：班级id不为空，
			// 班级id和老师id不能同时为空，同时为空就查不了数据

			if (emptyRoomId && emptyClassId && emptyTeacherId) {
				returnData.put("timeSlotList", timeSlotList);
				returnData.put("weekList", createDefaultTemplateWeeksV2(thisWeekDayList, timeSlotList));
				return R.ok().put(Constant.R_DATA, returnData);
			}
		}
		// 如果是排课模式中查询
		if (flag) {
			// 如果没有选择任何条件，直接返回
			if (emptyRoomId && emptyClassId && emptyTeacherId) {
				returnData.put("timeSlotList", timeSlotList);
				returnData.put("weekList", createDefaultTemplateWeeksV1(thisWeekDayList, timeSlotList));
				return R.ok().put(Constant.R_DATA, returnData);
			}
			Map<String, Object> ps = new HashMap<String, Object>();
			// 这个时间段，这个教室的授课安排记录
			if (selectedRoomId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				ps.put("roomId", roomId);
				existedRoomScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				convertUtil.convertDict(existedRoomScheDuleList, "scheduleStateName", "scheduleState");
			}
			// 这个时间段，这个老师的授课安排记录
			if (selectedTeacherId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				ps.put("teacherId", teacherId);
				existedTeacherScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				log.debug("老师{}已安排的数据 => {}", teacherId, existedTeacherScheDuleList.size());
				convertUtil.convertDict(existedTeacherScheDuleList, "scheduleStateName", "scheduleState");
			}
			// 这个时间段，这个班级已经在哪些教室上课了
			if (selectedClassId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				String[] split = classId.toString().split(",");
				ps.put("classIdArray", split);
				existedClassScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				convertUtil.convertDict(existedClassScheDuleList, "scheduleStateName", "scheduleState");
			}
		}
		List<TevglTchSchedule> dataList = tevglTchScheduleMapper.queryScheduleData(map);
		log.debug("根据条件查询 => {}", map);
		log.debug("查询结果 => {}", dataList.size());
		convertUtil.convertDict(dataList, "scheduleStateName", "scheduleState");
		List<Map<String, Object>> weekList = new ArrayList<>();
		for (String day : thisWeekDayList) {
			Map<String, Object> weeks = new HashMap<>();
			Map<String, Object> mm = new HashMap<>();
			for (TevglTchScheduleBetween ob : timeSlotList) {
				// 存放这天，这个时间段的数据
				List<TevglTchSchedule> scheduleList = dataList.stream().filter(tevglTchSchedule -> tevglTchSchedule.getSchedule().equals(day) && tevglTchSchedule.getTimeQuantumId().equals(ob.getTimeQuantumId())).collect(Collectors.toList());
				if (scheduleList != null && scheduleList.size() > 0) {
					for (TevglTchSchedule scheduleData : scheduleList) {
						boolean flag1 = existedTeacherScheDuleList.stream().anyMatch(a -> a.getSchedule().equals(scheduleData.getSchedule()) && a.getTimeQuantumId().equals(scheduleData.getTimeQuantumId()));
						if (flag1) {
							scheduleData.setDisabled(true);
						}
					}
					// 放入数据
					mm.put(ob.getTimeQuantumId(), scheduleList.get(0));
				} else {
					mm.put(ob.getTimeQuantumId(), null);
				}
				// 同一时间内，该教室是否已经被安排了
				List<TevglTchSchedule> thisRoomDayTimeSchduleList = matching(existedRoomScheDuleList, day, ob.getTimeQuantumId());
				if (thisRoomDayTimeSchduleList != null && thisRoomDayTimeSchduleList.size() > 0) {
					thisRoomDayTimeSchduleList.get(0).setDisabled(true);
					mm.put(ob.getTimeQuantumId(), thisRoomDayTimeSchduleList.get(0));
				}
				// 同一时间内，该老师是否已经被安排了
				List<TevglTchSchedule> thisTeacherDayTimeSchduleList = matching(existedTeacherScheDuleList, day, ob.getTimeQuantumId());
				if (thisTeacherDayTimeSchduleList != null && thisTeacherDayTimeSchduleList.size() > 0) {
					thisTeacherDayTimeSchduleList.get(0).setDisabled(true);
					mm.put(ob.getTimeQuantumId(), thisTeacherDayTimeSchduleList.get(0));
				}
				// 同一时间内，该班级是否已经被安排了
				List<TevglTchSchedule> thisClassDayTimeSchduleList = matching(existedClassScheDuleList, day, ob.getTimeQuantumId());
				if (thisClassDayTimeSchduleList != null && thisClassDayTimeSchduleList.size() > 0) {
					thisClassDayTimeSchduleList.get(0).setDisabled(true);
					mm.put(ob.getTimeQuantumId(), thisClassDayTimeSchduleList.get(0));
				}
				// 如果时间已过，肯定也不能再设置了
				TevglTchSchedule okData = (TevglTchSchedule) mm.get(ob.getTimeQuantumId());
				if (okData != null) {
					doHandleBooleanValue(okData);
					okData.setScheduleStateName(okData.getScheduleState());
					convertUtil.convertDict(okData, "scheduleStateName", "scheduleState");
					mm.put(ob.getTimeQuantumId(), okData);
					// TODO 晚自习中，一个老师可能在多个教室
					// TODO 晚自习中，一个班级同一时间内，只能在一个教师
					// 晚自习
					if (ifAtNight(ob.getTimeQuantumId())) {
						// 取出当天该时间段的授课记录
						List<TevglTchSchedule> list = new ArrayList<>();
						/*List<TevglTchSchedule> collect = scheduleList.stream().filter(a -> a.getTimeQuantumId().equals(ob.getTimeQuantumId())).collect(Collectors.toList());
						if (collect.size() > 0) {
							list.addAll(collect);
						}*/
						Map<String, Object> sm = new HashMap<>();
						Boolean b = false;
						// 班级是否冲突：如果当前用户选择的班级，已经在其它教室了，禁选
						boolean ifClassConflicts = existedClassScheDuleList.stream().anyMatch(a -> a.getClassId().equals(classId) && a.getTimeQuantumId().equals(ob.getTimeQuantumId()));
						log.debug("班级{}已经被安排至其它教室，匹配结果{}", classId, ifClassConflicts);
						// 教室是否冲突：
						boolean ifsRoomConflicts = existedRoomScheDuleList.stream().anyMatch(a -> a.getRoomId().equals(roomId) && a.getTimeQuantumId().equals(ob.getTimeQuantumId()));
						log.debug("教室{}已经被安排了班级，匹配结果{}", roomId, ifsRoomConflicts);
						if (ifClassConflicts || ifsRoomConflicts) {
							b = true;
							if (thisClassDayTimeSchduleList != null && thisClassDayTimeSchduleList.size() > 0) {
								thisClassDayTimeSchduleList.stream().forEach(op -> {
									if (!list.stream().anyMatch(a -> a.getScheduleId().equals(op.getScheduleId()))) {
										list.add(op);
									}
								});
							}
							if (thisRoomDayTimeSchduleList != null && thisRoomDayTimeSchduleList.size() > 0) {
								thisRoomDayTimeSchduleList.stream().forEach(op -> {
									if (!list.stream().anyMatch(a -> a.getScheduleId().equals(op.getScheduleId()))) {
										list.add(op);
									}
								});
							}
						}
						if (existedTeacherScheDuleList != null && existedTeacherScheDuleList.size() > 0) {
							existedTeacherScheDuleList.stream().forEach(op -> {
								if (!list.stream().anyMatch(a -> a.getScheduleId().equals(op.getScheduleId()))) {
									list.add(op);
								}
							});
						}
						list.stream().forEach(myData -> {
							doHandleBooleanValue(myData);
						});
						sm.put("disabled", b);
						sm.put("list", list);
						mm.put(ob.getTimeQuantumId(), sm);
					}
				} else {
					String targetTime = day + " 23:59:59";
					boolean b = targetDateBeforeNowDate(targetTime);
					if (b) {
						Map<String, Object> tempData = new HashMap<>();
						tempData.put("disabled", true);
						mm.put(ob.getTimeQuantumId(), tempData);
					}
				}
			}
			weeks.put("weeks", mm);
			weeks.put("date", day);
			weekList.add(weeks);
		}
		// 返回数据
		returnData.put("timeSlotList", timeSlotList);
		returnData.put("weekList", weekList);
		return R.ok().put(Constant.R_DATA, returnData);
	}
	
	/**
	 * 判断当前时间段是否为晚自习
	 * @param timeQuantumId 值可能为1，2，3，4，5，6。
	 * @return
	 * @apiNote
	 * t_evgl_tch_schedule_between表中晚自习认定为5，6
	 */
	private boolean ifAtNight(String timeQuantumId) {
		return Arrays.asList("5", "6").contains(timeQuantumId);
	}
	
	/**
	 * 匹配，返回 同一天，同一时间段内，的授课安排记录
	 * @param sourceTevglTchScheduleList
	 * @param day 必传参数，哪一天，如 2021-11-17
	 * @param timeQuantumId 必传参数
	 * @return
	 */
	private List<TevglTchSchedule> matching(List<TevglTchSchedule> sourceTevglTchScheduleList, String day, String timeQuantumId) {
		List<TevglTchSchedule> thisTeacherDayTimeSchduleList = sourceTevglTchScheduleList.stream()
				.filter(a -> a.getSchedule().equals(day)
						&& a.getTimeQuantumId().equals(timeQuantumId)
				)
				.collect(Collectors.toList());
		return thisTeacherDayTimeSchduleList;
	}
	
	/**
	 * 处理删除权限
	 * @param okData
	 * @return
	 */
	private boolean doHandleBooleanValue(TevglTchSchedule okData) {
		if (okData == null) {
			return false;
		}
		String targetTime = okData.getSchedule() + " " + okData.getBeginTime() + ":00";
		// 判断时间是否已过，以往日期不再允许删除
		boolean targetDateBeforeNowDate = targetDateBeforeNowDate(targetTime);
		if (targetDateBeforeNowDate) {
			okData.setHasDeleteIcon(false);
			return false;
		} else {
			okData.setHasDeleteIcon(true);
			return true;
		}
	}
	
	/**
	 * <p>获取两日期间所有的日期</p>
	 * @author huj
	 * @data 2019年11月30日
	 * @param startTime 必传参数，格式要求为 yyyy-MM-dd
	 * @param endTime 必传参数，格式要求为 yyyy-MM-dd
	 * @return
	 */
	public static List<String> getDays(String startTime, String endTime) {
		// 返回的日期集合
		List<String> days = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = dateFormat.parse(startTime);
			Date end = dateFormat.parse(endTime);
			Calendar tempStart = Calendar.getInstance();
			tempStart.setTime(start);
			Calendar tempEnd = Calendar.getInstance();
			tempEnd.setTime(end);
			tempEnd.add(Calendar.DATE, +1); // 日期加1(包含结束)
			while (tempStart.before(tempEnd)) {
				days.add(dateFormat.format(tempStart.getTime()));
				tempStart.add(Calendar.DAY_OF_YEAR, 1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}
	
	/**
	 * <b>判断目标日期是否在当前系统时间之前</b>
	 * @param str 格式要求yyyy-MM-dd HH:mm:ss
	 * @return {@link Boolean}
	 * @apiNote 示例：
	 * 假设形参传入了2021-11-08 01:15:00，且当前系统时间为2021-11-12 10:30:00，那么是在之前，返回true
	 */
	public static boolean targetDateBeforeNowDate(String str) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = dateFormat.parse(str);
			Calendar tempStart = Calendar.getInstance();
		    tempStart.setTime(start);
		    Calendar tempEnd = Calendar.getInstance();
		    boolean flag = tempStart.before(tempEnd);
		    return flag;
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 新增
	 * @param tevglTchSchedule
	 * @return
	 */
	@Override
	public R saveBatch(TevglTchSchedule tevglTchSchedule) throws BudaosException {
		R r = checkIsPass(tevglTchSchedule);
		if (!r.get("code").equals(0)) {
			return r;
		}
		if (!checkUniqueness(tevglTchSchedule)) {
			return R.error("提交的数据不合法");
		}
		List<Map<String, Object>> list = tevglTchSchedule.getList();
		// 等待入库的数据
		List<TevglTchSchedule> insertList = new ArrayList<>();
		List<Map<String, Object>> insertClassList = new ArrayList<>();
		// 组装数据
		list.stream().forEach(item -> {
			TevglTchSchedule t = new TevglTchSchedule();
			String scheduleId = Identities.uuid();
			t.setScheduleId(scheduleId);
			t.setRoomId(tevglTchSchedule.getRoomId());
			t.setClassId(tevglTchSchedule.getClassId());
			t.setTeacherId(tevglTchSchedule.getTeacherId());
			t.setSchedule(item.get("schedule").toString());
			t.setTimeQuantumId(item.get("timeQuantumId").toString());
			t.setCreateTime(DateUtils.getNowTimeStamp());
			t.setCreateUserId(serviceLoginUtil.getLoginUserId());
			t.setScheduleState(StrUtils.isEmpty(tevglTchSchedule.getScheduleState()) ? "1" : tevglTchSchedule.getScheduleState());
			t.setSubjectId(tevglTchSchedule.getSubjectId());
			t.setState("Y");
			insertList.add(t);
		});
		if (insertList.size() > 0) {
			insertList.stream().forEach(item -> {
				String classId = item.getClassId();
				if (StrUtils.isNotEmpty(classId)) {
					String[] split = classId.split(",");
					for (int i = 0; i < split.length; i++) {
						// 绑定教室与班级的关系（同一时间内，考虑一个教室可以有多个班级在此上课）
						Map<String, Object> data = new HashMap<>();
						data.put("scheduleId", item.getScheduleId());
						data.put("classId", split[i]);
						insertClassList.add(data);
					}
				}
			});
			tevglTchScheduleMapper.insertBatch(insertList);
			if (insertClassList.size() > 0) {
				tevglTchScheduleMapper.insertBatchScheduleClass(insertClassList);
			}
		}
		return R.ok("保存成功");
	}
	
	private R checkIsPass(TevglTchSchedule tevglTchSchedule) {
		if (StrUtils.isEmpty(tevglTchSchedule.getClassId()) || StrUtils.isEmpty(tevglTchSchedule.getRoomId()) || StrUtils.isEmpty(tevglTchSchedule.getTeacherId())) {
			return R.error("班级、教室、老师必须设置");
		}
		if (tevglTchSchedule.getList() == null || tevglTchSchedule.getList().size() == 0) {
			return R.error("请具体设置");
		}
		// 节次时间段
		List<TevglTchScheduleBetween> timeList = tevglTchScheduleBetweenMapper.selectListByMap(new HashMap<>());
		// 老师信息
		TevglTchTeacher tevglTchTeacher = getTevglTchTeacher(tevglTchSchedule.getTeacherId());
		// 教室信息
		List<TevglTchScheduleTrainingRoom> trainingRoomList = tevglTchScheduleTrainingRoomMapper.selectListByMap(new HashMap<>());
		// 前端用户提交的数据
		List<Map<String, Object>> userSelectedList = tevglTchSchedule.getList();
		// 查询老师的授课记录
		Map<String, Object> map = new HashMap<>();
		map.put("teacherId", tevglTchSchedule.getTeacherId());
		List<TevglTchSchedule> teacherScheduleList = tevglTchScheduleMapper.selectListByMap(map);
		for (Map<String, Object> data : userSelectedList) {
			List<TevglTchSchedule> currentDayList = new ArrayList<>();
			// 如果当前设置的上课
			if ("1".equals(tevglTchSchedule.getScheduleState())) {
				currentDayList = teacherScheduleList.stream().filter(a -> a.getSchedule().equals(data.get("schedule")) && a.getTimeQuantumId().equals(data.get("timeQuantumId"))).collect(Collectors.toList());
			} else {
				// 晚自习不参与判断
				currentDayList = teacherScheduleList.stream()
						.filter(a -> a.getSchedule().equals(data.get("schedule")) 
								&& a.getTimeQuantumId().equals(data.get("timeQuantumId"))
								&& !"4".equals(a.getScheduleState())
						)
						.collect(Collectors.toList());
			}
			if (currentDayList != null && currentDayList.size() > 0) {
				String teacherName = tevglTchTeacher == null ? "" : tevglTchTeacher.getTeacherName();
				Object targetDate = data.get("schedule");
				String segmentTimeName = getSegmentTimeName(timeList, data.get("timeQuantumId"));
				String roomName = getRoomName(trainingRoomList, tevglTchSchedule.getRoomId());
				String msg = teacherName + " 无法被安排至 " + targetDate + " " + segmentTimeName + " 的【" + roomName + "】教室。";
				String reason = "因为，同一时间内，该老师已经被安排至" + getRoomName(trainingRoomList, currentDayList.get(0).getRoomId());
				return R.error(msg + reason);
			}
		}
		// TODO 查询班级的上课记录
		return R.ok();
	}
	
	private String getRoomName(List<TevglTchScheduleTrainingRoom> list, String roomId){
		if (list == null || list.size() == 0 || StrUtils.isEmpty(roomId)) {
			return "";
		}
		List<TevglTchScheduleTrainingRoom> collect = list.stream().filter(a -> a.getRoomId().equals(roomId)).collect(Collectors.toList());
		if (collect != null && collect.size() > 0) {
			return collect.get(0).getRoomName();
		}
		return "";
	}
	
	private String getRoomName(String roomId){
		if (StrUtils.isEmpty(roomId)) {
			return "";
		}
		TevglTchScheduleTrainingRoom tevglTchScheduleTrainingRoom = tevglTchScheduleTrainingRoomMapper.selectObjectById(roomId);
		if (tevglTchScheduleTrainingRoom != null) {
			return tevglTchScheduleTrainingRoom.getRoomName();
		}
		return "";
	}

	/**
	 * 获取老师信息
	 * @param teacherId
	 * @return
	 */
	private TevglTchTeacher getTevglTchTeacher(String teacherId) {
		TevglTchTeacher tevglTchTeacher = tevglTchTeacherMapper.selectObjectById(teacherId);
		if (tevglTchTeacher == null) {
			tevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(teacherId);
		}
		return tevglTchTeacher;
	}

	/**
	 * 组装名称 => 第一大节 08:30 - 10:00
	 * @param timeList
	 * @param timeQuantumId
	 * @return
	 */
	private String getSegmentTimeName(List<TevglTchScheduleBetween> timeList, Object timeQuantumId) {
		if (StrUtils.isNull(timeQuantumId) || timeList == null || timeList.size() == 0) {
			return "";
		}
		List<TevglTchScheduleBetween> collect = timeList.stream().filter(a -> a.getTimeQuantumId().equals(timeQuantumId)).collect(Collectors.toList());
		if (collect == null || collect.size() == 0) {
			return "";
		}
		return collect.get(0).getName() + " " + collect.get(0).getBeginTime() + " - " + collect.get(0).getEndTime();
	}
	
	/**
	 * 组装名称 => 第一大节 08:30 - 10:00
	 * @param timeQuantumId
	 * @return
	 */
	private String getSegmentTimeName(Object timeQuantumId) {
		if (StrUtils.isNull(timeQuantumId) ) {
			return "";
		}
		TevglTchScheduleBetween tevglTchScheduleBetween = tevglTchScheduleBetweenMapper.selectObjectById(timeQuantumId);
		if (tevglTchScheduleBetween == null) {
			return "";
		}
		return tevglTchScheduleBetween.getName() + " " + tevglTchScheduleBetween.getBeginTime() + " - " + tevglTchScheduleBetween.getEndTime();
	}

	
	/**
	 * 检查唯一性，返回true表示通过了验证，否则控制不再执行后续程序
	 * @param tevglTchSchedule
	 * @return
	 */
	private boolean checkUniqueness(TevglTchSchedule tevglTchSchedule) {
		if (StrUtils.isEmpty(tevglTchSchedule.getRoomId())) {
			return false;
		}
		// 取出用户本次选择的日期
		List<Object> userSelectedScheduleList = tevglTchSchedule.getList().stream().map(a -> a.get("schedule")).collect(Collectors.toList());
		// 从数据库根据条件查询已存在的数据
		Map<String, Object> map = new HashMap<>();
		map.put("roomId", tevglTchSchedule.getRoomId());
		map.put("scheduleList", userSelectedScheduleList);
		List<TevglTchSchedule> existedTevglTchSchedules = tevglTchScheduleMapper.selectListByMap(map);
		// 判断：同一教室、同一天、同一时间段，是否重复了
		for (int i = 0; i < tevglTchSchedule.getList().size(); i++) {
			Map<String, Object> op = tevglTchSchedule.getList().get(i);
			boolean ifRepetition = existedTevglTchSchedules.stream().anyMatch(a -> a.getTimeQuantumId().equals(op.get("timeQuantumId")) && a.getSchedule().equals(op.get("schedule")));
			// 如果匹配上了，那么说明，重复了
			if (ifRepetition) {
				// 那么，认证不通过
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 创建周模板数据
	 * @param thisWeekDayList
	 * @param timeSlotList
	 * @return
	 */
	private List<Map<String, Object>> createDefaultTemplateWeeks(List<String> thisWeekDayList, List<TevglTchScheduleBetween> timeSlotList) {
		// 定义集合，存放7个对象，对象也就是相当于某周中的某一天
		List<Map<String, Object>> weekList = new ArrayList<Map<String,Object>>();
		// 遍历天
		thisWeekDayList.stream().forEach(day -> {
			Map<String, Object> dayMap = new HashMap<>();
			Map<String, Object> timeMapData = new HashMap<>();
			timeSlotList.stream().forEach(timeSlot -> {
				timeMapData.put(timeSlot.getTimeQuantumId(), null);
			});
			dayMap.put("weeks", timeMapData);
			// 加入集合
			weekList.add(dayMap);
		});
		return weekList;
	}
	
	/*private List<Map<String, Object>> createDefaultTemplateWeeksV1(List<String> thisWeekDayList, List<TevglTchScheduleBetween> timeSlotList) {
		Map<String, Object> map = new HashMap<>();
		map.put("scheduleList", thisWeekDayList);
		List<TevglTchSchedule> existedList = tevglTchScheduleMapper.queryScheduleData(map);
		// 定义集合，存放7个对象，对象也就是相当于某周中的某一天
		List<Map<String, Object>> weekList = new ArrayList<Map<String,Object>>();
		// 遍历天
		thisWeekDayList.stream().forEach(day -> {
			// 某天的授课记录
			List<TevglTchSchedule> dayScheduleList = existedList.stream().filter(a -> a.getSchedule().equals(day)).collect(Collectors.toList());
			Map<String, Object> dayMap = new HashMap<>();
			Map<String, Object> timeMapData = new HashMap<>();
			timeSlotList.stream().forEach(timeSlot -> {
				timeMapData.put(timeSlot.getTimeQuantumId(), null);
				List<TevglTchSchedule> dataList = dayScheduleList.stream().filter(a -> a.getTimeQuantumId().equals(timeSlot.getTimeQuantumId())).collect(Collectors.toList());
				if (dataList != null && dataList.size() > 0) {
					Map<String, Object> myData = new HashMap<>();
					myData.put("roomName", dataList.stream().map(a -> a.getRoomName()).collect(Collectors.joining(",")));
					myData.put("className", dataList.stream().map(a -> a.getClassName()).collect(Collectors.joining(",")));
					myData.put("teacherName", dataList.stream().map(a -> a.getTeacherName()).collect(Collectors.joining(",")));
					myData.put("subjectName", dataList.stream().filter(a -> StrUtils.isNotEmpty(a.getSubjectName())).map(a -> a.getSubjectName()).collect(Collectors.joining(",")));
					timeMapData.put(timeSlot.getTimeQuantumId(), myData);
				}
			});
			dayMap.put("weeks", timeMapData);
			// 加入集合
			weekList.add(dayMap);
		});
		return weekList;
	}*/
	
	/**
	 * 没有传任何查询条件时，返回空模板数据
	 * @param thisWeekDayList
	 * @param timeSlotList
	 * @return
	 */
	private List<Map<String, Object>> createDefaultTemplateWeeksV1(List<String> thisWeekDayList, List<TevglTchScheduleBetween> timeSlotList) {
		Map<String, Object> map = new HashMap<>();
		map.put("scheduleList", thisWeekDayList);
		List<TevglTchSchedule> existedList = tevglTchScheduleMapper.queryScheduleData(map);
		convertUtil.convertDict(existedList, "scheduleStateName", "scheduleState");
		// 定义集合，存放7个对象，对象也就是相当于某周中的某一天
		List<Map<String, Object>> weekList = new ArrayList<Map<String,Object>>();
		// 遍历天
		ArrayList<Object> arrayList = new ArrayList<>();
		thisWeekDayList.stream().forEach(day -> {
			Map<String, Object> dayMap = new HashMap<>();
			Map<String, Object> timeMapData = new HashMap<>();
			timeSlotList.stream().forEach(timeSlot -> {
				Map<String, Object> sm = new HashMap<>();
				sm.put("disabled", false);
				sm.put("list", arrayList);
				timeMapData.put(timeSlot.getTimeQuantumId(), sm);
			});
			dayMap.put("weeks", timeMapData);
			// 加入集合
			weekList.add(dayMap);
		});
		return weekList;
	}
	
	private List<Map<String, Object>> createDefaultTemplateWeeksV2(List<String> thisWeekDayList, List<TevglTchScheduleBetween> timeSlotList) {
		Map<String, Object> map = new HashMap<>(); 
		map.put("scheduleList", thisWeekDayList);
		List<TevglTchSchedule> existedList = tevglTchScheduleMapper.queryScheduleData(map);
		convertUtil.convertDict(existedList, "scheduleStateName", "scheduleState");
		// 定义集合，存放7个对象，对象也就是相当于某周中的某一天
		List<Map<String, Object>> weekList = new ArrayList<Map<String,Object>>();
		// 遍历天
		thisWeekDayList.stream().forEach(day -> {
			// 某天的授课记录
			List<TevglTchSchedule> dayScheduleList = existedList.stream().filter(a -> a.getSchedule().equals(day)).collect(Collectors.toList());
			Map<String, Object> dayMap = new HashMap<>();
			Map<String, Object> timeMapData = new HashMap<>();
			timeSlotList.stream().forEach(timeSlot -> {
				timeMapData.put(timeSlot.getTimeQuantumId(), null);
				List<TevglTchSchedule> dataList = dayScheduleList.stream().filter(a -> a.getTimeQuantumId().equals(timeSlot.getTimeQuantumId())).collect(Collectors.toList());
				Map<String, Object> sm = new HashMap<>();
				sm.put("disabled", timeOut(day, timeSlot.getBeginTime()));
				dataList.stream().forEach(data -> {
					doHandleBooleanValue(data);
				});
				sm.put("list", dataList);
				timeMapData.put(timeSlot.getTimeQuantumId(), sm);
			});
			dayMap.put("weeks", timeMapData);
			dayMap.put("date", day);
			dayMap.put("dateName", com.budaos.modules.common.DateUtils.getWeekName(day));
			// 加入集合
			weekList.add(dayMap);
		});
		return weekList;
	}

	/**
     * 网站端查询课表
     * @param params 前端传递的查询条件
     * @param traineeInfo 登陆用户
     * @return
     */
	@Override
	public R queryScheduleDataForWeb(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo) {
		// 查询时间段
		List<TevglTchScheduleBetween> timeSlotList = tevglTchScheduleBetweenMapper.selectListByMap(new HashMap<>());
		// 获取两个日期之间的天数
		List<String> thisWeekDayList = getDays(params.getBeginDate(), params.getEndDate());
		// 最终返回数据
		Map<String, Object> returnData = new HashMap<>();
		// 判断身份如果是老师（trainee_type貌似没有被实时维护更新）
		if ("4".equals(traineeInfo.getTraineeType())) {
			params.setTeacherId(traineeInfo.getTraineeId());
		} else {
			// 判断身份如果是老师（trainee_type貌似没有被实时维护更新，所以额外判断下）
			TevglTchTeacher tevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(traineeInfo.getTraineeId());
			if (tevglTchTeacher != null) {
				params.setTeacherId(traineeInfo.getTraineeId());	
			} else {
				// 否则是学员，查询其所在班级的授课安排记录
				/*List<String> classIdList = tevglTchClasstraineeMapper.selectClassIdListByTraineeId(traineeInfo.getTraineeId());
				// 如果没有在任何班级，那么是查不到数据的！
				if (classIdList == null || classIdList.size() == 0) {
					returnData.put("timeSlotList", timeSlotList);
					returnData.put("weekList", createDefaultTemplateWeeks(thisWeekDayList, timeSlotList));
					return R.ok().put(Constant.R_DATA, returnData);
				} else {
					params.setClassId(classIdList.get(0));
				}*/
				String classId = tevglTchClasstraineeMapper.findTheLatestClassId(traineeInfo.getTraineeId());
				if (StrUtils.isEmpty(classId)) {
					returnData.put("timeSlotList", timeSlotList);
					returnData.put("weekList", createDefaultTemplateWeeksV2(thisWeekDayList, timeSlotList));
					return R.ok().put(Constant.R_DATA, returnData);
				} else {
					params.setClassId(classId);
				}	
			}
		}
		// 根据条件查询授课安排
		List<TevglTchSchedule> dataList = tevglTchScheduleMapper.queryScheduleDataForWeb(params);
		log.debug("查询条件 => {}", params);
		log.debug("查询结果 => {}", dataList.size());
		convertUtil.convertDict(dataList, "scheduleStateName", "scheduleState");
		// 组装数据
		List<Map<String, Object>> weekList = new ArrayList<>();
		for (String day : thisWeekDayList) {
			Map<String, Object> weeks = new HashMap<>();
			Map<String, Object> mm = new HashMap<>();
			for (TevglTchScheduleBetween ob : timeSlotList) {
				Map<String, Object> sm = new HashMap<>();
				//boolean disabled = timeOut(day, ob.getBeginTime());
				sm.put("disabled", true);
				// 存放这天，这个时间段的数据
				List<TevglTchSchedule> scheduleList = dataList.stream().filter(tevglTchSchedule -> tevglTchSchedule.getSchedule().equals(day) && tevglTchSchedule.getTimeQuantumId().equals(ob.getTimeQuantumId())).collect(Collectors.toList());
				/*if (scheduleList != null && scheduleList.size() > 0) {
					TevglTchSchedule tevglTchSchedule = scheduleList.get(0);
					tevglTchSchedule.setRoomName(scheduleList.stream().map(a -> a.getRoomName()).collect(Collectors.joining(",")));
					tevglTchSchedule.setClassName(scheduleList.stream().map(a -> a.getClassName()).collect(Collectors.joining(",")));
					tevglTchSchedule.setTeacherName(scheduleList.stream().map(a -> a.getTeacherName()).collect(Collectors.joining(",")));
					tevglTchSchedule.setSubjectName(scheduleList.stream().map(a -> a.getSubjectName()).collect(Collectors.joining(",")));
					mm.put(ob.getTimeQuantumId(), tevglTchSchedule);
				} else {
					mm.put(ob.getTimeQuantumId(), null);
				}*/
				sm.put("list", scheduleList);
				mm.put(ob.getTimeQuantumId(), sm);
			}
			weeks.put("weeks", mm);
			weeks.put("date", day);
			weeks.put("dateName", com.budaos.modules.common.DateUtils.getWeekName(day));
			weekList.add(weeks);
		}
		// 返回数据
		returnData.put("timeSlotList", timeSlotList);
		returnData.put("weekList", weekList);
		return R.ok().put(Constant.R_DATA, returnData);
	}

	@Override
	public R queryTrainingRoomList(Map<String, Object> params) {
		List<Map<String,Object>> list = tevglTchScheduleMapper.queryTrainingRoomList(params);
		return R.ok().put(Constant.R_DATA, list);
	}

	/**
	 * 调课
	 * @param tevglTchSchedule
	 * @return
	 */
	@Override
	@Transactional
	public R exchangeSchedule(TevglTchSchedule tevglTchSchedule) {
		if (StrUtils.isEmpty(tevglTchSchedule.getScheduleId())) {
			return R.error("请先选择要调课的记录");
		}
		TevglTchSchedule oldTevglTchSchedule = tevglTchScheduleMapper.selectObjectById(tevglTchSchedule.getScheduleId());
		if (oldTevglTchSchedule == null) {
			return R.error("参数异常");
		}
		String msg = "申请成功，等待审核";
		// TODO 如果已经申请过，且被拒绝了，直接重新更新未待审核状态即可
		TevglTchScheduleHistory tevglTchScheduleHistory = tevglTchScheduleHistoryMapper.selectObjectByOldId(oldTevglTchSchedule.getScheduleId());
		log.debug("是否已经申请过 => {}", tevglTchScheduleHistory);
		if (tevglTchScheduleHistory != null) {
			// 不允许多次提交
			if ("0".equals(tevglTchScheduleHistory.getCheckState())) {
				return R.error("已经提交了申请，请耐心等待审核通过");
			}
			// 被拒绝之后，再次申请
			if ("2".equals(tevglTchScheduleHistory.getCheckState())) {
				TevglTchScheduleHistory h = new TevglTchScheduleHistory();
				h.setScheduleIdLatest(tevglTchScheduleHistory.getScheduleIdLatest());
				h.setReason(null);
				h.setCheckState("0");
				tevglTchScheduleHistoryMapper.update(h);
				return R.ok(msg);
			}
		}
		// 如果仅仅只是更换下教室
		if (!oldTevglTchSchedule.getRoomId().equals(tevglTchSchedule.getRoomId()) && StrUtils.isEmpty(tevglTchSchedule.getSchedule()) && StrUtils.isEmpty(tevglTchSchedule.getTimeQuantumId())) {
			msg = "申请更换教室，请耐心等待管理员审核通过~";
		}
		// TODO 校验指定时间内，目标教室是否被占用
		Map<String, Object> ps = new HashMap<>();
		ps.put("schedule", tevglTchSchedule.getSchedule());
		ps.put("timeQuantumId", tevglTchSchedule.getTimeQuantumId());
		ps.put("roomId", tevglTchSchedule.getRoomId());
		List<TevglTchSchedule> existedList = tevglTchScheduleMapper.selectListByMap(ps);
		log.debug("选择调课至 {} {} => 教室 {}", tevglTchSchedule.getSchedule(), tevglTchSchedule.getTimeQuantumId(), tevglTchSchedule.getRoomId());
		// 如果已经被申请
		if (existedList.stream().anyMatch(a -> "N".equals(a.getState()))) {
			return R.error(tevglTchSchedule.getSchedule() + "教室早已被申请");
		}
		if (existedList.stream().anyMatch(a -> "Y".equals(a.getState()))) {
			return R.error(tevglTchSchedule.getSchedule() + "教室已被占用，请调整更换");
		}
		// 创建新数据，审核通过之后，将状态改为有效，历史表中记录原数据，原纪录删掉
		String uuid = Identities.uuid();
		TevglTchSchedule t = new TevglTchSchedule();
		t.setScheduleId(uuid);
		t.setTeacherId(oldTevglTchSchedule.getTeacherId());
		t.setSubjectId(oldTevglTchSchedule.getSubjectId());
		t.setScheduleState(oldTevglTchSchedule.getScheduleState());
		t.setSchedule(tevglTchSchedule.getSchedule());
		t.setTimeQuantumId(tevglTchSchedule.getTimeQuantumId());
		t.setRoomId(tevglTchSchedule.getRoomId());
		t.setCreateTime(DateUtils.getNowTimeStamp());
		t.setCreateUserId(serviceLoginUtil.getLoginUserId());
		t.setState("N");
		tevglTchScheduleMapper.insert(t);
		// 授课班级关系
		saveScheduleClass(oldTevglTchSchedule, uuid);
		// 历史表入库
		String message = "申请调课至 " + tevglTchSchedule.getSchedule() + " " + getSegmentTimeName(tevglTchSchedule.getTimeQuantumId()) + " " + getRoomName(tevglTchSchedule.getRoomId()) +" 教室";
		TevglTchScheduleHistory history = clone(oldTevglTchSchedule);
		history.setScheduleIdLatest(uuid);
		history.setMessage(message);
		tevglTchScheduleHistoryMapper.insert(history);
		// TODO 校验，时间冲突、教室冲突、老师冲突
		// TODO 现阶段，直接审核通过
		R checkResult = check(uuid, "1", null);
		log.info("现阶段直接默认审核通过，处理结果 => {}", checkResult);
		return R.ok(msg + "（现阶段直接默认审核通过）");
	}
	
	/**
	 * 绑定新授课记录与班级的关系
	 * @param oldTevglTchSchedule
	 * @param uuid
	 */
	private void saveScheduleClass(TevglTchSchedule oldTevglTchSchedule, String uuid) {
		Map<String, Object> map = new HashMap<>();
		map.put("scheduleId", oldTevglTchSchedule.getScheduleId());
		List<TevglTchScheduleClass> list = tevglTchScheduleClassMapper.selectListByMap(map);
		String classId = (list != null && list.size() > 0) ? list.get(0).getClassId() : "";
		TevglTchScheduleClass c = new TevglTchScheduleClass();
		c.setScheduleId(uuid);
		c.setClassId(classId);
		tevglTchScheduleClassMapper.insert(c);
	}

	/**
	 * 将未调整前的数据复制一份，保存至数据库
	 * @param oldTevglTchSchedule
	 * @return
	 */
	private TevglTchScheduleHistory clone(TevglTchSchedule oldTevglTchSchedule) {
		TevglTchScheduleHistory history = new TevglTchScheduleHistory();
		history.setScheduleId(oldTevglTchSchedule.getScheduleId());
		history.setTeacherId(oldTevglTchSchedule.getTeacherId());
		history.setSubjectId(oldTevglTchSchedule.getSubjectId());
		history.setRoomId(oldTevglTchSchedule.getRoomId());
		history.setTimeQuantumId(oldTevglTchSchedule.getTimeQuantumId());
		history.setSchedule(oldTevglTchSchedule.getSchedule());
		history.setScheduleState(oldTevglTchSchedule.getScheduleState());
		history.setCreateUserId(oldTevglTchSchedule.getCreateUserId());
		history.setCreateTime(oldTevglTchSchedule.getCreateTime());
		//history.getUpdateUserId(serviceLoginUtil.getLoginUserId());
		history.setUpdateTime(DateUtils.getNowTimeStamp());
		// 0待审核1通过2拒绝
		history.setCheckState("0");
		return history;
	}

	
	/**
	 * 审核 调课记录
	 *
	 * @param scheduleIdLatest
	 * @param checkState       审核状态(0待审核1通过2未通过)
	 * @param reason           未通过时的原因
	 * @return
	 */
	@Override
	public R check(String scheduleIdLatest, String checkState, String reason) {
		if (StrUtils.isEmpty(scheduleIdLatest) || StrUtils.isEmpty(checkState)) {
			return R.error("必传参数为空");
		}
		TevglTchScheduleHistory tevglTchScheduleHistory = tevglTchScheduleHistoryMapper.selectObjectById(scheduleIdLatest);
		if (tevglTchScheduleHistory == null) {
			return R.error("参数异常");
		}
		if (!"0".equals(tevglTchScheduleHistory.getCheckState())) {
			return R.ok("已处理");
		}
		TevglTchScheduleHistory t = new TevglTchScheduleHistory();
		t.setScheduleIdLatest(scheduleIdLatest);
		t.setCheckUserId(serviceLoginUtil.getLoginUserId());
		switch (checkState) {
			case "1":
				// 审核通过的情况下
				t.setCheckState("1");
				tevglTchScheduleHistoryMapper.update(t);
				// 删除授课表中的原有记录
				tevglTchScheduleMapper.delete(tevglTchScheduleHistory.getScheduleId());
				// 将新记录更新为有效
				TevglTchSchedule ts = new TevglTchSchedule();
				ts.setScheduleId(scheduleIdLatest);
				ts.setState("Y");
				tevglTchScheduleMapper.update(ts);
				break;
			case "2":
				// 审核不通过的情况下
				t.setCheckState("2");
				t.setReason(reason);
				tevglTchScheduleHistoryMapper.update(t);
				break;
			default:
				break;
		}
		return R.ok("处理成功");
	}
	
	/**
	 * 获取指定年月的第一天 
	 * @param yearMonth 必传参数，且格式要求为yyyy-MM
	 * @return yyyy-MM-dd
	 * @apiNote
	 * <ul>
	 *   <li>假如传入 2021-11 则返回2021-11-01</li>
	 * </ul>
	 */
	private static String getFirstDayOfMonth(String yearMonth) {
		String[] arr = yearMonth.split("-");
		int year = Integer.parseInt(arr[0]);
		int month = Integer.parseInt(arr[1]);
		Calendar c = Calendar.getInstance();
		// 设置年份
		c.set(Calendar.YEAR, year);
		// 设置月份 
        c.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = c.getMinimum(Calendar.DATE);
        // 设置日历中月份的最小天数 
        c.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}
	
	/**
     * 获取指定年月的最后一天
     * @param yearMonth 必传参数，且格式要求为yyyy-MM
     * @return yyyy-MM-dd
     * @apiNote
     * 假如传入 2021-02 则返回2021-02-28
     * 假如传入 2021-10 则返回2021-10-31
     * 假如传入 2021-11 则返回2021-11-30
     */
    public static String getLastDayOfMonth(String yearMonth) {
    	String[] arr = yearMonth.split("-");
		int year = Integer.parseInt(arr[0]);
		int month = Integer.parseInt(arr[1]);
		Calendar cal = Calendar.getInstance();
        // 设置年份  
        cal.set(Calendar.YEAR, year);
        // 设置月份  
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        // 设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
	
	/**
	 * 小程序端查询课表
	 * @param params
	 * @param traineeInfo
	 * @return
	 */
	@Override
	public R queryScheduleDataForWx(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo) {
		if (StrUtils.isEmpty(params.getYearMonth())) {
			return R.error("必传参数为空");
		}
		String firstDayOfMonth = getFirstDayOfMonth(params.getYearMonth());
		String lastDayOfMonth = getLastDayOfMonth(params.getYearMonth());
		params.setBeginDate(firstDayOfMonth);
		params.setEndDate(lastDayOfMonth);
		// 判断身份如果是老师（trainee_type貌似没有被实时维护更新）
		if ("4".equals(traineeInfo.getTraineeType())) {
			params.setTeacherId(traineeInfo.getTraineeId());
		} else {
			// 判断身份如果是老师（trainee_type貌似没有被实时维护更新，所以额外判断下）
			TevglTchTeacher tevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(traineeInfo.getTraineeId());
			if (tevglTchTeacher != null) {
				params.setTeacherId(traineeInfo.getTraineeId());	
			} else {
				// 如果没有在任何班级，返回空数据
				String classId = tevglTchClasstraineeMapper.findTheLatestClassId(traineeInfo.getTraineeId());
				if (StrUtils.isEmpty(classId)) {
					return R.ok().put(Constant.R_DATA, new ArrayList<>());
				} else {
					params.setClassId(classId);
				}	
			}
		}
		List<TevglTchSchedule> list = tevglTchScheduleMapper.queryScheduleDataForWeb(params);
		list.stream().forEach(item -> {
			item.setScheduleStateName(item.getScheduleState());
			// 改为斜杠满足小程序端格式
			String[] arr = item.getSchedule().split("-");
			String str = "";
			for (int i = 0; i < arr.length; i++) {
				str += arr[i] + "/";
			}
			item.setSchedule(StrUtils.isNotEmpty(str) ? str.substring(0, str.length() - 1) : item.getSchedule());
		});
		convertUtil.convertDict(list, "scheduleStateName", "scheduleState");
		return R.ok().put(Constant.R_DATA, list);
	}

	
	
	@Override
	public R viewSomeDaySchedule(TevglTchScheduleParams params, TevglTraineeInfo traineeInfo) {
		if (StrUtils.isEmpty(params.getBeginDate())) {
			return R.error("必传参数为空");
		}
		Map<String, Object> otherInfo = new HashMap<String, Object>();
		otherInfo.put("traineeId", traineeInfo.getTraineeId());
		// 查询条件
		params.setEndDate(params.getBeginDate());
		// 判断身份如果是老师（trainee_type貌似没有被实时维护更新）
		if ("4".equals(traineeInfo.getTraineeType())) {
			params.setTeacherId(traineeInfo.getTraineeId());
			otherInfo.put("identity", "老师");
		} else {
			// 判断身份如果是老师（trainee_type貌似没有被实时维护更新，所以额外判断下）
			TevglTchTeacher tevglTchTeacher = tevglTchTeacherMapper.selectObjectByTraineeId(traineeInfo.getTraineeId());
			if (tevglTchTeacher != null) {
				params.setTeacherId(traineeInfo.getTraineeId());
				otherInfo.put("teacherId", tevglTchTeacher.getTeacherId());
				otherInfo.put("identity", "老师");
			} else {
				// 如果没有在任何班级，返回空数据
				String classId = tevglTchClasstraineeMapper.findTheLatestClassId(traineeInfo.getTraineeId());
				if (StrUtils.isEmpty(classId)) {
					return R.ok().put(Constant.R_DATA, new ArrayList<>());
				} else {
					params.setClassId(classId);
				}
				
				otherInfo.put("identity", "学生");
			}
		}
		List<TevglTchSchedule> list = tevglTchScheduleMapper.queryScheduleDataForWeb(params);
		// 查询时间段
		List<TevglTchScheduleBetween> timeSlotList = tevglTchScheduleBetweenMapper.selectListByMap(new HashMap<>());
		timeSlotList.stream().forEach(item -> {
			list.stream().forEach(o -> {
				if (o.getTimeQuantumId().equals(item.getTimeQuantumId())) {
					item.setTevglTchSchedule(o);
				}
			});
		});
		return R.ok().put(Constant.R_DATA, timeSlotList).put("otherInfo", otherInfo);
	}

	@Override
	public R queryScheduleDataV2(Map<String, Object> map) {
		boolean emptyDate = StrUtils.isNull(map.get("beginDate")) || StrUtils.isNull(map.get("endDate"));
		if (emptyDate) {
			return R.error("必传参数为空");
		}
		// 1上课2活动日3放假4自习5休息6其它
		Object type = map.get("type");
		// 以下为必传参数，如果没有，则直接返回空白数据
		Object beginDate = map.get("beginDate");
		Object endDate = map.get("endDate");
		Object roomId = map.get("roomId");
		Object teacherId = map.get("teacherId");
		Object classId = map.get("classId");
		// 获取两个日期之间的天数
		List<String> thisWeekDayList = getDays(beginDate.toString(), endDate.toString());
		// 最终返回数据
		Map<String, Object> returnData = new HashMap<>();
		// 已安排的班级授课数据
		List<TevglTchSchedule> existedClassScheDuleList = new ArrayList<>();
		// 已安排的老师授课数据
		List<TevglTchSchedule> existedTeacherScheDuleList = new ArrayList<>();
		//
		List<TevglTchSchedule> existedRoomScheDuleList = new ArrayList<>();
		// 1.查询时间段
		List<TevglTchScheduleBetween> timeSlotList = tevglTchScheduleBetweenMapper.selectListByMap(new HashMap<>());
		// 作为查询条件，班级和老师不能同时为空！日期更不能为空
		boolean emptyClassId = StrUtils.isNull(map.get("classId")) || StrUtils.isEmpty(map.get("classId").toString());
		boolean emptyTeacherId = StrUtils.isNull(map.get("teacherId")) || StrUtils.isEmpty(map.get("teacherId").toString());
		boolean emptyRoomId = StrUtils.isNull(map.get("roomId")) || StrUtils.isEmpty(map.get("roomId").toString());
		// 假定本周起止日期为 2021-11-08 至 2021-11-14
		//List<String> thisWeekDayList = Arrays.asList("2021-11-08", "2021-11-09", "2021-11-10","2021-11-11","2021-11-12","2021-11-13", "2021-11-14");
		// 2.查询
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		log.debug("当前查询条件 => {}", map);
		// 标识
		boolean flag = "Y".equals(map.get("flag"));
		boolean selectedRoomId = StrUtils.notNull(roomId);
		boolean selectedTeacherId = StrUtils.notNull(teacherId);
		boolean selectedClassId = StrUtils.notNull(teacherId);
		// 查询模式下
		if (!flag) {
			// 前提条件
			// 同一时间内，一个班级只会在一个教室上课
			// 同一时间内，一个老师只会在一个教室上课
			// 同一时间内，一个教室内，只会有一个老师、多个班级在此上课
			// 教室id可能为空，则直接返回空白数据，如果不为空，那么是可以查出相关数据并展示，因为一个教室内：只允许一个老师、单或多个班级同时上课
			// 如果查询条件：班级id不为空，
			// 班级id和老师id不能同时为空，同时为空就查不了数据
			if (emptyRoomId && emptyClassId && emptyTeacherId) {
				log.debug("没有传任何必传参数，返回模板数据");
				returnData.put("timeSlotList", timeSlotList);
				returnData.put("weekList", createDefaultTemplateWeeksV2(thisWeekDayList, timeSlotList));
				return R.ok().put(Constant.R_DATA, returnData);
			}
		}
		// 如果是排课模式中查询
		if (flag) {
			// 如果没有选择任何条件，直接返回
			if (emptyRoomId && emptyClassId && emptyTeacherId) {
				returnData.put("timeSlotList", timeSlotList);
				returnData.put("weekList", createDefaultTemplateWeeksV2(thisWeekDayList, timeSlotList));
				return R.ok().put(Constant.R_DATA, returnData);
			}
			Map<String, Object> ps = new HashMap<String, Object>();
			// 这个时间段，这个教室的授课安排记录
			if (selectedRoomId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				ps.put("roomId", roomId);
				existedRoomScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				convertUtil.convertDict(existedRoomScheDuleList, "scheduleStateName", "scheduleState");
			}
			// 这个时间段，这个老师的授课安排记录
			if (selectedTeacherId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				ps.put("teacherId", teacherId);
				existedTeacherScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				log.debug("老师{}已安排的数据 => {}", teacherId, existedTeacherScheDuleList.size());
				convertUtil.convertDict(existedTeacherScheDuleList, "scheduleStateName", "scheduleState");
			}
			// 这个时间段，这个班级已经在哪些教室上课了
			if (selectedClassId) {
				ps.clear();
				ps.put("beginDate", beginDate);
				ps.put("endDate", endDate);
				String[] split = classId.toString().split(",");
				ps.put("classIdArray", split);
				existedClassScheDuleList = tevglTchScheduleMapper.queryScheduleData(ps);
				log.debug("班级{}已有的授课记录", (Object) split);
				convertUtil.convertDict(existedClassScheDuleList, "scheduleStateName", "scheduleState");
			}
		}
		List<TevglTchSchedule> dataList = tevglTchScheduleMapper.queryScheduleData(map);
		log.debug("根据条件查询 => {}", map);
		log.debug("查询结果 => {}", dataList.size());
		convertUtil.convertDict(dataList, "scheduleStateName", "scheduleState");
		List<Map<String, Object>> weekList = new ArrayList<>();
		for (String day : thisWeekDayList) {
			Map<String, Object> weeks = new HashMap<>();
			Map<String, Object> mm = new HashMap<>();
			for (TevglTchScheduleBetween ob : timeSlotList) {
				Map<String, Object> sm = new HashMap<>();
				boolean disabled = timeOut(day, ob.getBeginTime());
				sm.put("disabled", disabled); // 处理是否允许删除、调课
				// 存放这天，这个时间段的数据
				List<TevglTchSchedule> scheduleList = dataList.stream().filter(tevglTchSchedule -> tevglTchSchedule.getSchedule().equals(day) && tevglTchSchedule.getTimeQuantumId().equals(ob.getTimeQuantumId())).collect(Collectors.toList());
				log.info("{} 的 {}-{} 授课记录{}", day, ob.getBeginTime(), ob.getEndTime(), scheduleList.size());
				// 同一时间内，该教室是否已经被安排了
				List<TevglTchSchedule> thisRoomDayTimeSchduleList = matching(existedRoomScheDuleList, day, ob.getTimeQuantumId());
				if (thisRoomDayTimeSchduleList != null && thisRoomDayTimeSchduleList.size() > 0) {
					log.debug("同一时间内，该教室是否已经被安排了");
					if (scheduleList.size() == 0) {
						scheduleList.addAll(thisRoomDayTimeSchduleList);
					} else {
						thisRoomDayTimeSchduleList.stream().forEach(item -> {
							if (!scheduleList.stream().anyMatch(a -> a.getScheduleId().equals(item.getScheduleId()))) {
								scheduleList.add(item);
							}
						});
					}
					// TODO 一个教室只允许一个班级（如果可能有多个班级的话，注释该代码）
					sm.put("disabled", true);
				}
				// 同一时间内，该老师是否已经被安排了
				List<TevglTchSchedule> thisTeacherDayTimeSchduleList = matching(existedTeacherScheDuleList, day, ob.getTimeQuantumId());
				if (thisTeacherDayTimeSchduleList != null && thisTeacherDayTimeSchduleList.size() > 0) {
					log.debug("同一时间内，该老师是否已经被安排了");
					if (scheduleList.size() == 0) {
						scheduleList.addAll(thisTeacherDayTimeSchduleList);
					} else {
						thisTeacherDayTimeSchduleList.stream().forEach(item -> {
							if (!scheduleList.stream().anyMatch(a -> a.getScheduleId().equals(item.getScheduleId()))) {
								scheduleList.add(item);
							}
						});
					}
				}
				// 同一时间内，该班级是否已经被安排了
				List<TevglTchSchedule> thisClassDayTimeSchduleList = matching(existedClassScheDuleList, day, ob.getTimeQuantumId());
				if (thisClassDayTimeSchduleList != null && thisClassDayTimeSchduleList.size() > 0) {
					log.debug("同一时间内，该班级是否已经被安排了");
					if (scheduleList == null || scheduleList.size() == 0) {
						scheduleList.addAll(thisClassDayTimeSchduleList);
					} else {
						thisClassDayTimeSchduleList.stream().forEach(item -> {
							if (!scheduleList.stream().anyMatch(a -> a.getScheduleId().equals(item.getScheduleId()))) {
								scheduleList.add(item);
							}
						});
					}
					sm.put("disabled", true);
				}
				// 是否有删除、调课的图标
				if (scheduleList != null && scheduleList.size() > 0) {
					scheduleList.stream().forEach(item -> {
						item.setHasDeleteIcon(!disabled);
					});
				}
				// 只要有一个不是自习
				if (!disabled) {
					boolean allMatch = scheduleList.stream().allMatch(a -> "4".equals(a.getScheduleState()));
					if (!allMatch) {
						sm.put("disabled", true);
					}
				}
				sm.put("list", scheduleList);
				mm.put(ob.getTimeQuantumId(), sm);
			}
			weeks.put("weeks", mm);
			weeks.put("date", day);
			weeks.put("dateName", com.budaos.modules.common.DateUtils.getWeekName(day));
			weekList.add(weeks);
		}
		// 返回数据
		returnData.put("timeSlotList", timeSlotList);
		returnData.put("weekList", weekList);
		return R.ok().put(Constant.R_DATA, returnData);
	}
	
	/**
	 * 如果已经过了时间，则禁止选择，否在不禁止
	 * @param yyyyMMdd 示例：2021-12-23
	 * @param beginTime 10:00
	 * @return true 表示禁选
	 */
	private boolean timeOut(String yyyyMMdd, String beginTime) {
		if (StrUtils.isNotEmpty(yyyyMMdd) && StrUtils.isNotEmpty(beginTime)) {
			String targetTime = yyyyMMdd + " " + beginTime + ":00";
			return targetDateBeforeNowDate(targetTime);
		}
		return true;
	}
}
