package com.budaos.utils.tool;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Title:字符串处理 Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */

public class StrUtils {
	
	private static final Logger log = LoggerFactory.getLogger(StrUtils.class);
	
	/** The Constant chr. */
	public final static char[] chr = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	/**
	 * 为空
	 * 
	 * 2013年9月8日 下午10:08:44
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 不为空
	 * 
	 * 2013年9月8日 下午10:08:31
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	/**
	 * 转大写
	 * 
	 * 2013年9月8日 下午10:08:23
	 * 
	 * @param instr
	 * @return
	 */
    public static String toUpperCase(String instr) {
        return instr == null ? instr : instr.toUpperCase();
    }
    
    /**
     * 转小写
     * 
     * 2013年9月8日 下午10:08:10
     * 
     * @param instr
     * @return
     */
    public static String toLowerCase(String instr) {
        return instr == null ? instr : instr.toLowerCase();
    }
    

	/**
	 * 首字母大写 ,其余不变
	 * 
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirst(String str) {
		if (str == null)
			return null;
		if (str.length() == 0)
			return str;
		String pre = String.valueOf(str.charAt(0));
		return str.replaceFirst(pre, pre.toUpperCase());
	}
	
	/**
	 * 首字母小写 ,其余不变
	 * 
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerCaseFirst(String str) {
		if (str == null)
			return null;
		if (str.length() == 0)
			return str;
		String pre = String.valueOf(str.charAt(0));
		return str.replaceFirst(pre, pre.toLowerCase());
	}
	
	/**
	 * 不会抛NullPointerException 的trim() <br>
	 * 传入null会返回null
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}
	
	/**
	 * 过滤 ;当instr==null时返回长度为0的""; <br>
	 * 与 nvl(...)系的区别在于只处理null ,不处理长度为0的"";
	 * 
	 * @param instr
	 * @return
	 */
	public static String nvl(String instr) {
		return nvl(instr, "");
	}

	/**
	 * 过滤 ,把null和长度为0的""当成同一种情况处理; <br>
	 * 当instr==null||"".equals(instr)时返回defaultValue ;其它情况返回 instr
	 * 
	 * @param instr
	 * @param defaultValue
	 * @return
	 */
	public static String nvl(String instr, String defaultValue) {
		return instr == null || "".equals(instr) ? defaultValue : instr;
	}
	
	/**
	 * 比较 str1 和 str2 如果都是 null 或者 str1.equals(str2) 返回 true 表示一样 ;
	 * 
	 */
	public static boolean equals(String str1, String str2) {
		if (str1 == null && str2 == null)
			return true;
		if (str1 != null && str1.equals(str2))
			return true;
		return false;
	}

	public static String apadLeft(double a, int b, int len) {
		return apadLeft(String.valueOf(a), String.valueOf(b), len);
	}

	public static String apadRight(double a, int b, int len) {
		return apadRight(String.valueOf(a), String.valueOf(b), len);
	}

	public static String apadLeft(String str, String str2, int len) {
		if (str == null || str.length() == len || str2 == null)
			return str;
		if (str.length() > len)
			return str.substring(str.length() - len, len);
		return apadpro(str, str2, len, true);
	}

	public static String apadRight(String str, String str2, int len) {
		if (str == null || str.length() == len || str2 == null)
			return str;
		if (str.length() > len)
			return str.substring(0, len);
		return apadpro(str, str2, len, false);
	}

	private static String apadpro(String a, String b, int len, boolean appendleft) {
		int f = len - a.length();
		for (int i = 0; i < f; i++) {
			a = appendleft == true ? b + a : a + b;
		}
		return a;
	}



	/**
	 * 清除字符串中所有的空格 ,传入null返回null
	 * 
	 * @param str
	 * @return
	 */
	public static String clear(String str) {
		return clear(str, " ");
	}

	/**
	 * 清除str中出现的所有str2字符序列 直到结果中再也找不出str2为止 str2 == null时 返回str
	 * 
	 * @param str
	 *            原始字符串
	 * @param str2
	 *            清除的目标
	 * @return
	 */
	public static String clear(String str, String str2) {
		if (str == null)
			return str;
		if (str2 == null)
			return str;
		String reg = "(" + str2 + ")+";
		Pattern p = Pattern.compile(reg);
		while (p.matcher(str).find()) {
			str = str.replaceAll(reg, "");
		}
		return str;
	}

