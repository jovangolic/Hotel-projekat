package com.jovan.Hotel_projekat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jovan.Hotel_projekat.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
