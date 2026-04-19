package com.budaos.modules.evgl.forum.vo;

import com.budaos.modules.evgl.forum.domain.TevglForumBlogPost;

/**
 * <p> Title: </p>
 * <p> Description:</p>
 * <p> Copyright: Copyright (c) 2019 </p>
 * <p> Company:budaos.co.,ltd </p>
 *
 * @author zhuq
 * @version 1.0
 */

public class TevglForumBlogPostVo extends TevglForumBlogPost {
	
	private static final long serialVersionUID = 1L;
	
	private String traineeName;
	private String traineePic;
	
	public String getTraineeName() {
		return traineeName;
	}
	public void setTraineeName(String traineeName) {
		this.traineeName = traineeName;
	}
	public String getTraineePic() {
		return traineePic;
	}
	public void setTraineePic(String traineePic) {
		this.traineePic = traineePic;
	}
	
}

