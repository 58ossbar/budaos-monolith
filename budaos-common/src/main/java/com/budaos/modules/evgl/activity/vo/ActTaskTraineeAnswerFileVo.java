package com.budaos.modules.evgl.activity.vo;

import java.io.Serializable;

/**
 * @author huj
 * @create 2022-05-31 10:17
 * @email hujun@budaos.com
 */
public class ActTaskTraineeAnswerFileVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 小组成员id
     */
    private String memberId;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件访问/显示地址
     */
    private String fileAccessUrl;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 源文件名称
     */
    private String originalFilename;

    /**
     * 创建时间
     */
    private String createTime;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileAccessUrl() {
        return fileAccessUrl;
    }

    public void setFileAccessUrl(String fileAccessUrl) {
        this.fileAccessUrl = fileAccessUrl;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ActTaskTraineeAnswerFileVo{" +
                "memberId='" + memberId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileAccessUrl='" + fileAccessUrl + '\'' +
                ", fileSuffix='" + fileSuffix + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", originalFilename='" + originalFilename + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
