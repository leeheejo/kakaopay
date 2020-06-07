package com.kakaopay.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakaopay.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

	Coupon findOneByCoupon(String coupon);

	Coupon findTop1ByIsIssued(boolean isIssued);

	List<Coupon> findByIsIssued(boolean isIssued);

	Coupon findOneByCouponAndIsIssuedAndIsUsed(String coupon, boolean isIssued, boolean isUsed);

	List<Coupon> findByExpiredAtBetweenAndIsUsed(LocalDateTime start, LocalDateTime end, boolean isUsed);
}
