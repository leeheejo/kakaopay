package com.kakaopay.response;

import java.util.List;

import com.kakaopay.model.Coupon;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnCouponList {

	private int totalCount;
	private List<Coupon> coupons;

	@Builder
	public ReturnCouponList(List<Coupon> coupons) {
		super();
		this.totalCount = coupons.size();
		this.coupons = coupons;
	}

}
