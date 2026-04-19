package com.budaos.modules.evgl.site.vo;

import com.budaos.core.baseclass.annotation.validator.MaxLength;
import com.budaos.core.baseclass.annotation.validator.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * 面向管理端，微信公众号，新闻资讯，新增修改的vo
 * @author huj
 * @create 2022-01-04 9:10
 * @email hujun@budaos.com
 */
public class TevglSiteNewsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 资讯主键id       db_column: newsid
     */
    @NotNull(msg="资讯主键id不能为空")
    @MaxLength(value=32, msg="字段[资讯主键id]超出最大长度[32]")
    private String newsid;

    /**
     * 资讯标题       db_column: new_title
     */
    @NotNull(msg="资讯标题不能为空")
    @MaxLength(value=200, msg="字段[资讯标题]超出最大长度[200]")
    private String newTitle;

    /**
     * 资讯内容       db_column: content
     */
    @NotNull(msg="资讯内容不能为空")
    @MaxLength(value=2147483647, msg="字段[资讯内容]超出最大长度[2147483647]")
    private String content;

    /**
     * 资讯logo       db_column: news_logo
     */
    @MaxLength(value=300, msg="字段[资讯logo]超出最大长度[300]")
    @NotNull(msg="资讯封面不能为空")
    private String newsLogo;

    /**
     * 作者       db_column: author
     */
    @MaxLength(value=20, msg="字段[作者]超出最大长度[20]")
    private String author;
    
    /**
     * 类型       db_column: official_link_type
     */
    @MaxLength(value=20, msg="字段[类型]超出最大长度[20]")
    private String officialLinkType;

    private List<TevglSiteNewsVo> children;

    /**
     * 状态1待审2已发布 3删除
     */
    private String status;
    private String statusName;
    
    private String time;
    
    private Integer viewNum;

    /**
     * 创建时间
     */
    private String createTime;

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewsLogo() {
        return newsLogo;
    }

    public void setNewsLogo(String newsLogo) {
        this.newsLogo = newsLogo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TevglSiteNewsVo> getChildren() {
        return children;
    }

    public void setChildren(List<TevglSiteNewsVo> children) {
        this.children = children;
    }

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOfficialLinkType() {
		return officialLinkType;
	}

	public void setOfficialLinkType(String officialLinkType) {
		this.officialLinkType = officialLinkType;
	}

	public Integer getViewNum() {
		return viewNum;
	}

	public void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	@Override
	public String toString() {
		return "TevglSiteNewsVo [newsid=" + newsid + ", newTitle=" + newTitle + ", content=" + content + ", newsLogo="
				+ newsLogo + ", author=" + author + ", children=" + children + ", status=" + status + ", time=" + time
				+ ", createTime=" + createTime + "]";
	}

	
}
