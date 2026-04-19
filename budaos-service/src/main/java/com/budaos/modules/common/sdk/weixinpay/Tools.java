package com.budaos.modules.common.sdk.weixinpay;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****************************************************
 *
 *  常用工具
 *
 * @author majker
 * @version 1.0
 **************************************************/
public class Tools {

   /**
    * 随机生成六位数验证码
    * @return
    */
   public static int getRandomNum(){
        Random r = new Random();
        return r.nextInt(900000)+100000;//(Math.random()*(999999-100000)+100000)
   }

   /**
    * 检测字符串是否不为空(null,"","null")
    * @param s
    * @return 不为空则返回true，否则返回false
    */
   public static boolean notEmpty(String s){
       return s!=null && !"".equals(s) && !"null".equals(s);
   }

   /**
    * 检测字符串是否为空(null,"","null")
    * @param s
    * @return 为空则返回true，不否则返回false
    */
   public static boolean isEmpty(String s){
       return s==null || "".equals(s) || "null".equals(s);
   }

   /**
    * 字符串转换为字符串数组
    * @param str 字符串
    * @param splitRegex 分隔符
    * @return
    */
   public static String[] str2StrArray(String str,String splitRegex){
       if(isEmpty(str)){
           return null;
       }
       return str.split(splitRegex);
   }

   /**
    * 用默认的分隔符(,)将字符串转换为字符串数组
    * @param str	字符串
    * @return
    */
   public static String[] str2StrArray(String str){
       return str2StrArray(str,",\\s*");
   }

   /**
    * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
    * @param date
    * @return yyyy-MM-dd HH:mm:ss
    */
   public static String date2Str(Date date){
       return date2Str(date,"yyyy-MM-dd HH:mm:ss");
   }

   /**
    * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
    * @param date
    * @return
    */
   public static Date str2Date(String date){
       if(notEmpty(date)){
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           try {
               return sdf.parse(date);
           } catch (ParseException e) {
               e.printStackTrace();
           }
           return new Date();
       }else{
           return null;
       }
   }

   /**
    * 按照参数format的格式，日期转字符串
    * @param date
    * @param format
    * @return
    */
   public static String date2Str(Date date,String format){
       if(date!=null){
           SimpleDateFormat sdf = new SimpleDateFormat(format);
           return sdf.format(date);
       }else{
           return "";
       }
   }

   /**
    * 把时间根据时、分、秒转换为时间段
    * @param StrDate
    */
   public static String getTimes(String StrDate){
       String resultTimes = "";

       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date now;

       try {
           now = new Date();
           Date date=df.parse(StrDate);
           long times = now.getTime()-date.getTime();
           long day  =  times/(24*60*60*1000);
           long hour = (times/(60*60*1000)-day*24);
           long min  = ((times/(60*1000))-day*24*60-hour*60);
           long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

           StringBuffer sb = new StringBuffer();
           //sb.append("发表于：");
           if(hour>0 ){
               sb.append(hour+"小时前");
           } else if(min>0){
               sb.append(min+"分钟前");
           } else{
               sb.append(sec+"秒前");
           }

           resultTimes = sb.toString();
       } catch (ParseException e) {
           e.printStackTrace();
       }

       return resultTimes;
   }

   /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
     boolean flag = false;
     try{
       String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
       Pattern regex = Pattern.compile(check);
       Matcher matcher = regex.matcher(email);
       flag = matcher.matches();
      }catch(Exception e){
       flag = false;
      }
     return flag;
    }

    /**
     * 验证手机号码
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobileNumber);
        b = m.matches();
        return b;
    }

   /**
    * 将驼峰转下划线
    * @param param
    * @return
    */
   public static String camelToUnderline(String param){
       if (param==null||"".equals(param.trim())){
           return "";
       }
       int len=param.length();
       StringBuilder sb=new StringBuilder(len);
       for (int i = 0; i < len; i++) {
           char c=param.charAt(i);
           if (Character.isUpperCase(c)){
               sb.append("_");
               sb.append(Character.toLowerCase(c));
           }else{
               sb.append(c);
           }
       }
       return sb.toString();
   }

   /**
    * 去掉下划线并将下划线后的首字母转为大写
    * @param str
    * @return
    */
   public static String transformStr(String str){
       //去掉数据库字段的下划线
       if(str.contains("_")) {
           String[] names = str.split("_");
           String firstPart = names[0];
           String otherPart = "";
           for (int i = 1; i < names.length; i++) {
               String word = names[i].replaceFirst(names[i].substring(0, 1), names[i].substring(0, 1).toUpperCase());
               otherPart += word;
           }
           str = firstPart + otherPart;
       }
       return str;
   }

   /**
    * 转换为map
    * @param list
    * @return
    */
   public static List<Map<String,Object>> transformMap(List<Map<String,Object>> list){
       List<Map<String,Object>> resultMapList = new ArrayList<>();

       for (Map<String, Object> map : list) {
           Map<String,Object> tempMap = new HashMap<>();
           for (String s : map.keySet()) {
               tempMap.put(transformStr(s),map.get(s));
           }
           resultMapList.add(tempMap);
       }
       return resultMapList;
   }

   public static String clearHtml(String content,int p) {
       if(null==content) return "";
       if(0==p) return "";

       Pattern p_script;
       Matcher m_script;
       Pattern p_style;
       Matcher m_style;
       Pattern p_html;
       Matcher m_html;

       try {
           String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
           //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
           String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
           //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
           String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

           p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
           m_script = p_script.matcher(content);
           content = m_script.replaceAll(""); //过滤script标签
           p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
           m_style = p_style.matcher(content);
           content = m_style.replaceAll(""); //过滤style标签

           p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
           m_html = p_html.matcher(content);

           content = m_html.replaceAll(""); //过滤html标签
       }catch(Exception e) {
           return "";
       }

       if(content.length()>p){
           content = content.substring(0, p)+"...";
       }else{
           content = content + "...";
       }

       return content;
   }

   public static String md5(String str) {
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(str.getBytes());
           byte b[] = md.digest();

           int i;

           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
               i = b[offset];
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }
           str = buf.toString();
       } catch (Exception e) {
           e.printStackTrace();

       }
       return str;
   }


}

