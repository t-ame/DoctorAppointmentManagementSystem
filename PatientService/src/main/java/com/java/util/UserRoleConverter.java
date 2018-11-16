package com.java.util;

import javax.persistence.AttributeConverter;

import org.springframework.stereotype.Component;

import com.java.dto.UserRole;

@Component
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

	@Override
	public String convertToDatabaseColumn(UserRole attribute) {
		return attribute.name();
	}

	@Override
	public UserRole convertToEntityAttribute(String dbData) {
		return UserRole.valueOf(dbData);
	}

}
