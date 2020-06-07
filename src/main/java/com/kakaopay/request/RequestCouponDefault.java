package com.kakaopay.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCouponDefault {
	@NotBlank(message = "[coupon] is Null")
	private String coupon;

}
