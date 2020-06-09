package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnMessage {

	private int code;
	private String message;

	@Builder
	public ReturnMessage(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
