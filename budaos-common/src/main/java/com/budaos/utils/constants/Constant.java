package com.budaos.utils.constants;

/**
 * Title:常量 Copyright: Copyright (c) 2017 
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class Constant {
	
	public static final String USER_INFO = "userinfo"; // 登录的用户信息

	public static final String R_DATA = "data";
	public static final String PAGE_NUM = "pageNum";
	public static final String PAGE_SIZE = "pageSize";
	/**
	 * 成功
	 */
	public static final int CODE_SUCCESS = 0;
	/**
	 * 失败
	 */
	public static final int CODE_FAIL = -1;
	/**
	 * 异常
	 */
	public static final int CODE_ERROR = -2;
	  /**
     * 表单token
     */
    public static final String TOKEN_FORM = "tokenForm";
    /** 数据权限过滤 */
	public static final String SQL_FILTER = "filterSql";
    /**
     * 刷新表单token
     */
    public static final String HEAD_REFRESH_TOKEN_FORM = "X-Refresh-Token-Form";
	public static final String MSG_SUCCESS = "success";
	public static String SSO_KEY = "budaos123budaos";
	public static final int DEFAULT_PAGE_SIZE = 20;
	/** 超级管理员ID */
	public static final String SUPER_ADMIN = "1";
	// 数据范围（1：所有数据；2：所在部门及以下数据；3：所在部门数据；4：仅本人数据；5：按明细设置）
	public static enum DATA_SCOPE {
		ALLORG("所有数据", "1"), 
		SELFANDSUBORG("所在部门及以下数据",
				"2"), SELFORG("所在部门数据", "3"), SELFUSER(
			"仅本人数据", "4"), CUSTOMORG("按明细设置", "9");
		String name;
		String value;

		private DATA_SCOPE(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String toString() {
			return this.name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	};
	
	/**
	 * 菜单类型
	 * 
	 * @author
	 */
	public enum MenuType {
		/**
		 * 目录
		 */
		CATALOG(0),
		/**
		 * 菜单
		 */
		MENU(1),
		/**
		 * 按钮
		 */
		BUTTON(2),
		//子系统
		SYS(-1);

		private int value;

		private MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	/**
     * 定时任务状态
     * 
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

}
