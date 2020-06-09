package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnDefault {

	private int code;
	private String message;
	private Object result;

	@Builder
	public ReturnDefault(int code, String message, Object result) {
		super();
		this.code = code;
		this.message = message;
		this.result = result;
	}

}
