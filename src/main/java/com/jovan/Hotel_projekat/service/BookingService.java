package com.jovan.Hotel_projekat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jovan.Hotel_projekat.exceptions.InvalidBookingRequestException;
import com.jovan.Hotel_projekat.exceptions.ResourceNotFoundException;
import com.jovan.Hotel_projekat.model.BookedRoom;
import com.jovan.Hotel_projekat.model.Room;
import com.jovan.Hotel_projekat.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{
	
	private final BookingRepository bookingRepository;
	
	private final IRoomService roomService;
	
	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepository.findAll();
	}

	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		//ako je korisnik uneo validne podatke
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must come before check-out date");
		}
		Room room = roomService.getRoomById(roomId).get();
		//ako je ta soba slobodna, onda vadimo sve podatke o toj sobi
		//dobijamo celu booking istoriju za tu sobu
		List<BookedRoom> existingBookings = room.getBookings();
		//provera da li je soba bukirana za navedeni datum
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings); 
		if(roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		}
		else {
			throw new InvalidBookingRequestException("This room has been booked for the selected dates.Not Available.");
		}
		//ako je sve u redu, onda korisniku vracamo confirmation code
		return bookingRequest.getBookingConfirmationCode();
	}

	

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepository.findByBookingConfirmationCode(confirmationCode).
				orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
	}

	//metoda koja proverava da li je soba slobodna za rezervaciju
	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepository.findByGuestEmail(email);
	}
}
