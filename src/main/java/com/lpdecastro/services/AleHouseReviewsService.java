package com.lpdecastro.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lpdecastro.dtos.yelp.ReviewsDto;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
public interface AleHouseReviewsService {

    ReviewsDto getAleHouseReviews(String locale) throws JsonProcessingException;
}
