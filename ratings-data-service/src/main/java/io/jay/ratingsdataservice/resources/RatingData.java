package io.jay.ratingsdataservice.resources;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import io.jay.ratingsdataservice.models.Rating;
import io.jay.ratingsdataservice.repo.RatingRepo;

@Configuration
public class RatingData implements CommandLineRunner {

	@Autowired private RatingRepo ratingRepo;
	
	@Override
	public void run(String... args) throws Exception {
		List<Rating> ratings = Arrays.asList(
				new Rating("100", 4, "jay"),
				new Rating("200", 3, "jay"));
		
		ratingRepo.saveAll(ratings);
	}
}
