package com.tsinghua.course.Frame.Util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class SecureUtil
{
	/**
	 * 使用加盐MD5计算用户密码的哈希值
	 */
	public static String getHashedPassword(String useId, String password) {
		String saltPassword = useId + password;
		return DigestUtils.md5DigestAsHex(saltPassword.getBytes(StandardCharsets.UTF_8));
	}
}
