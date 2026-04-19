package com.budaos.modules.evglsp.rest.controller.signup;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DictService;
import com.budaos.modules.evgl.eao.api.TeaoFdOrderService;
import com.budaos.modules.evgl.eao.api.TeaoTraineeSignupService;
import com.budaos.modules.evgl.medu.sys.api.TmeduApiTokenService;
import com.budaos.modules.evgl.medu.sys.domain.TmeduApiToken;
import com.budaos.modules.evgl.tch.api.TevglTchClassService;
import com.budaos.utils.constants.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/api/order")
@Api(value = "订单报名相关服务接口", tags = { "订单相关服务接口" })
public class ApiSignUpController {

    @Autowired
    private TmeduApiTokenService tmeduApiTokenService;
    @Autowired
    private TeaoTraineeSignupService teaoTraineeSignupService;
    @Autowired
    private TevglTchClassService tevglTchClassService;
    @Autowired
    private TeaoFdOrderService teaoFdOrderService;
    @Autowired
    private DictService dictService;
    // 报名时默认支付的金额
    @Value("${weixin.paymentAmount}")
    private String paymentAmount;
    
    /**
     * 获取开放的班级
     * @param params
     * @return
     */
    @RequestMapping("/queryClassList")
    public R queryClassList(@RequestParam Map<String, Object> params) {
    	params.put("sidx", "t1.update_time ");
    	params.put("order", "desc, t1.create_time desc");
    	params.put("classState", "1"); // 1开放，3授课，4完成
    	List<Map<String,Object>> list = tevglTchClassService.selectSimpleListMap(params);
    	return R.ok().put(Constant.R_DATA, list);
    }
    
