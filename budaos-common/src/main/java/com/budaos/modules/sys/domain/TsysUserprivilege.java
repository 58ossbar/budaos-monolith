/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

package com.budaos.modules.sys.domain;
import com.budaos.core.baseclass.domain.BaseDomain;


public class TsysUserprivilege extends BaseDomain<Object>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysUserprivilege";
	public static final String ALIAS_USERPRVIID = "用户特权ID";
	public static final String ALIAS_MENU_ID = "资源ID";
	public static final String ALIAS_USERID = "用户ID";
	

    /**
     * 用户特权ID       db_column: USERPRVIID 
     */	
	private java.lang.String userprviid;
    /**
     * 资源ID       db_column: MENU_ID 
     */	
	private java.lang.String menuId;
    /**
     * 用户ID       db_column: USERID 
     */	
	private java.lang.String userid;
	//columns END

	public TsysUserprivilege(){
	}

	public TsysUserprivilege(
		java.lang.String userprviid
	){
		this.userprviid = userprviid;
	}

	public void setUserprviid(java.lang.String value) {
		this.userprviid = value;
	}
	
	public java.lang.String getUserprviid() {
		return this.userprviid;
	}
	public void setMenuId(java.lang.String value) {
		this.menuId = value;
	}
	
	public java.lang.String getMenuId() {
		return this.menuId;
	}
	public void setUserid(java.lang.String value) {
		this.userid = value;
	}
	
	public java.lang.String getUserid() {
		return this.userid;
	}
	
	private TsysResource tsysResource;
	
	public void setTsysResource(TsysResource tsysResource){
		this.tsysResource = tsysResource;
	}
	
	public TsysResource getTsysResource() {
		return tsysResource;
	}

}

