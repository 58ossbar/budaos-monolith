package com.budaos.modules.core.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class CbMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	public CbMappingJackson2HttpMessageConverter(){
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.TEXT_HTML);
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		mediaTypes.add(MediaType.MULTIPART_FORM_DATA);
		setSupportedMediaTypes(mediaTypes);
	}
}
