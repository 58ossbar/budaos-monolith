package com.budaos.modules.sys.domain;

import com.budaos.core.baseclass.domain.BaseDomain;

import java.util.List;

public class TsysResource extends BaseDomain<Object> {
	private static final long serialVersionUID = 1L;
	// alias
	public static final String TABLE_ALIAS = "TsysResource";
	public static final String ALIAS_MENU_ID = "menuId";
	public static final String ALIAS_PARENT_ID = "父菜单ID，一级菜单为0";
	public static final String ALIAS_NAME = "菜单名称";
	public static final String ALIAS_URL = "菜单URL";
	public static final String ALIAS_PERMS = "授权(多个用逗号分隔，如：user:list,user:create)";
	public static final String ALIAS_TYPE = "类型   0：目录   1：菜单   2：按钮";
	public static final String ALIAS_ICON = "菜单图标";
	public static final String ALIAS_ORDER_NUM = "排序";
	public static final String ALIAS_APP_ID = "应用ID";
	public static final String ALIAS_RESOURCE_CLASS = "层级";
	public static final String ALIAS_REMARK = "资源描述";
	public static final String ALIAS_DISPLAY = "是否可见";
	/**
	 * 菜单ID
	 */
	private String menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private String parentId;

	/**
	 * 父菜单名称
	 */
	private String parentName;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型 0：目录 1：菜单 2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 应用ID db_column: APP_ID
	 */
	private java.lang.String orgId;
	/**
	 * 层级 db_column: RESOURCE_CLASS
	 */
	private java.lang.Integer resourceClass;
	/**
	 * 资源描述 db_column: REMARK
	 */
	private java.lang.String remark;
	/**
	 * 是否可见 db_column: DISPLAY
	 */
	private java.lang.String display;
	/**
	 * 是否有子结点
	 */
	private boolean isLeaf;

	private int level; // 节点所级别
	private List<TsysResource> children; // 子数据

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	// columns END
	
	private String orgName;
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * ztree属性
	 */
	private Boolean open;

	public java.lang.String getOrgId() {
		return orgId;
	}

	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}

	public java.lang.Integer getResourceClass() {
		return resourceClass;
	}

	public void setResourceClass(java.lang.Integer resourceClass) {
		this.resourceClass = resourceClass;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.lang.String getDisplay() {
		return display;
	}

	public void setDisplay(java.lang.String display) {
		this.display = display;
	}

	private List<?> list;

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuId() {
		return menuId;
	}

	/**
	 * 设置：父菜单ID，一级菜单为0
	 * 
	 * @param parentId 父菜单ID，一级菜单为0
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父菜单ID，一级菜单为0
	 * 
	 * @return String
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 设置：菜单名称
	 * 
	 * @param name 菜单名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：菜单名称
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：菜单URL
	 * 
	 * @param url 菜单URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：菜单URL
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置：菜单图标
	 * 
	 * @param icon 菜单图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 获取：菜单图标
	 * 
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置：排序
	 * 
	 * @param orderNum 排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 * 
	 * @return Integer
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<TsysResource> getChildren() {
		return children;
	}

	public void setChildren(List<TsysResource> children) {
		this.children = children;
	}
}
