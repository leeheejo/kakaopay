package com.kakaopay.constant;

import lombok.Getter;

public class Constant {
	final static private String JWT_KEY = "7J207Z2s7KGwMTIzDQo=";
	final static private String SHA256_KEY = "7Iqk7ZSE66eBNDU2";
	final static public String STATUS_OK = "OK";

	public Constant() {
	}

	public static String getSha256Key() {
		return SHA256_KEY;
	}

	public static String getJwtKey() {
		return JWT_KEY;
	}
}
