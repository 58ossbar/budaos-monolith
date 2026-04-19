package com.budaos.modules.core.config.captcha;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

/**
 * Title:生成验证码配置  Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        // 图片边框，设为no表示无边框
        properties.put("kaptcha.border", "no");
        // 边框颜色，合法值：white,lightGray,gray,black,darkGray,blue,cadetblue,darkGreen,darkMagenta,darkOrange,darkRed,darkSlateBlue,green,lightGreen,magenta,orange,pink,red,slateBlue,yellow
        properties.put("kaptcha.border.color", "lightGray");
        // 边框厚度，合法值：>0
        properties.put("kaptcha.border.thickness", "1");
        // 图片宽度
        properties.put("kaptcha.image.width", "140");
        // 图片高度
        properties.put("kaptcha.image.height", "50");
        // 图片实现类
        properties.put("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha");
        // 文本生成器
        properties.put("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
        // 文本集合，验证码值从此集合中获取
        properties.put("kaptcha.textproducer.char.string", "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ");
        // 验证码文本字符长度，默认4
        properties.put("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.put("kaptcha.textproducer.font.names", "Arial,Courier New,Georgia,Microsoft YaHei,SimSun");
        // 字体大小，默认40
        properties.put("kaptcha.textproducer.font.size", "38");
        // 字体颜色，使用深蓝色更醒目
        properties.put("kaptcha.textproducer.font.color", "0,0,139");
        // 文字间隔
        properties.put("kaptcha.textproducer.char.space", "8");
        // 干扰线实现类，使用无干扰线
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        // 干扰线颜色
        properties.put("kaptcha.noise.color", "220,220,220");
        // 图片样式：水纹 com.google.code.kaptcha.impl.WaterRipple
        // 鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy
        // 阴影 com.google.code.kaptcha.impl.ShadowGimpy
        // 使用水纹效果，干扰较少
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        // 背景实现类
        properties.put("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        // 背景颜色渐变，开始颜色
        properties.put("kaptcha.background.clear.from", "245,245,245");
        // 背景颜色渐变，结束颜色
        properties.put("kaptcha.background.clear.to", "255,255,255");
        // 文字渲染器
        properties.put("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        // session key
        properties.put("kaptcha.session.key", Constants.KAPTCHA_SESSION_KEY);
        // session date key
        properties.put("kaptcha.session.date", Constants.KAPTCHA_SESSION_DATE);
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
