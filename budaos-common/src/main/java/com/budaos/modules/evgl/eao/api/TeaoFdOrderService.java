package com.budaos.modules.evgl.eao.api;

import com.budaos.common.exception.BudaosException;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.evgl.eao.domain.TeaoFdOrder;

import java.util.List;
import java.util.Map;

public interface TeaoFdOrderService {

    void save(TeaoFdOrder teaoFdOrder) throws BudaosException;

    void update(TeaoFdOrder teaoFdOrder) throws BudaosException;
    
    TeaoFdOrder selectObjectById(String id);
    
    List<Map<String, Object>> selectListByState(Map<String, Object> map);

    TeaoFdOrder selectObjectBySeriano(String id);
    
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
     * @param paymentAmount 报名时，默认支付的金额
     * @param mobile 非必传参数，手机号码，用户手动输入留下的
     * @param wechatNumber 非必传参数，微信号码
     * @return
     */
    R signUp(String token, String formid, String name, String education, String qq, String orgid, String classid, String ispx, String paymentAmount, String mobile, String wechatNumber);
    
    /**
     * 获取订单支付参数
     * @param token
     * @param ofid
     * @return
     */
    R orderPayParam(String token, String ofid);
    
    /**
     * 退款
     * @param token
     * @param ofid
     * @param reason
     * @return
     */
    R applyRefund(String token, String ofid, String reason);
    
    /**
     * 查询订单列表
     * @param params
     * @return
     */
    R query(Map<String, Object> params);
    
    List<TeaoFdOrder> selectListByMap(Map<String, Object> params);
}
