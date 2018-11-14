package com.java.dto;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import org.hibernate.annotations.DynamicUpdate;

import com.java.util.UserRoleConverter;

import lombok.Data;

@Data
@Entity
@DynamicUpdate
public class Login {
	@Id
	@Email
	@Column(length = 100, nullable = false)
	private String userName;
	@Column(length = 100, nullable = false)
	private String userPassword;
//	@Enumerated(EnumType.STRING)
	@Convert(converter = UserRoleConverter.class)
	@Column(length = 20, nullable = false)
	private UserRole userRole;
	@Column(nullable = false)
	private int userId;

}
