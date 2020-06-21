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

	static public String generateToken(String id) throws UnsupportedEncodingException {
		String secretKey = Constant.getJwtKey();
		Encoder encoder = Base64.getEncoder();
		
		String jwt = Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + (3600000)))
				.claim("userId", new String(encoder.encode(id.getBytes()))).claim("iat", System.currentTimeMillis())
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();
		
		return jwt;
	}

	static public boolean validateToken(String token) throws UnsupportedEncodingException {

		if (token == null)
			return false;

		String secretKey = Constant.getJwtKey();
		String[] tokens = token.split("\\.");
		if (tokens.length != 3 || tokens[0] == null || tokens[1] == null || tokens[2] == null)
			return false;

		Decoder decoder = Base64.getDecoder();
		String enPayload = token.split("\\.")[1];
		String dePayload = new String(decoder.decode(enPayload));

		JSONObject obj = new JSONObject(dePayload);

		if (obj.isNull("userId") || obj.isNull("iat") || obj.isNull("exp"))
			return false;

		String jwt = Jwts.builder().claim("exp", obj.get("exp")).claim("userId", obj.get("userId"))
				.claim("iat", obj.get("iat")).signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8")).compact();

		if (token.equals(jwt)) {
			long exp = Long.parseLong(obj.get("exp").toString());
			long currentTime = System.currentTimeMillis();
			if (currentTime > exp * 1000) {
				return false;
			}
			return true;
		}

		return false;

	}

	static public String getUserIdFromToken(String token)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, GeneralSecurityException {

		Decoder decoder = Base64.getDecoder();
		String enPayload = token.split("\\.")[1];
		String dePayload = new String(decoder.decode(enPayload));
		JSONObject obj = new JSONObject(dePayload);

		String encodeduserId = obj.get("userId").toString();
		String userId = new String(decoder.decode(encodeduserId.getBytes()));
		return userId;
	}

}
