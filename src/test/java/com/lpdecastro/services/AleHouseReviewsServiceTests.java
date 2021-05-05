package com.lpdecastro.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpdecastro.config.GoogleVisionApiConfig;
import com.lpdecastro.config.YelpApiConfig;
import com.lpdecastro.dtos.google.vision.FaceAnnotationDto;
import com.lpdecastro.dtos.google.vision.FaceAnnotationsDto;
import com.lpdecastro.dtos.google.vision.ResponsesDto;
import com.lpdecastro.dtos.yelp.ReviewDto;
import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.dtos.yelp.UserDto;
import com.lpdecastro.exceptions.AleHouseReviewsException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@SpringBootTest
class AleHouseReviewsServiceTests {

    @MockBean
    YelpApiConfig yelpApiConfig;

    @MockBean
    GoogleVisionApiConfig googleVisionApiConfig;

    @SpyBean
    AleHouseReviewsService service;

    private MockWebServer mockWebServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
    }

    @AfterEach
    public void shutdown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void testContextLoads() {
        assertThat(service).isNotNull();
    }

    @Test
    void whenSuccessfulApiRequest_thenHttp200_thenReturnResponse() throws Exception {
        UserDto user = new UserDto();
        user.setId("testUserId01");
        user.setProfileUrl(new URL("https://testUrl/testProfile01"));
        user.setImageUrl(new URL("https://testUrl/testImage01.jpg"));

        ReviewDto review = new ReviewDto();
        review.setId("testReviewId01");
        review.setUrl(new URL("https://testUrl/testUrl01"));
        review.setText("Test Text 01");
        review.setRating(10);
        review.setTimeCreated("05/05/2021 01:01:01");
        review.setUser(user);

        List<ReviewDto> reviewList = new ArrayList<>();
        reviewList.add(review);

        ReviewsDto reviews = new ReviewsDto(reviewList);

        FaceAnnotationDto faceAnnotation = new FaceAnnotationDto();
        faceAnnotation.setJoyLikelihood("VERY_LIKELY");
        faceAnnotation.setSorrowLikelihood("VERY_UNLIKELY");

        List<FaceAnnotationDto> faceAnnotationList = new ArrayList<>();
        faceAnnotationList.add(faceAnnotation);

        FaceAnnotationsDto faceAnnotations = new FaceAnnotationsDto(faceAnnotationList);

        List<FaceAnnotationsDto> responseList = new ArrayList<>();
        responseList.add(faceAnnotations);

        ResponsesDto responses = new ResponsesDto(responseList);

        String url = String.format("http://localhost:%s", mockWebServer.getPort());

        when(yelpApiConfig.getBaseUrl()).thenReturn(url);
        when(yelpApiConfig.getApiKey()).thenReturn("TestApiKey01");
        when(yelpApiConfig.getAleHouseId()).thenReturn("TestAleHouseId01");
        when(googleVisionApiConfig.getBaseUrl()).thenReturn(url);
        when(googleVisionApiConfig.getApiKey()).thenReturn("TestApiKey01");

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(mapper.writeValueAsString(reviews)));

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(mapper.writeValueAsString(responses)));

        final ReviewsDto response = assertDoesNotThrow(
                () -> service.getAleHouseReviews("en_PH"));

        assertThat(response.getReviews().get(0).getId()).isEqualTo("testReviewId01");
        assertThat(response.getReviews().get(0).getUrl()).isEqualTo(new URL("https://testUrl/testUrl01"));
        assertThat(response.getReviews().get(0).getText()).isEqualTo("Test Text 01");
        assertThat(response.getReviews().get(0).getRating()).isEqualTo(10);
        assertThat(response.getReviews().get(0).getTimeCreated()).isEqualTo("05/05/2021 01:01:01");
        assertThat(response.getReviews().get(0).getUser().getId()).isEqualTo("testUserId01");
        assertThat(response.getReviews().get(0).getUser().getProfileUrl())
                .isEqualTo(new URL("https://testUrl/testProfile01"));
        assertThat(response.getReviews().get(0).getUser().getImageUrl())
                .isEqualTo(new URL("https://testUrl/testImage01.jpg"));
        assertThat(response.getReviews().get(0).getUser().getEmotions().get(0).getJoyLikelihood())
                .isEqualTo("VERY_LIKELY");
        assertThat(response.getReviews().get(0).getUser().getEmotions().get(0).getSorrowLikelihood())
                .isEqualTo("VERY_UNLIKELY");

    }

    @Test
    void whenYelpApiFailed_thenHttp400_thenThrowException() throws Exception {
        String url = String.format("http://localhost:%s", mockWebServer.getPort());

        when(yelpApiConfig.getBaseUrl()).thenReturn(url);
        when(yelpApiConfig.getApiKey()).thenReturn("TestApiKey01");
        when(yelpApiConfig.getAleHouseId()).thenReturn("TestAleHouseId01");
        when(googleVisionApiConfig.getBaseUrl()).thenReturn(url);
        when(googleVisionApiConfig.getApiKey()).thenReturn("TestApiKey01");

        mockWebServer.enqueue(new MockResponse().setResponseCode(400)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("ERROR"));

        final AleHouseReviewsException exception = assertThrows(AleHouseReviewsException.class,
                () -> service.getAleHouseReviews("en_PH"));

        assertThat(exception.getMessage()).isEqualTo("Call to external webservice failed.");
    }

    @Test
    void whenGoogleVisionApiFailed_thenHttp400_thenThrowException() throws Exception {
        UserDto user = new UserDto();
        user.setId("testUserId01");
        user.setProfileUrl(new URL("https://testUrl/testProfile01"));
        user.setImageUrl(new URL("https://testUrl/testImage01.jpg"));

        ReviewDto review = new ReviewDto();
        review.setId("testReviewId01");
        review.setUrl(new URL("https://testUrl/testUrl01"));
        review.setText("Test Text 01");
        review.setRating(10);
        review.setTimeCreated("05/05/2021 01:01:01");
        review.setUser(user);

        List<ReviewDto> reviewList = new ArrayList<>();
        reviewList.add(review);

        ReviewsDto reviews = new ReviewsDto(reviewList);

        String url = String.format("http://localhost:%s", mockWebServer.getPort());

        when(yelpApiConfig.getBaseUrl()).thenReturn(url);
        when(yelpApiConfig.getApiKey()).thenReturn("TestApiKey01");
        when(yelpApiConfig.getAleHouseId()).thenReturn("TestAleHouseId01");
        when(googleVisionApiConfig.getBaseUrl()).thenReturn(url);
        when(googleVisionApiConfig.getApiKey()).thenReturn("TestApiKey01");

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody(mapper.writeValueAsString(reviews)));

        mockWebServer.enqueue(new MockResponse().setResponseCode(400)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("ERROR"));

        final AleHouseReviewsException exception = assertThrows(AleHouseReviewsException.class,
                () -> service.getAleHouseReviews("en_PH"));

        assertThat(exception.getMessage()).isEqualTo("Call to external webservice failed.");
    }

}
