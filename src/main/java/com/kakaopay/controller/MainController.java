package com.kakaopay.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.constant.Constant;
import com.kakaopay.exeption.ExpiredCouponException;
import com.kakaopay.model.Coupon;
import com.kakaopay.request.RequestCouponDefault;
import com.kakaopay.response.ReturnDefault;
import com.kakaopay.response.CouponDTO;
import com.kakaopay.response.CouponListDTO;
import com.kakaopay.response.ReturnMessage;
import com.kakaopay.service.CouponService;
import com.mysql.cj.protocol.Message;

@RestController
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	@Autowired
	CouponService couponService;

	@PostMapping(value = "/coupon/{N}")
	public @ResponseBody ResponseEntity<Object> generateCoupon(@RequestHeader HttpHeaders headers,
			@PathVariable Long N) {
		couponService.generateCoupon(N);

		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).build();

		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@PutMapping(value = "/coupon/issue")
	public @ResponseBody ResponseEntity<Object> issueCoupon(@RequestHeader HttpHeaders headers) {

		String couponNum = couponService.issueCoupon();
		if (couponNum.equals("")) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			ReturnDefault msg = ReturnDefault.builder().code(Constant.RES.STATUS_OK.getCode())
					.message(Constant.RES.STATUS_OK.getMessage()).result(new CouponDTO(couponNum)).build();
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/coupon/issue")
	public @ResponseBody ResponseEntity<Object> findIssuedCoupon(@RequestHeader HttpHeaders headers) {

		List<Coupon> issuedCoupon = couponService.findIssuedCoupon();
		ReturnDefault msg = ReturnDefault.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).result(new CouponListDTO(issuedCoupon)).build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@PutMapping(value = "/coupon/use")
	public @ResponseBody ResponseEntity<Object> useCoupon(@RequestHeader HttpHeaders headers,
			final @Valid @RequestBody RequestCouponDefault coupon) throws ExpiredCouponException {
		boolean currentUseStatue = false;
		couponService.changeUseCoupon(coupon.getCoupon(), currentUseStatue);
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@PutMapping(value = "/coupon/cancel")
	public @ResponseBody ResponseEntity<Object> cancelCoupon(@RequestHeader HttpHeaders headers,
			final @Valid @RequestBody RequestCouponDefault coupon) throws ExpiredCouponException {
		boolean currentUseStatue = true;
		couponService.changeUseCoupon(coupon.getCoupon(), currentUseStatue);
		ReturnMessage msg = ReturnMessage.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	@GetMapping(value = "/coupon/expire")
	public @ResponseBody ResponseEntity<Object> findExpireTodayCoupon(@RequestHeader HttpHeaders headers) {

		List<Coupon> issuedCoupon = couponService.getExpireTodayCoupon();
		ReturnDefault msg = ReturnDefault.builder().code(Constant.RES.STATUS_OK.getCode())
				.message(Constant.RES.STATUS_OK.getMessage()).result(new CouponListDTO(issuedCoupon)).build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

}