	/**
	 * 如果str的长度超过了c则取c-sub.length长度,然后拼上sub结尾
	 * 
	 * @param str
	 * @param c
	 * @param sub
	 * @return
	 */
	public static String suojin(String str, int c, String sub) {
		if (isEmpty(str))
			return str;
		if (str.length() <= c)
			return str;
		sub = nvl(sub);
		c = c - sub.length();
		c = c > str.length() ? 0 : c;
		str = str.substring(0, c);
		return str + sub;
	}

	/**
	 * 如果str的长度超过了length,取前length位然后拼上...
	 * @param str
	 * @param length
	 * @return
	 */
	public static String suojin(String str, int length) {
		return suojin(str, length, "…");
	}

	public static String replaceOnce(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, 1);
	}

	public static String replace(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	public static String replace(String text, String searchString, String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0)
			return text;
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1)
			return text;
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase >= 0 ? increase : 0;
		increase *= max >= 0 ? max <= 64 ? max : 64 : 16;
		StringBuffer buf = new StringBuffer(text.length() + increase);
		do {
			if (end == -1)
				break;
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0)
				break;
			end = text.indexOf(searchString, start);
		} while (true);
		buf.append(text.substring(start));
		return buf.toString();
	}
	// 将字符串转换成BigDecimal类型
		/**
		 * To big decimal.
		 *
		 * @param bigdecimal the bigdecimal
		 * @return the big decimal
		 */
		public static BigDecimal toBigDecimal(final String bigdecimal)
		{
			if (bigdecimal == null)
			{
				return null;
			}

			if (bigdecimal.length() < 1)
			{
				return null;
			}

			try
			{
				return new BigDecimal(bigdecimal);
			}
			catch (final Exception e)
			{
				return null;
			}
		}

		// 1 -> '0001'
		/**
		 * Gets the number string.
		 *
		 * @param i the i
		 * @param length the length
		 * @return the number string
		 */
		public static String getNumberString(final int i, final int length)
		{
			String sret = String.valueOf(i);
			final int lack = length - sret.length();

			if (lack == 0)
			{
				return sret;
			}

			if (lack > 0)
			{
				for (int x = 0; x < lack; x++)
					sret = "0" + sret;
			}
			else
			{
				sret = sret.substring(0 - lack, sret.length());
			}

			return sret;
		}

		// 1 -> '0001'
		/**
		 * Gets the number string.
		 *
		 * @param i the i
		 * @param length the length
		 * @return the number string
		 */
		public static String getNumberString(final long i, final int length)
		{
			String sret = String.valueOf(i);
			final int lack = length - sret.length();

			if (lack == 0)
			{
				return sret;
			}

			if (lack > 0)
			{
				for (int x = 0; x < lack; x++)
					sret = "0" + sret;
			}
			else
			{
				sret = sret.substring(0 - lack, sret.length());
			}

			return sret;
		}

		// 将字符串转换成符合HTML页面以及其表单显示的字符串
		/**
		 * To form input.
		 *
		 * @param oldValue the old value
		 * @return the string
		 */
		public static String toFormInput(final String oldValue)
		{
			if (oldValue == null)
			{
				return null;
			}

			String szTemp = "";
			final int len = oldValue.length();

			for (int i = 0; i < len; i++)
				switch (oldValue.charAt(i))
				{
				case 34: // '"'
					szTemp = szTemp + "&quot;";

					break;

				default:
					szTemp = szTemp + oldValue.charAt(i);

					break;
				}

			return szTemp;
		}

		// 将字符串中的oldtext用newtext来替换掉
		// 如："hello world"中的" "用"_"替换，就成了："hello_world"
		/**
		 * Replace.
		 *
		 * @param source the source
		 * @param oldtext the oldtext
		 * @param newtext the newtext
		 * @return the string
		 */
		public static String Replace(final String source, final String oldtext, final String newtext)
		{
			if ((source == null) || (oldtext == null) || (newtext == null))
			{
				return null;
			}

			String temp1 = source;
			String temp = "";

			for (int index = temp1.indexOf(oldtext); index >= 0; index = temp1.indexOf(oldtext))
			{
				temp = temp + temp1.substring(0, index) + newtext;
				temp1 = temp1.substring(index + oldtext.length(), temp1.length());
			}

			temp = temp + temp1;

			return temp;
		}

		// 将字符串形式的浮点数值转换成浮点数，如："234.7"-->234.7
		// 可以处理象"12,345.7"的形式
		/**
		 * To float.
		 *
		 * @param value the value
		 * @return the float
		 */
		public static float toFloat(final String value)
		{
			if (value == null)
			{
				return 0.0F;
			}

			String szTemp = "";

			for (int i = 0; i < value.length(); i++)
				if (value.charAt(i) != ',')
				{
					szTemp = szTemp + value.charAt(i);
				}

			try
			{
				final float f = Float.parseFloat(szTemp);

				return f;
			}
			catch (final NumberFormatException e)
			{
				final float f1 = 0.0F;

				return f1;
			}
		}

		// 将字符串形式的浮点数值转换成浮点数，如："234.7"-->234.7
		// 可以处理象"12,345.7"的形式
		/**
		 * To double.
		 *
		 * @param value the value
		 * @return the double
		 */
		public static double toDouble(final String value)
		{
			if (value == null)
			{
				return 0.0D;
			}

			String szTemp = "";

			for (int i = 0; i < value.length(); i++)
				if (value.charAt(i) != ',')
				{
					szTemp = szTemp + value.charAt(i);
				}

			try
			{
				final double d = Double.parseDouble(szTemp);

				return d;
			}
			catch (final NumberFormatException e)
			{
				final double d1 = 0.0D;

				return d1;
			}
		}

		// 将字符串形式的长整型值转换成长整数，如："234"-->234
		// 可以处理象"12,345"的形式
		/**
		 * To long.
		 *
		 * @param value the value
		 * @return the long
		 */
		public static long toLong(final String value)
		{
			if (value == null)
			{
				return 0L;
			}

			String szTemp = "";

			for (int i = 0; i < value.length(); i++)
				if (value.charAt(i) != ',')
				{
					szTemp = szTemp + value.charAt(i);
				}

			try
			{
				final double dd = Double.parseDouble(szTemp);
				final long l1 = (long) dd;

				return l1;
			}
			catch (final NumberFormatException e)
			{
				final long l = 0L;

				return l;
			}
		}

		// 将字符串形式的整型值转换成整数，如："234"-->234
		// 可以处理象"12,345"的形式
		/**
		 * To int.
		 *
		 * @param value the value
		 * @return the int
		 */
		public static int toInt(final String value)
		{
			return (int) toLong(value);
		}

		// 将对象转换成字符串
		/**
		 * To string.
		 *
		 * @param obj the obj
		 * @return the string
		 */
		public static String toString(final Object obj)
		{
			if (obj == null)
			{
				return "";
			}
			else
			{
				return obj.toString();
			}
		}

		/**
		 * 将数字转换成用'a'-'z'表示的字符串;1='a'...26='z',27='aa'...52='zz'等
		 * 等于将十进制转换成二十六进制
		 *
		 * @param num the num
		 * @return the string
		 */
		public static String toAbcNumber(final int num)
		{
			int i = num;
			final StringBuffer str = new StringBuffer();

			if (i <= 0)
			{
				return "";
			}

			do
			{
				str.insert(0, chr[(i - 1) % chr.length]);
				i = i / chr.length;

				if ((i > 0) && (i < chr.length))
				{
					str.insert(0, chr[i - 1]);
				}
			}
			while (i > chr.length);

			return str.toString();
		}
		/**
		 * 不会抛NullPointerException 的trim() <br>
		 * 传入null会返回null
		 * 
		 * @param map
		 * @return
		 */
		public static void  trim(Map<String,Object> map) {
			if(map != null && !map.isEmpty()){
				Iterator<String> keys = map.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					Object obj = map.get(key);
					if(obj instanceof String){
						map.put(key, trim(obj == null ? null : obj.toString()));
					}
				}
			}
		}
		/**
		 * 缩简字符串的函数
		 * 例如："abcdef,akd;adf" 变成 "abcdef,a..."
		 * 可以指定长度和最后的字符串
		 *
		 * @param source the source
		 * @param lststr the lststr
		 * @param length the length
		 * @param iscodelen the iscodelen
		 * @return the string
		 */
		public static String trimString(final String source, final String lststr, final int length, final boolean iscodelen)
		{
			if ((source == null) || (source.trim().length() == 0) || (length <= 0))
			{
				return source;
			}

			final String endStr = (lststr != null) ? lststr : "...";
			String result = source.trim();
			final int len = (endStr.length() < length) ? (length - endStr.length()) : 2;
			final byte[] sbytes = result.getBytes();

			if (length < (iscodelen ? sbytes.length : result.length()))
			{
				if (iscodelen)
				{
					if (new String(sbytes, 0, len).length() == 0)
					{
						result = new String(sbytes, 0, len - 1) + endStr;
					}
					else
					{
						result = new String(sbytes, 0, len) + endStr;
					}
				}
				else
				{
					result = source.substring(0, len) + endStr;
				}
			}

			return result;
		}

		/**
		 * 将字符串转换成垂直显示的HTML格式代码
		 * 在每一个字符的后面加上一个<br>.
		 *
		 * @param source the source
		 * @param lstline the lstline
		 * @param length the length
		 * @param iscodelen the iscodelen
		 * @return the string
		 */
		public static String toTrimHtmlVerticalString(final String source, final String lstline, final int length, final boolean iscodelen)
		{
			if ((source == null) || (source.trim().length() == 0) || (length <= 0))
			{
				return source;
			}

			String dst = source.trim();
			final byte[] sbytes = dst.getBytes();
			boolean istrim = false;

			if (length < (iscodelen ? sbytes.length : dst.length()))
			{
				istrim = true;

				if (iscodelen)
				{
					if (new String(sbytes, 0, length).length() == 0)
					{
						dst = new String(sbytes, 0, length - 1);
					}
					else
					{
						dst = new String(sbytes, 0, length);
					}
				}
				else
				{
					dst = dst.substring(0, length);
				}
			}

			final int len = dst.length();
			final StringBuffer result = new StringBuffer();

			for (int i = 0; i < (len - 1); i++)
			{
				result.append(dst.charAt(i)).append("<br>");
			}

			if (istrim && (lstline != null) && (lstline.trim().length() > 0))
			{
				result.append(lstline.trim());
			}
			else
			{
				result.append(dst.charAt(len - 1));
			}

			return result.toString();
		}

		/**
		 * 将字符串转换成垂直显示的HTML格式代码
		 * 在每一个字符的后面加上一个<br>.
		 *
		 * @param source the source
		 * @return the string
		 */
		public static String toHtmlVerticalString(final String source)
		{
			if ((source == null) || (source.trim().length() == 0))
			{
				return source;
			}

			final String tmp = source.trim();
			final int len = tmp.length();
			final StringBuffer result = new StringBuffer();

			for (int i = 0; i < (len - 1); i++)
			{
				result.append(tmp.charAt(i)).append("<br>");
			}

			result.append(tmp.charAt(len - 1));

			return result.toString();
		}

		/**
		 * 缩简字符串的函数
		 * 例如："abcdef,akd;adf" 变成 "abcdef,a..."
		 * 可以指定长度和最后的字符串
		 *
		 * @param source the source
		 * @return the short string
		 */

		// 无参数，取缺省参数
		public static String getShortString(final String source)
		{
			final int len = 10;
			final String endStr = "...";
			String result = source;

			if ((source != null) && (source.length() > len))
			{
				result = source.substring(0, len) + endStr;
			}

			return result;
		}

		// 指定长度
		/**
		 * Gets the short string.
		 *
		 * @param source the source
		 * @param length the length
		 * @return the short string
		 */
		public static String getShortString(final String source, final int length)
		{
			final int len = (length > 0) ? length : 10;
			final String endStr = "...";
			String result = source;

			if ((source != null) && (source.length() > len))
			{
				result = source.substring(0, len) + endStr;
			}

			return result;
		}

		// 指定长度与最后字符串
		/**
		 * Gets the short string.
		 *
		 * @param source the source
		 * @param length the length
		 * @param lastStr the last str
		 * @return the short string
		 */
		public static String getShortString(final String source, final int length, final String lastStr)
		{
			final int len = (length > 0) ? length : 10;
			final String endStr = (lastStr != null) ? lastStr : "...";
			String result = source;

			if ((source != null) && (source.length() > len))
			{
				result = source.substring(0, len) + endStr;
			}

			return result;
		}

		// 计算次方
		/**
		 * P.
		 *
		 * @param a the a
		 * @param b the b
		 * @return the double
		 */
		public static double P(final int a, final int b)
		{
			int result = 1;

			for (int i = 0; i < b; i++)
				result = result * a;

			return result;
		}

		/**
		 * P.
		 *
		 * @param a the a
		 * @param b the b
		 * @return the double
		 */
		public static double P(final float a, final int b)
		{
			float result = 1;

			for (int i = 0; i < b; i++)
				result = result * a;

			return result;
		}

		// 将整数值转换成二进制数组，如 13(10)=1101(2)，即1,3,4位为真，第2位为假(假设第一位为1)
		// 输入参数：整数数值value，数组长度length
		/**
		 * Gets the bin array.
		 *
		 * @param value the value
		 * @param length the length
		 * @return the bin array
		 */
		public static int[] getBinArray(final int value, final int length)
		{
			if ((value < 0) || (length <= 0))
			{
				return null;
			}

			final int len = (length > 32) ? 32 : length; // 限定最多32位长度
			int val = value;
			final int[] binbit = new int[len];

			for (int i = 0; i < len; i++)
			{
				if ((val % 2) == 1)
				{
					binbit[i] = 1;
				}
				else
				{
					binbit[i] = 0;
				}

				val = val >> 1;
			}

			return binbit;
		}

		/**
		 * 换行，空格字符的替换操作.
		 *
		 * @param in 要进行转换的字符串
		 * @return 替换后的字符串
		 */
		public static String replaceNewLine(final String in)
		{
			if (in == null)
			{
				return null;
			}
			char ch;
			final char[] input = in.toCharArray();
			final int len = input.length;
			final StringBuffer out = new StringBuffer((int) (len * 1.3));
			for (int index = 0; index < len; index++)
			{
				ch = input[index];
				if (ch == '\n')
				{
					out.append("<br>");
				}
				else if (ch == ' ')
				{
					out.append("&nbsp;");
				}
				else
				{
					out.append(ch);
				}

			}
			return out.toString();
		}

		/**
		 * 把字符串的字符集从ISO转换为gb2312.
		 *
		 * @param in 输入的ISO字符串
		 * @return GB2312字符串
		 */
		public static String convertIso8859ToGb2312(final String in)
		{
			if (in == null) {
				return null;
			}
			String out = null;
			byte[] ins = null;
			try
			{
				ins = in.getBytes("iso-8859-1");
			}
			catch (final UnsupportedEncodingException e)
			{
				log.error("ISO-8859-1编码不支持", e);
				return null;
			}
			try
			{
				out = new String(ins, "gb2312");
			}
			catch (final UnsupportedEncodingException e)
			{
				log.error("GB2312编码不支持", e);
			}
			return out;
		}

		/**
		 * 把字符串的字符集从GB2312转换为ISO.
		 *
		 * @param in 输入的GB2312字符串
		 * @return ISO字符串
		 */
		public static String convertGb2312ToIso8859(final String in)
		{
			if (in == null) {
				return null;
			}
			String out = null;
			try
			{
				final byte[] ins = in.getBytes("gb2312");
				out = new String(ins, "iso-8859-1");
			}
			catch (final Exception e)
			{
				log.error("GB2312转ISO-8859-1失败: {}", in, e);
			}
			return out;
		}

		/**
		 * Convert utf to gbk.
		 *
		 * @param in the in
		 * @return the string
		 */
		public static String convertUtfToGBK(final String in)
		{
			if (in == null) {
				return null;
			}
			String out = null;
			try
			{
				final byte[] ins = in.getBytes("UTF-8");
				out = new String(ins, "ISO-8859-1");
			}
			catch (final Exception e)
			{
				log.error("UTF-8转GBK失败: {}", in, e);
			}
			return out;
		}

		/**
		 * 去掉字符串两头的空格.
		 *
		 * @param str 待处理的字符串
		 * @return 处理后的字符串
		 */
		public static String convertNullToString(final String str)
		{

			if (str == null)
			{
				return "";
			}
			else
			{
				final int len = str.length();
				for (int i = 0; i < len; i++)
				{
					if (str.charAt(i) != ' ')
						break;
				}

				return str.trim();
			}
		}

		/**
		 * 检查电子邮件合法性.
		 *
		 * @param email 带验证的电子邮件地址
		 * @return true表示合法   false表示非法
		 */
		public static boolean checkEmailIsValid(final String email)
		{

			boolean isok = false;

			if (email.equals("") || email == "" || email == null)
				isok = false;

			for (int i = 1; i < email.length(); i++)
			{

				final char s = email.charAt(i);
				if (s == '@')
				{
					isok = true;
					break;

				}
			}
			return isok;
		}

		/**
		 * 替换字符串某些字符操作.
		 *
		 * @param str     原始的字符串 例如：bluesunny
		 * @param pattern 配备的字符 例如：blue
		 * @param replace 替换为的字符 例如：green
		 * @return 返回处理结果 例如：greensunny
		 */
		public static String replace_str(final String str, final String pattern, String replace)
		{

			if (replace == null)
			{
				replace = "";
			}
			int s = 0, e = 0;

			final StringBuffer result = new StringBuffer(str.length() * 2);
			while ((e = str.indexOf(pattern, s)) >= 0)
			{
				result.append(str.substring(s, e));
				result.append(replace);
				s = e + pattern.length();
			}
			result.append(str.substring(s));
			return result.toString();
		}

		/**
		 * 判断字符串是否为数字类型.
		 *
		 * @param str 待处理的字符串
		 * @return true表示为数字类型 false表示为非数字类型
		 */
		public static boolean isNumber(final String str)
		{
			if (str == null || str.equals(""))
			{
				return false;
			}
			String sStr = "";
			int m = 0;
			m = str.indexOf(".");

			for (int j = 0; j < str.length(); j++)
			{
				if (m != j)
					sStr = sStr + str.charAt(j);
			}

			final byte[] btyeStr = sStr.getBytes();
			for (int i = 0; i < btyeStr.length; i++)
			{
				if ((btyeStr[i] < 48) || (btyeStr[i] > 57))
				{
					return false;
				}
			}
			return true;
		}

		/**
		 * 把string型字符串转为整型.
		 *
		 * @param str 待处理的字符串
		 * @return 整数
		 */
		public static int strToInt(final String str)
		{
			int i = 0;
			if (str != null && str.length() != 0)
			{
				try
				{
					i = Integer.parseInt(str.trim());
				}
				catch (final NumberFormatException nfe)
				{
					log.warn("字符串转整数失败: {}", str, nfe);
				}
			}
			return i;
		}

		/**
		 * 把string型字符串转为Double.
		 *
		 * @param str 待处理的字符串
		 * @return double
		 */
		public static double strToDouble(final String str)
		{
			double i = 0;
			if (str != null && str.length() != 0)
			{
				try
				{
					i = Double.parseDouble(str.trim());
				}
				catch (final NumberFormatException nfe)
				{
					log.warn("字符串转Double失败: {}", str, nfe);
				}
			}
			return i;
		}

		/**
		 * 产生随机字符串.
		 *
		 * @return the string
		 */
		public static String createRandomString()
		{
			String random = null;
			// 产生随机字符串
			random = RandomStringUtils.randomAlphabetic(10);
			// 随机字符串再加上当前的日期时间 long
			random += DateUtils.getNowDateTimeToLong();
			return random;
		}

		/**
		 * 将Id数组转换成逗号分隔的字符串.
		 *
		 * @param fid the fid
		 * @return the string
		 */
		public static final String comboIdStr(final String[] fid)
		{
			String IdStr = "";
			for (int i = 0; i < fid.length; i++)
			{
				IdStr += fid[i];

				if (i != fid.length - 1)
					IdStr += ",";
			}
			return IdStr;
		}

		/**
		 * 将带特殊分隔符的字符串转换为按照指定替换符的字符串.
		 *
		 * @param oldValue the old value
		 * @param separateChar the separate char
		 * @param replaceStr the replace str
		 * @return the tree level value
		 */
		public static final String getTreeLevelValue(final String oldValue, final String separateChar, final String replaceStr)
		{
			final String[] spStr = oldValue.split(separateChar);
			String tmp = "";

			for (int i = 0; i < spStr.length - 1; i++)
			{
				tmp += replaceStr;
			}

			return tmp;
		}

		/**
		 * 2006-1.11zhujw add
		 * 把数组字符串转化成'１','２','３'格式
		 *
		 * @param fid the fid
		 * @return the string
		 */
		public static final String StrComboId(final String[] fid)
		{
			final StringBuffer str = new StringBuffer("");
			for (int i = 0; i < fid.length; i++)
			{
				str.append("'").append(fid[i]).append('\'');
				if (i != fid.length - 1)
					str.append(',');

			}
			return str.toString();
		}

		/**
		 * String >> char[] >> byte[] >> String
		 * 解决JSP中的中文问题.
		 *
		 * @param source the source
		 * @return the string
		 */
		public static String toByteString(final String source)
		{
			if (source == null)
			{
				return null;
			}

			if (source.length() == 0)
			{
				return "";
			}

			final char[] chars = source.toCharArray();
			final byte[] bytes = new byte[source.length() * 2];
			int index = 0;

			for (int i = 0, charValue = 0; (i < chars.length) && (index < (chars.length * 2)); i++)
			{
				charValue = chars[i];

				if (charValue > 255)
				{
					try
					{
						final byte[] tmp = String.valueOf(chars[i]).getBytes("GB2312");

						for (int j = 0; j < tmp.length; j++)
						{
							bytes[index] = tmp[j];
							index++;
						}
					}
					catch (final Exception e)
					{
						log.error("字符转GB2312字节失败: char={}", chars[i], e);
					}
				}
				else
				{
					bytes[index] = (byte) chars[i];
					index++;
				}
			}

			return new String(bytes, 0, index);
		}

		// unicode -> 8859-1 charset
		/**
		 * To_iso_8859_1.
		 *
		 * @param source the source
		 * @return the string
		 */
		public static String to_iso_8859_1(final String source)
		{
			if (source == null)
			{
				return null;
			}

			try
			{
				final String s = new String(source.getBytes(), "iso-8859-1");

				return s;
			}
			catch (final Exception uee)
			{
				log.error("字符串转ISO-8859-1失败: {}", source, uee);
				return null;
			}
		}

		// 8859-1 -> unicode charset
		/**
		 * From_iso_8859_1.
		 *
		 * @param source the source
		 * @return the string
		 */
		public static String from_iso_8859_1(final String source)
		{
			if (source == null)
			{
				return null;
			}

			try
			{
				final String s = new String(source.getBytes("iso-8859-1"));

				return s;
			}
			catch (final Exception uee)
			{
				final String s1 = null;

				return s1;
			}
		}

		/**
		 * 首字母大写.
		 *
		 * @param arg the arg
		 * @return the string
		 */
		public static String fistCapital(String arg)
		{
			if (arg != null && !arg.trim().equals(""))
			{
				arg = arg.replaceFirst(String.valueOf(arg.charAt(0)), String.valueOf(arg.charAt(0)).toUpperCase());
			}
			return arg;
		}

		/**
		 * Index of ignore case.
		 *
		 * @param src the src
		 * @param subS the sub s
		 * @param startIndex the start index
		 * @return the int
		 */
		public static int indexOfIgnoreCase(final String src, final String subS, final int startIndex)
		{
			final String sub = subS.toLowerCase();
			final int sublen = sub.length();
			final int total = src.length() - sublen + 1;

			for (int i = startIndex; i < total; i++)
			{
				int j = 0;

				while (j < sublen)
				{
					final char source = Character.toLowerCase(src.charAt(i + j));

					if (sub.charAt(j) != source)
					{
						break;
					}

					j++;
				}

				if (j == sublen)
				{
					return i;
				}
			}

			return -1;
		}

		/**
		 * Equals.
		 *
		 * @param obj1 the obj1
		 * @param obj2 the obj2
		 * @return true, if successful
		 */
		public static boolean equals(final Object obj1, final Object obj2)
		{
			if (obj1 == null && obj2 == null)
			{
				return true;
			}
			else if (obj1 != null)
			{
				return obj1.equals(obj2);
			}
			else
			{
				return obj2.equals(obj1);
			}
		}
		/**
		 * 是否为空
		 * @param obj
		 * @return
		 */
		public static boolean isNull(Object obj) {
			return obj == null || "".equals(obj);
		}
		/**
		 * 是否非空
		 * @param obj
		 * @return
		 */
		public static boolean notNull(Object obj) {
			return !isNull(obj);
		}
		/**
		 * 将字符患的首字母大写
		 * @param source
		 * @return
		 * @author 
		 */
		public static String upperFirst(String source) {
			if (isNull(source)) return "";
			return source.substring(0, 1).toUpperCase() + source.substring(1);
		}
		/**
		 * 首字母小写
		 * @param source
		 * @return
		 */
		public static String lowerFirst(String source) {
			if (isNull(source)) return source;
			return source.substring(0, 1).toLowerCase() + source.substring(1);
		}
		/**
		 * 把集合中的某一字段用逗号连接起来，用来拼装SQL语句
		 * @param datas
		 * @param field
		 * @return
		 */
		public static String join(List<?> datas, String field) {
			return join(datas, ",", "'", field);
		}
		/**
		 * 把集合中的某一字段用指定的符号连接起来，用来拼装SQL语句
		 * @param datas
		 * @param field
		 * @return
		 */
		public static String join(List<?> datas, String parttern, String wrapChar, String field) {
			if (datas == null || datas.isEmpty()) return wrapChar + wrapChar;
			String[] strs = new String[datas.size()];
			Method getter = null;
			int index = 0;
			for (Object t : datas) {
				if (getter == null) {
					try {
						getter = t.getClass().getDeclaredMethod("get"+ upperFirst(field));
					} catch (NoSuchMethodException e) {
						log.debug("字段 {} 的getter方法不存在", field);
						continue;
					} catch (Exception e) {
						log.error("反射获取getter方法失败: field={}", field, e);
						continue;
					}
				}
				if (getter != null) {
					Object o = null;
					try {
						o = getter.invoke(t);
					} catch (Exception e) {
						log.error("调用getter方法失败", e);
						continue;
					}
					if (o == null) continue;
					strs[index++] = o.toString();
				}
			}
			return join(strs, parttern, wrapChar);
		}
		/**
		 * 把数组中的某一字段用逗号连接起来，用来拼装SQL语句
		 * @param ts
		 * @return
		 */
		public static String join(Object[] ts) {
			return join(ts, ",", "'");
		}
		/**
		 * 把数组中的某一字段用指定的符号连接起来，用来拼装SQL语句
		 * @param ts
		 * @param parttern
		 * @param wrapChar
		 * @return
		 */
		public static String join(Object[] ts, String parttern, String wrapChar) {
			StringBuffer s = new StringBuffer();
			if (ts == null) return s.toString();
			if (isNull(parttern)) parttern = "";
			if (isNull(wrapChar)) wrapChar = "";
			for (Object obj : ts) {
				if (isNull(obj)) continue;
				s.append(wrapChar + obj + wrapChar + parttern);
			}
			if (s.length() > 0)	s.setLength(s.length() - 1);
			return s.toString();
		}

		
		public static String encodeToUrl(String value) {
			if (value == null) {
				return null;
			}
			try {
				String v = java.net.URLEncoder.encode(value, "UTF-8");
				return v.replaceAll("\\+", "%20");
			} catch (Exception e) {
				log.error("URL编码失败: {}", value, e);
				return value;
			}
		}

}
