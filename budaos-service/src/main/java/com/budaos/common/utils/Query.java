package com.budaos.common.utils;

import com.budaos.utils.tool.ReflectUtil;
import com.budaos.utils.tool.StrUtils;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title:查询参数Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//当前页码
    private int page;
    //每页条数
    private int limit;
    
    String[] sqlFilter = new String[] {"SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "TRUNCATE", "DECLARE", "XP_CMDSHELL", "/ADD", "ADD", "NET USER", "EXEC", "EXECUTE", "0X"};

    public Query(Map<String, Object> params){
    	//去除查询参数中两头的空格
        this.putAll(params);

        //分页参数
        if(params.get("pageNum") != null) {
        	this.page = Integer.parseInt(params.get("pageNum").toString());
        }else if(params.get("page") != null) {
        	this.page = Integer.parseInt(params.get("page").toString());
        }else {
        	this.page = 1;
        }
        if(params.get("pageSize") != null) {
        	this.limit = Integer.parseInt(params.get("pageSize").toString());
        }else if(params.get("limit") != null) {
        	this.limit = Integer.parseInt(params.get("limit").toString());
        }else {
        	this.limit = 10;
        }
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
        this.put("pageNum", page);
        this.put("pageSize", limit);

        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）把对象属性转换成表字段
        String sidx = ReflectUtil.propertyToField((String)params.get("sidx"));
        String order = ReflectUtil.propertyToField((String)params.get("order"));
        if(StringUtils.isNotBlank(sidx) && !checkSql(sidx)){
            this.put("sidx", sidx);
        }
        if(StringUtils.isNotBlank(order) && !checkSql(order)){
            this.put("order", order);
        }

    }

    private boolean checkSql(String sql) {
    	if(StrUtils.isEmpty(sql)) {
    		return false;
    	}
    	for(String s : sqlFilter) {
    		if(sql.indexOf(s.toUpperCase()) >= 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
