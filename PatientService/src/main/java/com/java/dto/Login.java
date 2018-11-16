package com.java.dto;

import javax.persistence.Convert;

import com.java.util.UserRoleConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {

	private String userName;
	private String userPassword;
	@Convert(converter = UserRoleConverter.class)
	private UserRole userRole;
	private int userId;
	private boolean enabled = true;

}
