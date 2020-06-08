package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnToken {
	private String token;

	@Builder
	public ReturnToken(String token) {
		super();
		this.token = token;
	}
}
