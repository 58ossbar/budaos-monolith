package com.budaos.utils.tool;

import com.alibaba.fastjson.JSONObject;
import com.budaos.utils.constants.Constant;

import java.util.Map;

/**
 * resp 工具类
 * <p>
 */
public class RespUtils {

    /**
     * resp转json
     *
     * @param resp
     * @return
     */
    public static JSONObject json(Resp resp) {
        JSONObject json = new JSONObject();
        json.put("no", resp.getNo());
        json.put("code", resp.getCode());
        json.put("msg", resp.getMsg());
        for (Map.Entry<String, Object> entry : resp.getAttr().entrySet()) {
            if (entry.getValue() != null)
                json.put(entry.getKey(), entry.getValue());
        }
        return json;
    }

    /**
     * 成功RESP
     *
     * @param data
     * @return
     */
    public static JSONObject success(Object data) {
    	Resp resp = new Resp(data);
        return json(resp);
    }

    /**
     * 失败resp
     *
     * @param data
     * @param msg
     * @return
     */
    public static JSONObject fail(Object data, String msg) {
    	Resp resp = new Resp(data);
        resp.setCode(Constant.CODE_FAIL).setMsg(msg);
        return json(resp);
    }

    /**
     * 异常resp
     *
     * @param data
     * @param msg
     * @return
     */
    public static JSONObject error(Object data, String msg) {
    	Resp resp = new Resp(data);
        resp.setCode(Constant.CODE_ERROR).setMsg(msg);
        return json(resp);
    }


}
