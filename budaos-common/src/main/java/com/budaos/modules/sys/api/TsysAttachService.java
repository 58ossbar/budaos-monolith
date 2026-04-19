package com.budaos.modules.sys.api;

import com.budaos.core.baseclass.domain.R;
import com.budaos.modules.sys.domain.TsysAttach;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件管理api
 * </p>
 * <p>
 * Title: TsysAttachService.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: 湖南创蓝信息科技有限公司
 * </p>
 * 
 * @author huj
 * @date 2019年5月9日
 */
public interface TsysAttachService {

	/**
	 * <p>
	 * 根据条件查询记录
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param map
	 * @return
	 */
	R query(Map<String, Object> map);

	/**
	 * <p>
	 * 执行数据保存和修改
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 * @return
	 */
	R saveOrUpdate(TsysAttach tsysAttach);

	/**
	 * <p>
	 * 新增
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 */
	void save(TsysAttach tsysAttach);

	/**
	 * <p>
	 * 修改
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param tsysAttach
	 */
	void update(TsysAttach tsysAttach);

	/**
	 * <p>
	 * 删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R delete(String id);

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param ids
	 * @return
	 */
	R deleteBatch(String[] ids);

	/**
	 * <p>
	 * 查看明细
	 * </p>
	 * 
	 * @author huj
	 * @data 2019年5月9日
	 * @param id
	 * @return
	 */
	R view(String id);

	/**
	 * 文件上传时候保存附件记录，根据前端组件的特性,需要在实际点击保存的时候获取attach对象进行id的绑定。
	 * 
	 * @author huangwb
	 * @data 2019年5月30日
	 * @param oldNamePc      原始名称
	 * @param extensionPc    文件后缀
	 * @param fileSize       文件大小
	 * @param imgNamePc      链接地址
	 * @param longNamePc     文件名称
	 * @param fileType       文件类型 1、字典
	 * @param uploadUsername 上传人名称
	 * @return attachId 附件id
	 */
	String uploadFileInsertAttach(String oldNamePc, String fileSize, String extensionPc, String imgNamePc,
			String longNamePc, String fileType, String uploadUsername);

	/**
	 * 资源上传成功后保存时修改文件绑定关联状态
	 * 
	 * @param attachId 附件id
	 * @param type     删除还是修改 0为修改 1为添加
	 * @param fileType 附件类型 1为字典
	 * @param pkId     关联id
	 */
	@Deprecated
	void updateAttach(String attachId, String pkId, String type, String fileType);
	
	/**
	 * （绑定操作）资源上传成功后保存时修改文件绑定关联状态
	 * @param attachId 附件表t_sys_attach主键id
	 * @param pkId 关联id，被关联对象表的主键id，示例：假如是新增学员时，且上传了附件，该值就是学员表主键id
	 * @param fileType 下标位置，属性文件中，com.budaos.cb-upload-paths对应的下标
	 */
	void updateAttachForAdd(String attachId, String pkId, String fileType);
	
	/**
	 * （绑定新的，解绑旧的）资源上传成功后保存时修改文件绑定关联状态
	 * @param attachId 附件表t_sys_attach主键id
	 * @param pkId 关联id，被关联对象表的主键id，示例：假如是新增学员时，且上传了附件，该值就是学员表主键id
	 * @param fileType 下标位置，属性文件中，com.budaos.cb-upload-paths对应的下标
	 */
	void updateAttachForEdit(String attachId, String pkId, String fileType);
	
    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     * @param pkId
     * @param fileType
     * @return
     */
    boolean unBind(String pkId, String fileType);

    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     * @param pkIdList
     * @param fileType
     * @return
     */
    boolean unBind(List<String> pkIdList, String fileType);

    /**
     * 解绑关系，未绑定的附件，就可以直接物理删除了
     * @param pkIds
     * @param fileType
     * @return
     */
    boolean unBind(String[] pkIds, String fileType);
}
