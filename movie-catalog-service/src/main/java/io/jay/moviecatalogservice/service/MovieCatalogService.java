package io.jay.moviecatalogservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.jay.moviecatalogservice.models.CatalogItem;
import io.jay.moviecatalogservice.models.Movie;
import io.jay.moviecatalogservice.models.Rating;
import io.jay.moviecatalogservice.models.UserRating;

@Service
public class MovieCatalogService {

	@Autowired private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallbackUserRating", 
			commandProperties = {
				@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
				@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
				@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
				@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
	})
	public UserRating getUserRating(String userId) {
		return restTemplate.getForObject("http://RATINGS-DATA-SERVICE/ratingsdata/users/"+userId, UserRating.class);
	}

	public UserRating getFallbackUserRating(String userId) {
		UserRating userRating = new UserRating();
		userRating.setUserRatings(Arrays.asList(
				new Rating("Movie not found", 0))
				);
		return userRating;
	}
	
	@HystrixCommand(fallbackMethod = "getFallbackMovieInfo",
			threadPoolKey = "movieThread",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "20"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
	}
	)
	public CatalogItem getMovieInfo(Rating rating) {
		Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICES/movies/" + rating.getMovieId(), Movie.class);
		return new CatalogItem(movie.getName(), "Test", rating.getRating());
	}
	
	public CatalogItem getFallbackMovieInfo(Rating rating){
		return new CatalogItem("Movie not found", "", rating.getRating());
	}
}
