package com.kakaopay.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import java.util.Base64.Decoder;

import org.json.JSONObject;
import org.junit.Test;

public class JWTUtilsTests {

	@Test
	public void generateToken() throws Exception {
		JWTUtils jwtUtils = new JWTUtils();
		String jwt = jwtUtils.generateToken("testId");
		assertThat(jwt).isNotNull();
	}

	@Test
	public void generatvalidateTokeneToken() throws Exception {
		JWTUtils jwtUtils = new JWTUtils();
		boolean result = jwtUtils.validateToken(
				"eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTQyNzQ5NjksInVzZXJJZCI6IlpFZFdlbVJGYkd0T1ZGa3oiLCJpYXQiOjE1OTE2ODI3OTMyMjB9.U2fBICMxqVXedM2BQwhFZ_jClGVBqAPg5z4YrYVvK4o");
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(true);
	}
	
//	@Test
//	static public void getUserIdFromToken() throws Exception {
//
////		Decoder decoder = Base64.getDecoder();
////		String enPayload = token.split("\\.")[1];
////		String dePayload = new String(decoder.decode(enPayload));
////		JSONObject obj = new JSONObject(dePayload);
////
////		String userId = obj.get("userId").toString();
////		userId = new String(decoder.decode(userId.getBytes()));
////		return userId;
//	}

}
