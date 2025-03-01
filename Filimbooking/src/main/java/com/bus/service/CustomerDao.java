package com.bus.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.bus.beans.Admin;
import com.bus.beans.CurrentDateOperation;
import com.bus.beans.Customer;
import com.bus.beans.MovieDetails;
import com.bus.beans.OrderHistory;
import com.bus.beans.Seat;

@Component
public class CustomerDao {
	
	@Autowired
	private CustomerRepo repo;
	
	@Autowired
	private SeatRepo repo1;
	
	@Autowired
	private HistoryRepo repo2;
	
	@Autowired
	private MovieRepo movieRepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private AdminRepo adminRepo;
	
	public boolean isAdmin(String username, String password) {
        Admin admin = adminRepo.findByUsernameAndPassword(username, password);
        return admin != null;
    }
	
	public int save(Customer customer) {
		
		repo.save(customer);
		return 1;
		
	}
	
//	@Cacheable(cacheNames = "login", key = "'customer'+#email+#password")
	public Customer login(String email, String password) {
		Customer customer = repo.findByEmailAndPassword(email, password);
		return customer;
	}
	
	public int saveSeat(Seat seat, Customer customer, Date date, String time){
		List<Seat> list = new ArrayList<Seat>();
		list.add(seat);
		customer.setSeat(list);
		CurrentDateOperation cdo= new CurrentDateOperation();
		cdo.setShowDate(date);
		cdo.setShowTime(time);
		cdo.setSeat(list);
		
		seat.setOperation(cdo);
		seat.setOperation(cdo);
		seat.setCustomer(customer);
		Seat save = repo1.save(seat);
		return 1;
	}
	
	public List<Seat> getSeats(long id){
		List<Seat> list = repo1.getAllSeat(id);
		return list;
	}
	
	public List<Customer> getAll(){
		List<Customer> findAll = repo.findAll();
		return findAll;
	}
	
	public OrderHistory saveHistory(OrderHistory history, Customer customer) {
		customer.setHistory(history);
		OrderHistory save = repo2.save(history);
		return save;
	}
	
//	@Cacheable(cacheNames = "history", key = "#id")
	public List<OrderHistory> getAllHistory(long id){
		List<OrderHistory> list = repo2.getAllHistory(id);		
		return list;
	}
	
	public List<Seat> getAllSeat(LocalDate date, String time){
		List<Seat> list = repo1.getAllByDate(date, time);
		return list;
	}
	
	public void delete(long id) {
		repo1.deleteById(id);
	}
	
	public int updateDetail(Customer customer) {
		repo.save(customer);
		return 1;
	}
	
	public List<MovieDetails> getAllMovie(){
		List<MovieDetails> list = this.movieRepo.findAll();
		return list;
	}
	
	public void sendBookingConfirmation(String toMail,String movieName,String seatNumbers,String showDate,String showTime,double totalPrice) {
		SimpleMailMessage message= new SimpleMailMessage();
		message.setTo(toMail);
		message.setSubject("Your booking at Karur Cinemas is confirmed");
		message.setText("Dear Customer, \n Your Booking is Confirmed for the movie: "+movieName+"\n Seats: "+seatNumbers+
				"\n Your ShowDate: "+showDate+
				"\n ShowTiming: "+showTime+"\n Total Price: "+totalPrice+
				"\n Enjoy Your Movie at Karur Cinemas!!");
		
		mailSender.send(message);
	}
	// Save a new movie
		public int saveMovie(MovieDetails movie) {
			movieRepo.save(movie);
			return 1;
		}

		// Update an existing movie
		public int updateMovie(MovieDetails movie) {
			Optional<MovieDetails> existingMovie = movieRepo.findById(movie.getMovieId());
		    if (existingMovie.isPresent()) {
		        MovieDetails upMovie= existingMovie.get();
		        upMovie.setMovieName(movie.getMovieName());
		        upMovie.setMovieDetails(movie.getMovieDetails());
		        upMovie.setImage(movie.getImage());
		        movieRepo.save(upMovie);
		        return 1;
		    }
		    return 0;
		}

		// Delete a movie by ID
		public void deleteMovie(long id) {
			movieRepo.deleteById(id);
		}

		// Get a movie by ID
		public MovieDetails getMovieById(long id) {
			Optional<MovieDetails> movie = movieRepo.findById(id);
			return movie.orElse(null);
		}
	
	
	

}
