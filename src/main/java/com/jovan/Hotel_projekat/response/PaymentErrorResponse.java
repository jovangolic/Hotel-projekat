package com.jovan.Hotel_projekat.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentErrorResponse {

	private String message;
	private int errorCode;
	private LocalDateTime timeStamp;
	
	public PaymentErrorResponse(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
		this.timeStamp = LocalDateTime.now();
	}
	
}
