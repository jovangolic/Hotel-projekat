package com.jovan.Hotel_projekat.exceptions;


public class PaymentErrorException extends RuntimeException {

	public PaymentErrorException(String message) {
		super(message);
	}
}
