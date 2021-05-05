package com.lpdecastro.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpdecastro.config.GoogleVisionApiConfig;
import com.lpdecastro.config.YelpApiConfig;
import com.lpdecastro.dtos.google.vision.FeatureDto;
import com.lpdecastro.dtos.google.vision.ImageDto;
import com.lpdecastro.dtos.google.vision.RequestDto;
import com.lpdecastro.dtos.google.vision.RequestsDto;
import com.lpdecastro.dtos.google.vision.ResponsesDto;
import com.lpdecastro.dtos.google.vision.SourceDto;
import com.lpdecastro.dtos.yelp.ReviewDto;
import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.utils.WebClientUtility;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@Service
public class AleHouseReviewsServiceImpl implements AleHouseReviewsService {

    @Autowired
    private YelpApiConfig yelpApiConfig;

    @Autowired
    private GoogleVisionApiConfig googleVisionApiConfig;

    @Override
    public ReviewsDto getAleHouseReviews(String locale) throws JsonProcessingException {

        WebClientUtility yelpClient = new WebClientUtility(yelpApiConfig.getBaseUrl(), yelpApiConfig.getApiKey());

        ReviewsDto reviews = yelpClient.executeYelpApiRequest(
                String.format("/%s/reviews", yelpApiConfig.getAleHouseId()),
                locale);

        RequestsDto requests = generateRequestsForGoogleVisionApi(reviews);

        String requestsString = new ObjectMapper().writeValueAsString(requests);

        WebClientUtility googleVisionClient = new WebClientUtility(googleVisionApiConfig.getBaseUrl(),
                googleVisionApiConfig.getApiKey());

        ResponsesDto responses = googleVisionClient.executeGoogleVisionApiRequest(requestsString);

        for (int i = 0; i < reviews.getReviews().size(); i++) {
            reviews.getReviews().get(i).getUser().setEmotions(responses.getResponses().get(i).getFaceAnnotations());
        }

        return reviews;
    }

    private RequestsDto generateRequestsForGoogleVisionApi(ReviewsDto reviews) {
        List<RequestDto> requestList = new ArrayList<>();

        for (ReviewDto review : reviews.getReviews()) {
            SourceDto source = new SourceDto();
            source.setImageUri(review.getUser().getImageUrl().toString());

            FeatureDto feature = new FeatureDto();
            feature.setType("FACE_DETECTION");
            feature.setMaxResults(10);

            List<FeatureDto> features = new ArrayList<>();
            features.add(feature);

            ImageDto image = new ImageDto();
            image.setSource(source);

            RequestDto request = new RequestDto();
            request.setImage(image);
            request.setFeatures(features);

            requestList.add(request);
        }

        return new RequestsDto(requestList);
    }

}
