package io.jay.ratingsdataservice.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(value=RatingId.class)
@Getter @Setter
@NoArgsConstructor
public class Rating {

//	@EmbeddedId
//	private RatingId ratingId;
	@Id private String movieId;
	@Id private String userId;
	private int rating;
	
	public Rating(String movieId, int rating, String userId) {
		this.rating = rating;
//		this.ratingId.setMovieId(movieId);
//		this.ratingId.setUserId(userId);
		this.movieId = movieId;
		this.userId = userId;
	}
	
}
