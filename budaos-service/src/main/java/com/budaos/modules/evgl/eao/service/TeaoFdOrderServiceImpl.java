package com.budaos.modules.evgl.eao.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.common.utils.ConvertUtil;
import com.budaos.common.utils.PageUtils;
import com.budaos.common.utils.Query;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.annotation.NoRepeatSubmit;
import com.budaos.modules.common.config.MyRedisKeyConfig;
import com.budaos.modules.common.config.SeriaNoUtils;
import com.budaos.modules.evgl.eao.api.TeaoFdOrderService;
import com.budaos.modules.evgl.eao.api.TeaoTraineeSignupService;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeSignup;
import com.budaos.modules.evgl.eao.persistence.TeaoFdOrderMapper;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.medu.sys.persistence.TmeduApiTokenMapper;
import com.budaos.modules.evgl.tch.api.TevglTchClassService;
import com.budaos.modules.evgl.weixin.api.WxPayService;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.DateUtils;
import com.budaos.utils.tool.Identities;
import com.budaos.utils.tool.StrUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeaoFdOrderServiceImpl implements TeaoFdOrderService {

	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private TeaoFdOrderMapper teaoFdOrderMapper;
    @Autowired
    private TmeduApiTokenMapper tmeduApiTokenMapper;
    @Autowired
    private TeaoTraineeSignupService teaoTraineeSignupService;
    @Autowired
    private TevglTchClassService tevglTchClassService;
    @Autowired
    private SeriaNoUtils seriaNoUtils;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ConvertUtil convertUtil;
    

	@Override
	public R query(Map<String, Object> params) {
		// 构建查询条件对象Query
		if(params.get("sofTime") != null && params.get("sofTime").toString().length() > 0){
			params.put("sofTime",params.get("sofTime")+" 00:00:00");
		}
		if(params.get("eofTime") != null && params.get("eofTime").toString().length() > 0){
			params.put("eofTime",params.get("eofTime")+" 23:59:59");
		}
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TeaoFdOrder> teaoFdOrderList = teaoFdOrderMapper.selectListByMap(query);
		PageUtils pageUtil = new PageUtils(teaoFdOrderList,query.getPage(),query.getLimit());
		convertUtil.convertOrgId(teaoFdOrderList, "orgId");
		Map<String,String> map = new HashMap<>();
		map.put("spayment", "tpayment");
		map.put("ofState", "of_state"); // 订单状态
		map.put("signupType", "signup_type"); // 报名类型
		convertUtil.convertDict(teaoFdOrderList, map);
		return R.ok().put(Constant.R_DATA, pageUtil);
	} 
    
    @Override
    public void save(TeaoFdOrder teaoFdOrder) throws BudaosException {
        teaoFdOrder.setOrderId(Identities.uuid());
        teaoFdOrder.setCreateTime(DateUtils.getNowTimeStamp());
        teaoFdOrderMapper.insert(teaoFdOrder);
    }
    

	@Override
	public void update(TeaoFdOrder teaoFdOrder) throws BudaosException {
		teaoFdOrder.setUpdateTime(DateUtils.getNowTimeStamp());
		teaoFdOrderMapper.update(teaoFdOrder);
	}

    @Override
    public TeaoFdOrder selectObjectById(String id) {
        return teaoFdOrderMapper.selectObjectById(id);
    }
    
    @Override
    public List<Map<String, Object>> selectListByState(Map<String, Object> map){
		return teaoFdOrderMapper.selectListByState(map);
	}

	@Override
	public TeaoFdOrder selectObjectBySeriano(String id) {
		return teaoFdOrderMapper.selectObjectBySeriano(id);
	}

	/**
	 * 报名
	 * @param token
	 * @param formid
	 * @param name
	 * @param education
	 * @param qq
	 * @param orgid
	 * @param classid
	 * @param ispx
	 * @return
	 */
	@Override
	@NoRepeatSubmit
	public R signUp(String token, String formid, String name, String education, String qq, String orgid, String classid, String ispx, String paymentAmount, String mobile, String wechatNumber) {
		TmeduApiToken te = tmeduApiTokenMapper.selectTokenByToken(token);
        if (te == null) {
            return R.error(401, "Invalid token");
        }
        // 控制下，如果是报的同一个班级，不允许再次报名
        List<String> list = teaoFdOrderMapper.findObjectByTraineeIdAndClassId(te.getUserId(), classid);
        if (list.size() > 0) {
            return  R.error("该班级你已报名，无需重复报名，请去我的订单中查看~");
        }
		TeaoTraineeSignup signUp = new TeaoTraineeSignup();
        signUp.setClassId(classid);
        signUp.setOrgId(orgid);
        signUp.setSignupQq(qq);
        signUp.setSignupTelphone(te.getMobile());
        signUp.setTraineeId(te.getUserId());
        signUp.setChannel("2");
        if("Y".equals(ispx)) {
            signUp.setIspx(ispx);
        }else {
            signUp.setIspx("N");
        }
        paymentAmount = StrUtils.isEmpty(paymentAmount) ? "1" : paymentAmount;
        signUp.setSignupTime(DateUtils.getNowTimeStamp());
        signUp.setMobile(mobile);
        signUp.setWechatNumber(wechatNumber);
        teaoTraineeSignupService.save(signUp);
        TeaoFdOrder tmeOrder = new TeaoFdOrder();
        tmeOrder.setOfSeriano(seriaNoUtils.getSeriaNoWithRandom(MyRedisKeyConfig.ORDER_SERAINO));
        tmeOrder.setClassId(classid);
        tmeOrder.setOrgId(orgid);
        tmeOrder.setOfName(tevglTchClassService.selectObjectById(classid).getClassName());
        tmeOrder.setOfLinkman(name);
        tmeOrder.setOfTelphone(te.getMobile());
        tmeOrder.setTraineeId(te.getUserId());
        tmeOrder.setCreateUserId(te.getUserId());
        tmeOrder.setOfTime(DateUtils.getNowTimeStamp());
        tmeOrder.setOrderReceivable(new BigDecimal(paymentAmount));
        tmeOrder.setOfState("01");
        tmeOrder.setIspx(signUp.getIspx());
        tmeOrder.setSpayment("01");
        tmeOrder.setSpaymentper(100L);
        tmeOrder.setSrefund("Y");
        tmeOrder.setSignupType("2");
        this.save(tmeOrder);
        log.info("默认支付的金额：" + paymentAmount);
        return R.ok().put("ofid", tmeOrder.getOrderId());
	}
	
	/**
     * 获取订单支付参数
     * @param token
     * @param ofid
     * @return
     */
	@Override
    public R orderPayParam(String token, String ofid) {
		TmeduApiToken te = tmeduApiTokenMapper.selectTokenByToken(token);
		if (te == null) {
		    return R.error(401, "Invalid token");
		}
		TeaoFdOrder order = teaoFdOrderMapper.selectObjectById(ofid);
		if (order == null) {
			return R.error("无效的订单");
		}
		TmeduApiToken tokenEntity = tmeduApiTokenMapper.selectTokenByUserId(order.getCreateUserId());
		if (tokenEntity == null) {
			return R.error("无效的用户信息");
		}
		Map<String, String> map = null;
		try {
			map = wxPayService.gotoPay(order.getOfSeriano(), order.getOrderReceivable(), tokenEntity.getOpenid(), null, 1);
		} catch (Exception e) {
			log.error("微信支付失败", e);
		}
		return R.ok("查询成功").put("payinfo", map);
	}
	
	/**
     * 退款
     * @param token
     * @param ofid
     * @param reason
     * @return
     */
    public R applyRefund(String token, String ofid, String reason) {
    	TmeduApiToken te = tmeduApiTokenMapper.selectTokenByToken(token);
		if (te == null) {
			return R.error(401, "Invalid token");
		}
		TeaoFdOrder orderInfo = teaoFdOrderMapper.selectObjectById(ofid);
		// 如果该订单状态不是退款中，则不允许退款，需要先申请退款
		if (orderInfo == null || !"02".equals(orderInfo.getOfState())
				|| (orderInfo.getOfRefundState() != null && orderInfo.getOfRefundState().length() > 0)) {
			return R.error(-1, "订单状态异常");
		}
		orderInfo.setOfApplyrefundReason(reason);
		orderInfo.setOfRefundState("01");
		orderInfo.setOfApplyrefundTime(DateUtils.getNowTimeStamp());
		orderInfo.setRbSeriano(seriaNoUtils.getSeriaNoWithRandom(MyRedisKeyConfig.REFUND_SERAINO));
		this.update(orderInfo);
		return R.ok("申请成功");
    }

    @Override
    public List<TeaoFdOrder> selectListByMap(Map<String, Object> map) {
        return teaoFdOrderMapper.selectListByMap(map);
    }
}
