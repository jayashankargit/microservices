package io.jay.ratingsdataservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.jay.ratingsdataservice.models.Rating;

public interface RatingRepo extends JpaRepository<Rating, Integer> {

	public List<Rating> findByUserId(String userId);
}
