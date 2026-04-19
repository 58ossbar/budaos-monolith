package com.budaos.modules.job.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.budaos.modules.sys.api.UserService;
import com.budaos.modules.sys.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="用户接口")
@Service
public class UserServiceImpl implements UserService {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 保存用户
	 * 
	 * @param user
	 * @return 受影响的行数
	 */
	@ApiOperation(value = "保存用户", 
		notes = "<b>参数说明<b>:<br/>"
	    		+ "&emsp;id:用户id<br/>"
	    		+ "&emsp;name:用户姓名<br/>"
	    		+ "&emsp;age:用户年龄<br/>"
	    		+ "<b>返回值说明</b>:<br/>"
	    		+ "&emsp;code:服务状态，-1：失败，0：成功<br/>"
	    		+ "&emsp;msg:信息，当code为0时返回<br/>"
		)
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "form", dataType = "string", name = "id", value = "用户ID", required = false),
		@ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "用户姓名", required = false),
		@ApiImplicitParam(paramType = "form", dataType = "string", name = "age", value = "用户年龄", required = false),
	})
	@RequestMapping("user")
	@SentinelResource("user")
	public Integer save(User user) {
		log.info(user.toString());
		// 假装保存成功返回1
		return 1;
	}

}
