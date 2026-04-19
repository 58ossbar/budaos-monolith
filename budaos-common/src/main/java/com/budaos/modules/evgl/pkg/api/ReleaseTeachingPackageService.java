package com.budaos.modules.evgl.pkg.api;

import com.alibaba.fastjson.JSONObject;
import com.budaos.core.baseclass.domain.R;

/**
 * 发布教学包专用接口
 * @author huj
 *
 */
public interface ReleaseTeachingPackageService {

	/**
	 * 发布教学包
	 * 
	 * @param jsonObject
	 * @param loginUserId
	 * @return
	 * @apiNote
	 * {
		    "pkgId":"", "subjectId":"",
		    "chapterIdArray":["ff64ce6d6dfe4ce088f89b2d386920f1", "0c22d826ab6d4187a0a4026edefd2508"],
		    "resgroupIdArray":["430acb9f5e2045e3b11ec0f572ceadff"],
		    "activityJson":"[{'活动id':'固定值activityType'}]",
		    "pkgName":"时间不在于你拥有多少"
		}
	 */
	R releaseTeachingPackage(JSONObject jsonObject, String loginUserId);
	
	/**
	 * 发布教学包页面时，一次性查出必要的数据，返回给前端处理
	 * @param pkgId
	 * @param loginUserId
	 * @return
	 * @apiNote 返回数据格式定义如下
	 * 
	 [
	    {
	        "chapterId": "章节1", 
	        "chapterName": "章节名称",
	        "resgroupList": [
	            {
	                "resgroupId": "98e454983977464e815bbdeb0a9a990d", 
	                "resgroupName": "课程内容", 
	                "dictCode": "1",
	                "activityList:" [
	                	{"activityId": "", "activityType": ""}
	                ]
	            }
			]
	    },
	    {
	        "chapterId": "章节----xxx", 
	        "chapterName": "章节名称",
	        "resgroupList": [
	            {
	                "resgroupId": "98e454983977464e815bbdeb0a9a990d", 
	                "resgroupName": "课程内容", 
	                "dictCode": "1",
	    		}
			]
	        "activityList:" [
	         	{"activityId": "", "activityType": ""}
	        ]
		}
	]
	 */
	R queryDataList(String pkgId, String loginUserId);
	
	R check(String pkgId, String loginUserId);
	
}
