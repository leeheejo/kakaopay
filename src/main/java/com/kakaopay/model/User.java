package com.kakaopay.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
public class User extends BaseTimeEntity {

	@Id
	private String userId;
	private String password;

	public User() {
		super();
	}

	public User(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

}
