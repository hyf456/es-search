package com.hivescm.escenter.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DongChunfu on 2017/8/25
 *
 * copy http://www.cnblogs.com/sunzn/p/3455135.html
 */
public class MD5Util {
	public static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
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
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			// never happend !
			e.printStackTrace();
		}
		return result;
	}
}