    /**
     * 获取字典 学历
     * @param params
     * @return
     */
    @RequestMapping("/getTraineeEducation")
    public R getTraineeEducation(@RequestParam Map<String, Object> params) {
    	List<Map<String, Object>> dictList = dictService.getDictList("trainee_education");
    	return R.ok().put(Constant.R_DATA, dictList);
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
    @PostMapping("signup")
    @ApiOperation(value = "报名", notes = "<b>参数说明<b>:<br/>" + "token：用户token，必须<br/>" + "formid：formid，必须<br/>"
            + "name：姓名，必须<br/>" + "education：学历，必须<br/>" + "qq：qq号码，必须<br/>" + "orgid：教育中心，必须<br/>"
            + "classid：班级，必须<br/>" + "<b>返回值说明</b>:<br/>" + "code:服务状态，401: 无效的token,0：查询成功<br/><br/>"
            + "ofid：订单id，必须<br/>" + "}<br/>")

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "formid", value = "formid", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "姓名", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "education", value = "学历", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "qq", value = "QQ号", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "orgid", value = "教育中心", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "classid", value = "班级", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "ispx", value = "是否参加培训(Y参与，其他不参与)", required = true),})
    public R signUp(String token, String formid, String name, String education, String qq, String orgid,
                    String classid, String ispx, String mobile, String wechatNumber) {
        return teaoFdOrderService.signUp(token, formid, name, education, qq, orgid, classid, ispx, paymentAmount, mobile, wechatNumber);
    }

    /**
     * 获取订单支付参数
     *
     * @param token
     * @return
     */
    @PostMapping("payparam")
    @ApiOperation(value = "订单支付参数", notes = "<b>参数说明<b>:<br/>" + "token：客户会话token，必须<br/>" + "ofid：'订单ID'，必须<br/>"
            + "}<br/>" + "<b>返回值说明</b>:<br/>" + "code:服务状态，-1：申请失败，0：申请成功，<br/>" + "payinfo{"
            + "timeStamp：时间戳,当code为0时返回<br/>" + "nonceStr：随机串,当code为0时返回<br/>" + "package：预支付ID,当code为0时返回<br/>"
            + "signType：签名类型,当code为0时返回<br/>" + "paySign：签名,当code为0时返回<br/>" + "}<br/>")

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "客户会话token", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "ofid", value = "订单ID", required = true), })
    public R orderPayParam(String token, String ofid) {
		/*TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if (te == null) {
		    return R.error(401, "Invalid token");
		}
		// 统一订单，获取预支付ID
		String prepayId = weiXPayService.unifiedOrder(ofid);
		Map<String, String> signParam = new HashMap<String, String>();
		Long timeStamp = DateUtils.getNowDate().getTime();
		String nonceStr = Identities.uuid();
		if (prepayId == null || prepayId.length() == 0) {
		    return R.error(-1, "查询失败");
		}
		String packages = "prepay_id=" + prepayId;
		String signType = WXPayConstants.MD5;
		signParam.put("appId", myWxPayConfig.getAppID());
		signParam.put("timeStamp", timeStamp.toString());
		signParam.put("nonceStr", nonceStr);
		signParam.put("package", packages);
		signParam.put("signType", signType);
		String paySign = "";
		try {
		    paySign = WXPayUtil.generateSignature(signParam, myWxPayConfig.getKey(), WXPayConstants.SignType.MD5);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		signParam.remove("appId");
		signParam.put("paySign", paySign);
		return R.ok("查询成功").put("payinfo", signParam);*/
    	return teaoFdOrderService.orderPayParam(token, ofid);
    }
    
	
    /**
	 * 退款
	 * 
	 * @param token
	 * @return
	 */
	@PostMapping("applyrefund")
	@ApiOperation(value = "申请退款", notes = "<b>参数说明<b>:<br/>" + "token：客户会话token，必须<br/>" + "ofid：'订单ID'，必须<br/>"
			+ "reason：'申请退款原因'，必须<br/>" + "}<br/>" + "<b>返回值说明</b>:<br/>" + "code:服务状态，-1：申请失败，0：申请成功，<br/>"

	)

	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "客户会话token", required = true),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "ofid", value = "订单ID", required = true),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "reason", value = "申请退款原因", required = true), })
	public R applyRefund(String token, String ofid, String reason) {
		/*TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if (te == null) {
			return R.error(401, "Invalid token");
		}
		TeaoFdOrder orderInfo = teaoFdOrderService.selectObjectById(ofid);
		// 如果该订单状态不是退款中，则不允许退款，需要先申请退款
		if (orderInfo == null || !"02".equals(orderInfo.getOfState())
				|| (orderInfo.getOfRefundState() != null && orderInfo.getOfRefundState().length() > 0)) {
			return R.error(-1, "订单状态异常");
		}
		orderInfo.setOfApplyrefundReason(reason);
		orderInfo.setOfRefundState("01");
		orderInfo.setOfApplyrefundTime(DateUtils.getNowTimeStamp());
		orderInfo.setRbSeriano(seriaNoUtils.getSeriaNoWithRandom(MyRedisKeyConfig.REFUND_SERAINO));
		teaoFdOrderService.update(orderInfo);
		return R.ok("申请成功");*/
		return teaoFdOrderService.applyRefund(token, ofid, reason);
	}
	
	/**
	 * 订单列表
	 * 
	 * @param token
	 * @param state
	 * @return
	 */
	@PostMapping("getorderlist")
	@ApiOperation(value = "订单列表", notes = "<b>参数说明<b>:<br/>" + "token：客户会话token，必须<br/>" + "state：'订单状态'，必须<br/>"
			+ "}<br/>" + "<b>返回值说明</b>:<br/>" + "code:服务状态，-1：申请失败，0：申请成功，<br/>" + "orderlist : 订单列表"

	)

	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "客户会话token", required = true),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "state", value = "订单状态", required = true) })
	public R getOrderList(String token, String state) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if (te == null) {
			return R.error(401, "Invalid token");
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("traineeId", te.getUserId());
		if (state != null && !state.isEmpty()) {
			if ("03".equals(state)) {
				m.put("ofRefundState", "01");
			} else {
				m.put("ofState", state);
			}
		}

		List<Map<String, Object>> list = teaoFdOrderService.selectListByState(m);

		return R.ok().put("orderlist", list);

	}
	
	/**
	 * 订单信息
	 * 
	 * @param token
	 * @param state
	 * @return
	 */
	@PostMapping("getorder")
	@ApiOperation(value = "订单信息", notes = "<b>参数说明<b>:<br/>" + "token：客户会话token，必须<br/>" + "orderId：'订单ID'，必须<br/>"
			+ "}<br/>" + "<b>返回值说明</b>:<br/>" + "code:服务状态，-1：申请失败，0：申请成功，<br/>" + "orderlist : 订单列表"

	)

	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "token", value = "客户会话token", required = true),
			@ApiImplicitParam(paramType = "form", dataType = "string", name = "orderid", value = "订单ID", required = true) })
	public R getOrder(String token, String orderid) {
		TmeduApiToken te = tmeduApiTokenService.selectTokenByToken(token);
		if (te == null) {
			return R.error(401, "Invalid token");
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("orderId", orderid);

		List<Map<String, Object>> list = teaoFdOrderService.selectListByState(m);

		return R.ok().put("order", list.get(0));

	}
}
