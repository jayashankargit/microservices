package io.jay.ratingsdataservice.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jay.ratingsdataservice.models.Rating;
import io.jay.ratingsdataservice.models.UserRating;
import io.jay.ratingsdataservice.repo.RatingRepo;

@RestController
@RequestMapping("/ratingsdata")
public class RatingResource {

	@Autowired private RatingRepo ratingRepo;
	
	@RequestMapping("/{movieId}")
	public Rating getRating(@PathVariable("movieId") String movieId) {
		return new Rating(movieId, 4, "jay");
	}
	
	@RequestMapping("/users/{userId}")
	public UserRating getUserRatings(@PathVariable("userId") String userId) {
//		List<Rating> ratings =  Arrays.asList(
//				new Rating("100", 4), 
//				new Rating("200", 3)
//				);
		List<Rating> ratings = ratingRepo.findByUserId(userId);
		UserRating userRating = new UserRating();
		userRating.setUserRatings(ratings);
		return userRating;
	}
}
