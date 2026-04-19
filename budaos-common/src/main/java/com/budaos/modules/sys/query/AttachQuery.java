package com.budaos.modules.sys.query;

import java.io.Serializable;
import java.util.List;

/**
 * @author huj
 * @create 2022-09-05 10:03
 * @email hujun@budaos.com
 * @company 创蓝科技 www.budaos.com
 */
public class AttachQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileType;

    private List<String> pkIds;

    private List<String> attachIdList;

    public AttachQuery() {
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<String> getPkIds() {
        return pkIds;
    }

    public void setPkIds(List<String> pkIds) {
        this.pkIds = pkIds;
    }

    public List<String> getAttachIdList() {
        return attachIdList;
    }

    public void setAttachIdList(List<String> attachIdList) {
        this.attachIdList = attachIdList;
    }

    @Override
    public String toString() {
        return "AttachQuery{" +
                "fileType='" + fileType + '\'' +
                ", pkIds=" + pkIds +
                ", attachIdList=" + attachIdList +
                '}';
    }
}
