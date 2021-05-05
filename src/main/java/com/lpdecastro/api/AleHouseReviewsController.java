package com.lpdecastro.api;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.services.AleHouseReviewsService;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@RestController
@Validated
public class AleHouseReviewsController {

    @Autowired
    private AleHouseReviewsService service;

    @GetMapping(path = "/yelp/aleHouse/reviews",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ReviewsDto getYelpReviewsController(
            @RequestParam(value = "locale",
                    required = false) @Pattern(regexp = "^[a-z]{2,3}_[A-Z]{2}$") String locale)
            throws JsonProcessingException {
        return service.getAleHouseReviews(locale);
    }
}
