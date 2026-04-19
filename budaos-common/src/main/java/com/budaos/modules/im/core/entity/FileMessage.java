package com.budaos.modules.im.core.entity;

/**
 * 文件消息
 * 
 * @author zhuq
 *
 */
public class FileMessage extends BaseMessage {

	public FileMessage() {
	}

	public FileMessage(String touser, String mediaId) {
		super(touser, MsgType.FILE);
		this.file = new File();
		this.file.setMedia_id(mediaId);
	}

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public static class File {
		private String media_id;

		public String getMedia_id() {
			return media_id;
		}

		public void setMedia_id(String mediaId) {
			media_id = mediaId;
		}

	}
}
