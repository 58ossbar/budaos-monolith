package com.budaos.modules.im.core.entity;

/**
 * 语音
 *
 * @author zhuq
 */
public class VoiceMessage extends BaseMessage {

	public VoiceMessage() {
	}

	public VoiceMessage(String touser, String mediaId) {
		super(touser, MsgType.VOICE);
		this.voice = new Voice();
		this.voice.setMedia_id(mediaId);
	}

	public Voice voice;

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}

	public static class Voice {
		private String media_id;

		public String getMedia_id() {
			return media_id;
		}

		public void setMedia_id(String mediaId) {
			media_id = mediaId;
		}
	}

}
