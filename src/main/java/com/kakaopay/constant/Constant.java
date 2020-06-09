package com.kakaopay.constant;

import lombok.Getter;

public final class Constant {
	final static private String JWT_KEY = "7J207Z2s7KGwMTIzDQo=";
	final static private String SHA256_KEY = "7Iqk7ZSE66eBNDU2";

	public enum RES {
		STATUS_OK(200, "OK"), INVALID_REQUESTBODY(99, "Invalid RequestBody"), INVALID_COUPON(98, "Invalid Coupon"),
		INVALID_TOKEN(97, "Invalid Token"), EXPIRED_COUPON(96, "Expired Coupon"),
		USERID_NOT_EXIST(95, "UserId is not exist"), NO_COUPON(94, "No Coupon to Issue"),
		USERID_ALREADY_USED(93, "UserId already Used"), INCORRECT_PASSWORD(92, "Password is incorrect"),
		FAIL(91, "Something wrong");

		private int code;
		private String message;

		private RES(int code, final String message) {
			// TODO Auto-generated constructor stub
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	public Constant() {
	}

	public static String getSha256Key() {
		return SHA256_KEY;
	}

	public static String getJwtKey() {
		return JWT_KEY;
	}
}
