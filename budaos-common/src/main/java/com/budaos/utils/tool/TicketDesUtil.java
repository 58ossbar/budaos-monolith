package com.budaos.utils.tool;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Title:DES加解密Copyright: Copyright (c) 2017 Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
public class TicketDesUtil {

	private static final String Algorithm = "DESede"; // 定义加密算法,可用 DES,DESede,Blowfish
	private static final String salt = "budaos123@!admin#&*12345"; // DESede 要判1剂24位密鑰

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; ++n) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1) {
				// hs = hs;
			}
		}
		return hs.toLowerCase();
	}

	public static byte[] hexStr2Bytes(String src) {
		int m = 0;
		int n = 0;
		int l = src.length() / 2;

		byte[] ret = new byte[l];
		for (int i = 0; i < l; ++i) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	public static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	public static String encrypt(String source, String key) {
		if (key == null) {
			key = salt;
		} else if (key.length() < 24) {
			throw new RuntimeException("key length must be 24 !");
		}
		byte[] b = encrypt(source.getBytes(), key.substring(0, 24).getBytes());
		return byte2hex(b);
	}

	public static String encryptWithMd5(String source, String key) {
		if (key == null) {
			key = salt;
		} else if (key.length() < 24) {
			throw new RuntimeException("key length must be 24 !");
		}
		byte[] b = encrypt(source.getBytes(), key.substring(0, 24).getBytes());
		return MD5Utils.MD5Encode(byte2hex(b), "utf-8");
	}

	public static String decrypt(String ciphertext, String key) {
		if (key == null) {
			key = salt;
		} else if (key.length() < 24) {
			throw new RuntimeException("key length must be 24 !");
		}
		byte[] b = decrypt(hexStr2Bytes(ciphertext), key.substring(0, 24).getBytes());
		if (null != b) {
			return new String(b);
		}
		return null;
	}

	public static byte[] encrypt(byte[] src, byte[] keybyte) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);// 在单一方面的加密或解密
		} catch (java.security.NoSuchAlgorithmException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] src, byte[] keybyte) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (java.lang.Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

}
