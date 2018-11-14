package com.java.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {

	private int statusCode;
	private String errorMessage;
	private LocalDateTime timestamp;
}
