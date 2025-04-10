package com.jovan.Hotel_projekat.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jovan.Hotel_projekat.model.User;
import com.jovan.Hotel_projekat.repository.UserRepository;

import lombok.RequiredArgsConstructor;




@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {

	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException("User not found."));
		return HotelUserDetails.buildUserDetails(user);
	}
	
}
