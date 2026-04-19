package com.budaos.modules.sys.domain;

//import com.alibaba.fastjson.JSONObject;
//import com.budaos.common.cbsecurity.validator.an.MaxLength;
import com.budaos.core.baseclass.domain.BaseDomain;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2017 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhujw
 * @version 1.0
 */

public class TsysDocs extends BaseDomain<TsysDocs>{
	private static final long serialVersionUID = 1L;
	
	//alias
	public static final String TABLE_ALIAS = "TsysDocs";
	public static final String ALIAS_DOC_ID = "主键ID";
	public static final String ALIAS_PARENT_ID = "父节点";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_SYSTEM_ID = "所属子系统ID";
	public static final String ALIAS_MAIN_DOC = "归属文档";
	public static final String ALIAS_MENU_ID = "菜单ID";
	public static final String ALIAS_DOC_NAME = "文档名称";
	public static final String ALIAS_DOC_SUMMARY = "文档简介";
	public static final String ALIAS_CONTENT = "文档内容";
	public static final String ALIAS_DOC_HISTORY = "文档更新历史轨迹说明";
	public static final String ALIAS_DOC_TYPE = "文档类型(01帮助手册，02api文档)";
	public static final String ALIAS_DOC_CLASS = "层级(1,2,3)";
	public static final String ALIAS_DOC_SORT = "序号(1，1.1，1.1.1)";
	public static final String ALIAS_DOC_ICON = "文档封面图";
	public static final String ALIAS_DISPLAY = "是否可见";
	public static final String ALIAS_DOC_DOWNNUM = "文档下载量";
	public static final String ALIAS_DOC_SHARENUM = "文档分享量";
	public static final String ALIAS_DOC_VIEWNUM = "文档阅读量";
	public static final String ALIAS_DOC_LIKENUM = "文档点赞量";
	

    /**
     * 主键ID       db_column: doc_id 
     */	
 	//@NotNull(msg="主键ID不能为空")
 	//@MaxLength(value=32, msg="字段[主键ID]超出最大长度[32]")
	private java.lang.String docId;
    /**
     * 父节点       db_column: parent_id 
     */	
 	//@NotNull(msg="父节点不能为空")
 	//@MaxLength(value=32, msg="字段[父节点]超出最大长度[32]")
	private java.lang.String parentId;
    /**
     * 机构ID       db_column: org_id 
     */	
 	//@NotNull(msg="机构ID不能为空")
 	//@MaxLength(value=32, msg="字段[机构ID]超出最大长度[32]")
	private java.lang.String orgId;
    /**
     * 菜单ID       db_column: menu_id 
     */	
 	//@NotNull(msg="菜单ID不能为空")
 	//@MaxLength(value=32, msg="字段[菜单ID]超出最大长度[32]")
	private java.lang.String menuId;
    /**
     * 所属子系统ID       db_column: system_id 
     */	
 	//@NotNull(msg="所属子系统ID不能为空")
 	//@MaxLength(value=32, msg="字段[所属子系统ID]超出最大长度[32]")
	private java.lang.String systemId;
    /**
     * 归属文档       db_column: main_doc 
     */	
 	//@NotNull(msg="归属文档不能为空")
 	//@MaxLength(value=32, msg="字段[归属文档]超出最大长度[32]")
	private java.lang.String mainDoc;
    /**
     * 文档名称       db_column: doc_name 
     */	
 	//@NotNull(msg="文档名称不能为空")
 	//@MaxLength(value=50, msg="字段[文档名称]超出最大长度[50]")
	private java.lang.String docName;
    /**
     * 文档简介       db_column: doc_summary 
     */	
 	//@NotNull(msg="文档简介不能为空")
 	//@MaxLength(value=1000, msg="字段[文档简介]超出最大长度[1000]")
	private java.lang.String docSummary;
    /**
     * 文档内容       db_column: content 
     */	
 	//@NotNull(msg="文档内容不能为空")
 	//@MaxLength(value=2147483647, msg="字段[文档内容]超出最大长度[2147483647]")
	private java.lang.String content;
    /**
     * 文档更新历史轨迹说明       db_column: doc_history 
     */	
 	//@NotNull(msg="文档更新历史轨迹说明不能为空")
 	//@MaxLength(value=2147483647, msg="字段[文档更新历史轨迹说明]超出最大长度[2147483647]")
	private java.lang.String docHistory;
    /**
     * 文档类型(01帮助手册，02api文档)       db_column: doc_type 
     */	
 	//@NotNull(msg="文档类型不能为空")
 	//@MaxLength(value=5, msg="字段[文档类型]超出最大长度[5]")
	private java.lang.String docType;
    /**
     * 层级(1,2,3)       db_column: doc_class 
     */	
 	//@NotNull(msg="层级不能为空")
 	//@MaxLength(value=5, msg="字段[层级]超出最大长度[5]")
	private java.lang.String docClass;
    /**
     * 序号(1，1.1，1.1.1)       db_column: doc_sort 
     */	
 	//@NotNull(msg="序号不能为空")
 	//@MaxLength(value=10, msg="字段[序号]超出最大长度[10]")
	private java.lang.Integer docSort;
    /**
     * 文档封面图       db_column: doc_icon 
     */	
 	//@NotNull(msg="文档封面图不能为空")
 	//@MaxLength(value=100, msg="字段[文档封面图]超出最大长度[100]")
	private java.lang.String docIcon;
    /**
     * 是否可见       db_column: display 
     */	
 	//@NotNull(msg="是否可见不能为空")
 	//@MaxLength(value=5, msg="字段[是否可见]超出最大长度[5]")
	private java.lang.String display;
    /**
     * 文档下载量       db_column: doc_downnum 
     */	
 	//@NotNull(msg="文档下载量不能为空")
 	//@MaxLength(value=10, msg="字段[文档下载量]超出最大长度[10]")
	private java.lang.Integer docDownnum;
    /**
     * 文档分享量       db_column: doc_sharenum 
     */	
 	//@NotNull(msg="文档分享量不能为空")
 	//@MaxLength(value=10, msg="字段[文档分享量]超出最大长度[10]")
	private java.lang.Integer docSharenum;
    /**
     * 文档阅读量       db_column: doc_viewnum 
     */	
 	//@NotNull(msg="文档阅读量不能为空")
 	//@MaxLength(value=10, msg="字段[文档阅读量]超出最大长度[10]")
	private java.lang.Integer docViewnum;
    /**
     * 文档点赞量       db_column: doc_likenum 
     */	
 	//@NotNull(msg="文档点赞量不能为空")
 	//@MaxLength(value=10, msg="字段[文档点赞量]超出最大长度[10]")
	private java.lang.Integer docLikenum;
 	
