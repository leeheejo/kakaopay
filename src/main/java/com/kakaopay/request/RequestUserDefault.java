package com.kakaopay.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class RequestUserDefault {

	@NotBlank(message = "[userId] is Null")
	String userId;
	@NotBlank(message = "[password] is Null")
	String password;
}
