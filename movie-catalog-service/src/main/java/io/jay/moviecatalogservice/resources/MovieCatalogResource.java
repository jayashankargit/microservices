package io.jay.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.jay.moviecatalogservice.models.CatalogItem;
import io.jay.moviecatalogservice.models.CatalogItemMapper;
import io.jay.moviecatalogservice.models.Movie;
import io.jay.moviecatalogservice.models.Rating;
import io.jay.moviecatalogservice.models.UserRating;
import io.jay.moviecatalogservice.service.MovieCatalogService;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired RestTemplate restTemplate;
	@Autowired private WebClient.Builder webClientBuilder;
	@Autowired private DiscoveryClient discoveryClient;
	@Autowired private MovieCatalogService movieCatalogService;
	
	
	@RequestMapping("/{userId}")
	public CatalogItemMapper getResource(@PathVariable("userId") String userId) {
		UserRating userRating = movieCatalogService.getUserRating(userId);
		List<CatalogItem> catalogItemList = getCatalogMovieInfo(userRating.getUserRatings());
		CatalogItemMapper catalogItemMapper = new CatalogItemMapper();
		catalogItemMapper.setCatalogItemList(catalogItemList);
		return catalogItemMapper;
//		return webClientCall(userRating.getRatings());
	}
	
	private List<CatalogItem> getCatalogMovieInfo(List<Rating> ratings) {
		return ratings.stream().map(movieCatalogService::getMovieInfo).collect(Collectors.toList());
	}

	private List<CatalogItem> webClientCall(List<Rating> ratings) {
		return ratings.stream().map(rating-> {
			Movie movie = webClientBuilder.build()
							.get()
							.uri("http://MOVIE-INFO-SERVICES/movies/"+rating.getMovieId())
							.retrieve()
							.bodyToMono(Movie.class)
							.block();
			return new CatalogItem(movie.getName(), "Test", rating.getRating());
		}).collect(Collectors.toList());
	}

	private CatalogItem getMovieInfo(Rating rating1, CatalogItem catalogitem1) {
		return null;
	}

//	public List<CatalogItem> getMovieInfo(List<Rating> ratings) {
//		return movieCatalogService.getMovieInfo(ratings);
//		return ratings.stream().map(t -> {
//			Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICES/movies/"+t.getMovieId(), Movie.class);
//			return new CatalogItem(movie.getName(), "Test", t.getRating());
//		}).collect(Collectors.toList());
//	}
	
}
