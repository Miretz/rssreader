package com.semerad.rss.service.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashUtil {

	private PasswordHashUtil() {
		// Empty
	}

	public static String createHash(final String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	public static boolean validatePassword(final String password, final String hashedPasswordFromDb) {
		return BCrypt.checkpw(password, hashedPasswordFromDb);
	}
}
