package com.kakaopay.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kakaopay.constant.Constant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {
	private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

	public JWTUtils() {
		super();
	}

//	// icontrols1793!@# base64인코딩하고앞에 난수(dlswmd) 붙임
//	public static String ACCESS_TOEKN_SECRET_KEY = "dlswmdaWNvbnRyb2xzMTc5MyFAIw==";
//	// icontrolsrefresh1793!@#
//	public static String REFRESH_TOKEN_SECRET_KEY = "aWNvbnRyb2xzcmVmcmVzaDE3OTMhQCM=";

	static public String generateToken(String id) throws UnsupportedEncodingException {
		String secretKey = Constant.JWT_KEY;

		Encoder encoder = Base64.getEncoder();

		String jwt = Jwts.builder().setExpiration(new Date(259200000)) // 1일 -> 설정정보로 변경해야함
				.claim("userId", encoder.encode(id.getBytes())).claim("iat", System.currentTimeMillis() / 1000)
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();
		System.out.println(jwt);

		return jwt;
	}

	static public boolean validateToken(String token) throws UnsupportedEncodingException {

		String secretKey = Constant.JWT_KEY;
		logger.info("origin = " + token);
		String[] tokens = token.split("\\.");
		if (tokens.length != 3 || tokens[0] == null || tokens[1] == null || tokens[2] == null)
			return false;

		Decoder decoder = Base64.getDecoder();
		// 시그니처가 맞는지 확인
		String enPayload = token.split("\\.")[1];
		String dePayload = new String(decoder.decode(enPayload));

		JSONObject obj = new JSONObject(dePayload);

		if (obj.isNull("userId") || obj.isNull("iat") || obj.isNull("exp"))
			return false;

		String jwt = Jwts.builder().claim("exp", obj.get("exp")) // 1시간 -> 설정정보로 변경해야함
				.claim("userId", obj.get("userId")).claim("iat", obj.get("iat"))
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();

		logger.info("new = " + jwt);
		if (token.equals(jwt)) {
			long iat = Long.parseLong(obj.get("iat").toString());
			long currentTime = System.currentTimeMillis() / 1000;
			if (currentTime > iat + Long.parseLong(obj.get("exp").toString())) {
				return false;
			}
			return true;
		}

		return false;

	}
//
//	static public JWTInfo getJWTInfoFromToken(String token) {
//
//		Decoder decoder = Base64.getDecoder();
//		String enPayload = token.split("\\.")[1];
//		String dePayload = new String(decoder.decode(enPayload));
//		logger.info("P= " + enPayload);
//		JSONObject obj = new JSONObject(dePayload);
//
//		JWTInfo jwtInfo = new JWTInfo();
//		jwtInfo.setCmpIp(obj.get("cmpIp").toString());
//		jwtInfo.setHomeId(obj.get("homeId").toString());
//		jwtInfo.setClientInfo(obj.get("clientInfo").toString());
//
//		return jwtInfo;
//	}
//
//	static public String getCmpIpFromToken(String token) {
//
//		Decoder decoder = Base64.getDecoder();
//		String enPayload = token.split("\\.")[1];
//		String dePayload = new String(decoder.decode(enPayload));
//		logger.info("P= " + enPayload);
//		JSONObject obj = new JSONObject(dePayload);
//
//		String cmpIp = obj.get("cmpIp").toString();
//		Decoder dcoder = Base64.getDecoder();
//		cmpIp = new String(decoder.decode(cmpIp));
//
//		return cmpIp;
//	}
//
//	static public String getHomeIdFromToken(String token)
//			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
//
//		Decoder decoder = Base64.getDecoder();
//		String enPayload = token.split("\\.")[1];
//		String dePayload = new String(decoder.decode(enPayload));
//		JSONObject obj = new JSONObject(dePayload);
//
//		String encodedHomeId = obj.get("homeId").toString();
//		AES256Util aes = new AES256Util();
//		String homeId = aes.decrypt(encodedHomeId);
//
//		return homeId;
//	}
//
//	static public String getClientInfoFromToken(String token)
//			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {
//
//		Decoder decoder = Base64.getDecoder();
//		String enPayload = token.split("\\.")[1];
//		String dePayload = new String(decoder.decode(enPayload));
//		JSONObject obj = new JSONObject(dePayload);
//
//		String clientInfo = obj.get("clientInfo").toString();
//
//		return clientInfo;
//	}

}
