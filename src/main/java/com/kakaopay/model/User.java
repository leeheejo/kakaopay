package com.kakaopay.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "user")
//@Getter
//@Setter
//@AllArgsConstructor
public class User {

//	@Id
	private String id;
	private String password;

}
