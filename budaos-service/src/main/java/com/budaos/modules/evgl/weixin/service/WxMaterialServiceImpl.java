package com.budaos.modules.evgl.weixin.service;

import com.alibaba.fastjson.JSON;
import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.common.DateUtils;
import com.budaos.modules.common.enums.WxMaterialEnum;
import com.budaos.modules.evgl.weixin.api.WxMaterialService;
import com.budaos.modules.evgl.weixin.params.MaterialParams;
import com.budaos.modules.evgl.weixin.vo.MaterialResponseVo;
import com.budaos.modules.sys.api.TsysSettingsService;
import com.budaos.modules.sys.domain.TsysSettings;
import com.budaos.utils.constants.Constant;
import com.budaos.utils.tool.HttpUtils;
import com.budaos.utils.tool.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huj
 * @create 2021-12-30 10:10
 * @email 1552281464@qq.com
 */
@Service
public class WxMaterialServiceImpl implements WxMaterialService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TsysSettingsService tsysSettingsService;

    /**
     * 获取素材列表
     *
     * @param params
     * @return
     * @apiNote 微信返回的数据
     * {"item":[{"media_id":"NLqoRU2OWvmZ1GD6Ds-eXgulfyjlnc0-rAk_ShTYDzE","name":"01cfefec285b4ce4a7477a53a31fecff.jpg","update_time":1640766539,"url":"https:\/\/mmbiz.qpic.cn\/mmbiz_jpg\/pkf7iaN3tHMFbibdxv0qKaMKCK2rmj8qfF7vOia8KFlEzjT1QSNRkHRH0PSTGiap8aJfrhzodJAbFJIvRmiczCY1TlA\/0?wx_fmt=jpeg","tags":[]},{"media_id":"NLqoRU2OWvmZ1GD6Ds-eXhm_Tkx3Zgv0CNsvDlpDcjo","name":"d0b293d6dd0246fd8b231bf032681377.jpeg","update_time":1640762494,"url":"https:\/\/mmbiz.qpic.cn\/mmbiz_png\/pkf7iaN3tHMFbibdxv0qKaMKCK2rmj8qfF9kozuCDnz7z0f0SOscGX9vGYib3gXKxA2kEZlia3WhF9LoaZUMiaxDt8Q\/0?wx_fmt=png","tags":[]}],"total_count":2,"item_count":2}
     */
    @Override
    public R batchgetMaterial(MaterialParams params) {
        if (StrUtils.isEmpty(params.getType())) {
            params.setType(WxMaterialEnum.TYPE_IMAGE.getCode());
        }
        if (params.getOffset() == null) {
            params.setOffset(0);
        }
        if (params.getCount() == null) {
            params.setCount(1);
        }
        log.debug("查询条件 => {}", params);
        String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + getAccessToken();
        Map<String, String> map = new HashMap<>();
        map.put("type", params.getType());
        map.put("offset", String.valueOf(params.getOffset()));
        map.put("count", String.valueOf(params.getCount()));
        String responseString = null;
        MaterialResponseVo materialResponseVo = null;
        try {
        	responseString = HttpUtils.sendPost(url, JSON.toJSONString(map));
        	log.info("请求地址 => {}", url);
            log.info("响应结果 => {}", responseString);
		} catch (Exception e) {
			log.error("从微信那边获取数据失败", e);
		}
        if (responseString != null) {
        	// 测试数据
        	//responseString = "{\"item\":[{\"media_id\":\"NLqoRU2OWvmZ1GD6Ds-eXgulfyjlnc0-rAk_ShTYDzE\",\"name\":\"01cfefec285b4ce4a7477a53a31fecff.jpg\",\"update_time\":1640766539,\"url\":\"https:\\/\\/mmbiz.qpic.cn\\/mmbiz_jpg\\/pkf7iaN3tHMFbibdxv0qKaMKCK2rmj8qfF7vOia8KFlEzjT1QSNRkHRH0PSTGiap8aJfrhzodJAbFJIvRmiczCY1TlA\\/0?wx_fmt=jpeg\",\"tags\":[]},{\"media_id\":\"NLqoRU2OWvmZ1GD6Ds-eXhm_Tkx3Zgv0CNsvDlpDcjo\",\"name\":\"d0b293d6dd0246fd8b231bf032681377.jpeg\",\"update_time\":1640762494,\"url\":\"https:\\/\\/mmbiz.qpic.cn\\/mmbiz_png\\/pkf7iaN3tHMFbibdxv0qKaMKCK2rmj8qfF9kozuCDnz7z0f0SOscGX9vGYib3gXKxA2kEZlia3WhF9LoaZUMiaxDt8Q\\/0?wx_fmt=png\",\"tags\":[]}],\"total_count\":2,\"item_count\":2}";
            materialResponseVo = JSON.parseObject(responseString, MaterialResponseVo.class);
            if (materialResponseVo != null && materialResponseVo.getItem() != null && materialResponseVo.getItem().size() > 0) {
            	materialResponseVo.getItem().stream().forEach(ob -> {
            		ob.setUpdateTimeStr(DateUtils.converTimestampToStr(ob.getUpdateTime()));
            	});
            }
        }
        return R.ok().put(Constant.R_DATA, materialResponseVo);
    }

    /**
     * 从系统设置表中获取token
     * @return
     */
    @Override
    public String getAccessToken() {
        Map<String, Object> map = new HashMap<>();
        map.put("settingType", "official");
        List<TsysSettings> tsysSettingsList = tsysSettingsService.selectListByMap(map);
        if (tsysSettingsList != null && tsysSettingsList.size() > 0) {
            return tsysSettingsList.get(0).getSettingValue();
        }
        return "";
    }

    /**
     * 删除永久素材
     * @param media_id
     * @return
     */
	@Override
	public R delMaterial(String media_id) {
		if (StrUtils.isEmpty(media_id)) {
            return R.error("必传参数为空");
        }
        String httpUrl = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=" + getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("media_id", media_id);
        String responseString = HttpUtils.sendPost(httpUrl, JSON.toJSONString(map));
        log.debug("响应结果 => {}", responseString); // 成功的情况下 {"errcode":0,"errmsg":"ok"}
        if (responseString != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> res = (Map<String, Object>) JSON.parse(responseString);
            if (!res.get("errcode").equals(0)) {
                return R.error("删除失败");
            }
        }
        return R.ok("删除成功");
	}

}
