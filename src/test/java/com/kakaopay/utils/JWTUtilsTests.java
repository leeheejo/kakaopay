package com.kakaopay.utils;

import static org.assertj.core.api.Assertions.assertThat;

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
				"eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjI1OTIwMCwidXNlcklkIjoiWkVkV2VtUkZiR3M9IiwiaWF0IjoxNTkxNTczMDgwfQ.9lxDVmTIdgNfIN--_Fu8_vYbM8FtGR6Ie1QQAuRR65g");
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(true);
	}

}
