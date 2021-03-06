package com.kakaopay.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDTO {
	private String token;

	@Builder
	public TokenDTO(String token) {
		super();
		this.token = token;
	}
}
