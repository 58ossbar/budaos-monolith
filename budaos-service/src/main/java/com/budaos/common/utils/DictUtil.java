package com.budaos.common.utils;

import com.budaos.modules.job.persistence.TsysDictMapper;
import com.budaos.modules.sys.domain.TsysDict;
import com.budaos.utils.tool.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title:业务字典辅助类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company:budaos.co.,ltd
 * </p>
 *
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Component
public class DictUtil {
	
	@Autowired
	private TsysDictMapper tsysDictMapper;

	public DictUtil() {
	}

	/**
	 * 按内部值查找显示值
	 * 
	 * @param val
	 * @param codeName
	 * @return
	 * @author
	 * @since 2017-8-9
	 */
	public String getKeyByVal(String codeType, String val) {
		Map<String, List<TsysDict>> parsed = initIfEmpty();

		if (StrUtils.isNull(val)) {
			return val;
		}

		List<TsysDict> list = parsed.get(codeType);

		if (list == null) {
			return null;
		}

		for (TsysDict p : list) {
			if (p.getDictCode().equals(val)) {
				return p.getDictValue();
			}
		}

		return null;
	}

	/**
	 * 按显示值查找内部值
	 *
	 * @param key
	 * @param codeName
	 * @return
	 * @author
	 * @since 2017-8-9
	 */
	public String getValByKey(String codeType, String key) {
		Map<String, List<TsysDict>> parsed = initIfEmpty();

		if (StrUtils.isNull(key)) {
			return key;
		}

		List<TsysDict> list = parsed.get(codeType);

		if (list == null) {
			return null;
		}

		for (TsysDict p : list) {
			if (p.getDictValue().equals(key)) {
				return p.getDictCode();
			}
		}

		return null;
	}

	/**
	 * 取某一类型的编码
	 *
	 * @param type
	 * @return
	 */
	public List<TsysDict> getByType(String type) {
		Map<String, List<TsysDict>> parsed = initIfEmpty();

		List<TsysDict> list = parsed.get(type);

		return list;
	}

	/**
	 * 将所有编码按类型分类
	 *
	 * @param params
	 * @return
	 */
	private Map<String, List<TsysDict>> initIfEmpty() {
		List<TsysDict> params = tsysDictMapper.selectAllTsysDict();
		Map<String, List<TsysDict>> parsed = new HashMap<String, List<TsysDict>>();
		String lang = LocaleContextHolder.getLocale().getLanguage();// 当前请求的语种
		for (TsysDict p : params) {
			// 只取字典类型的，不取目录类型的
			if (!"2".equals(p.getDictSort())) {
				continue;
			}
			String type = p.getDictType();
			List<TsysDict> list = parsed.get(type);

			if (list == null) {
				list = new ArrayList<TsysDict>();
			}
			TsysDict dict = p.clone();
			String value = dict.getDictValue();
			if (value.indexOf(lang) >= 0) {// 如果有语种标识，则取当前语言，否则使用当前值

				String[] strs = value.split(",");
				for (String v : strs) {
					if (v.indexOf(lang) == 0) {
						v = v.replace(lang + "_", "");
						dict.setDictValue(v);
						break;
					}
				}
			}
			list.add(dict);
			parsed.put(type, list);
		}

		return parsed;
	}

}
