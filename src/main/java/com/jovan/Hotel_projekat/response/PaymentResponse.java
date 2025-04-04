package com.jovan.Hotel_projekat.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

	private Long id;
	
	private Double amount;
	
	private String currency;
	
	private String status; 	
}
