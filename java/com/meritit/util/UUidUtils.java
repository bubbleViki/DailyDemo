package com.meritit.util;

import java.util.UUID;

public class UUidUtils {

	public static String generateUUid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
