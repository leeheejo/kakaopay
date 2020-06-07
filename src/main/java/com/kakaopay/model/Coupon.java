package com.kakaopay.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coupon")
@Getter
@Setter
public class Coupon extends BaseTimeEntity {

	@Id
	private String coupon;
	private boolean isUsed = false;
	private boolean isIssued = false;
	private LocalDateTime issuedAt = null;
	private LocalDateTime expiredAt = null;

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
		LocalDate temp = LocalDate.of(issuedAt.getYear(), issuedAt.getMonth(), issuedAt.getDayOfMonth()).plusDays(8);
		this.expiredAt = temp.atStartOfDay().minusSeconds(1);
	}

	public Coupon() {
		super();
	}

	public Coupon(String coupon) {
		super();
		this.coupon = coupon;
	}

	@Override
	public String toString() {
		return "Coupon [coupon=" + coupon + ", isUsed=" + isUsed + ", isIssued=" + isIssued + ", issuedAt=" + issuedAt
				+ ", createdAt=" + getCreatedAt() + "]";
	}

}