 	private java.util.List<TsysDocs> childrens;
	//columns END

	public TsysDocs(){
	}

	public TsysDocs(
		java.lang.String docId
	){
		this.docId = docId;
	}

	public void setDocId(java.lang.String value) {
		this.docId = value;
	}
	public java.lang.String getDocId() {
		return this.docId;
	}
	public void setParentId(java.lang.String value) {
		this.parentId = value;
	}
	public java.lang.String getParentId() {
		return this.parentId;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public java.lang.String getSystemId() {
		return systemId;
	}
	public void setSystemId(java.lang.String systemId) {
		this.systemId = systemId;
	}
	public java.lang.String getMainDoc() {
		return mainDoc;
	}
	public void setMainDoc(java.lang.String mainDoc) {
		this.mainDoc = mainDoc;
	}
	public java.lang.String getMenuId() {
		return menuId;
	}
	public void setMenuId(java.lang.String menuId) {
		this.menuId = menuId;
	}
	public void setDocName(java.lang.String value) {
		this.docName = value;
	}
	public java.lang.String getDocName() {
		return this.docName;
	}
	public void setDocSummary(java.lang.String value) {
		this.docSummary = value;
	}
	public java.lang.String getDocSummary() {
		return this.docSummary;
	}
	public void setContent(java.lang.String value) {
		this.content = value;
	}
	public java.lang.String getContent() {
		return this.content;
	}
	public void setDocHistory(java.lang.String value) {
		this.docHistory = value;
	}
	public java.lang.String getDocHistory() {
		return this.docHistory;
	}
	public void setDocType(java.lang.String value) {
		this.docType = value;
	}
	public java.lang.String getDocType() {
		return this.docType;
	}
	public void setDocClass(java.lang.String value) {
		this.docClass = value;
	}
	public java.lang.String getDocClass() {
		return this.docClass;
	}
	public void setDocSort(java.lang.Integer value) {
		this.docSort = value;
	}
	public java.lang.Integer getDocSort() {
		return this.docSort;
	}
	public void setDocIcon(java.lang.String value) {
		this.docIcon = value;
	}
	public java.lang.String getDocIcon() {
		return this.docIcon;
	}
	public void setDisplay(java.lang.String value) {
		this.display = value;
	}
	public java.lang.String getDisplay() {
		return this.display;
	}
	public void setDocDownnum(java.lang.Integer value) {
		this.docDownnum = value;
	}
	public java.lang.Integer getDocDownnum() {
		return this.docDownnum;
	}
	public void setDocSharenum(java.lang.Integer value) {
		this.docSharenum = value;
	}
	public java.lang.Integer getDocSharenum() {
		return this.docSharenum;
	}
	public void setDocViewnum(java.lang.Integer value) {
		this.docViewnum = value;
	}
	public java.lang.Integer getDocViewnum() {
		return this.docViewnum;
	}
	public void setDocLikenum(java.lang.Integer value) {
		this.docLikenum = value;
	}
	public java.lang.Integer getDocLikenum() {
		return this.docLikenum;
	}
	public java.util.List<TsysDocs> getChildrens() {
		return childrens;
	}
	public void setChildrens(java.util.List<TsysDocs> childrens) {
		this.childrens = childrens;
	}
}

