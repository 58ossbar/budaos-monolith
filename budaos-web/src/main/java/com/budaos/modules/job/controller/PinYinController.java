package com.budaos.modules.job.controller;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.core.common.cbsecurity.log.SysLog;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sys/pinyin")
public class PinYinController {
	@GetMapping("topinyin")
	@SysLog("汉字拼音查询接口")
	public R toPinYin(@RequestParam("hz") String hz) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		try {
			String[] pys = PinyinHelper.toHanYuPinyinString(hz, format, ",", true).split(",");
			String qp = "";
			String jp = "";
			for (String p : pys) {
				String first = (p.charAt(0) + "").toUpperCase();
				jp += first;
				qp += first + p.substring(1);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("qp", qp);
			map.put("jp", jp);
			return R.ok().put("data", map);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			return R.error();
		}
	}
}
