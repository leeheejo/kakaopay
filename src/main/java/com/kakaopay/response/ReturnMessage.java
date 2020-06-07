package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnMessage {

	private String message;

	@Builder
	public ReturnMessage(String message) {
		super();
		this.message = message;
	}

}
