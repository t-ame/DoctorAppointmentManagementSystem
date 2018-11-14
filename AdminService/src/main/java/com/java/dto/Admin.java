package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "admin")
@Data
@Entity
@SQLDelete(sql = "update Admin set active = false where adminId = ?")
public class Admin {
	@Id
	@GeneratedValue
	private int adminId;
	private String email;
	private String password;
	private boolean active;
}
