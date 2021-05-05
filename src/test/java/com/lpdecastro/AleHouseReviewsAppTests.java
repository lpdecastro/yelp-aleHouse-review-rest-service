package com.lpdecastro;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.lpdecastro.api.AleHouseReviewsController;
import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.services.AleHouseReviewsService;
import com.lpdecastro.utils.TestMethodSource;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@SpringBootTest(classes = AleHouseReviewsApp.class,
        webEnvironment = WebEnvironment.RANDOM_PORT)
class AleHouseReviewsAppTests {

    @Autowired
    private AleHouseReviewsController controller;

    @MockBean
    private AleHouseReviewsService service;

    @LocalServerPort
    private int port;

    private WebTestClient wtClient;

    @BeforeEach
    public void init() {
        wtClient = WebTestClient.bindToServer()
                .responseTimeout(java.time.Duration.ofSeconds(60))
                .baseUrl("http://localhost:" + port + TestMethodSource.URL + TestMethodSource.QUERY_STRING)
                .build();
    }

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testInvalidURI() {
        wtClient = WebTestClient.bindToServer()
                .responseTimeout(java.time.Duration.ofSeconds(60))
                .baseUrl("http://localhost:" + port + "/")
                .build();

        wtClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_EXCEPTION).isEqualTo("No handler found for GET /");
    }

    @ParameterizedTest(name = "#{index} params -> {arguments}")
    @MethodSource("com.lpdecastro.utils.TestMethodSource#invalidHttpMethod_source")
    void testInvalidHttpMethod(HttpMethod method, String errMsg) {
        wtClient.method(method)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE)
                .isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_EXCEPTION).isEqualTo(errMsg);
    }

    @Test
    void testInvalidQueryParam() {
        wtClient = WebTestClient.bindToServer()
                .responseTimeout(java.time.Duration.ofSeconds(60))
                .baseUrl("http://localhost:" + port + TestMethodSource.URL + "?locale=INVALID")
                .build();

        wtClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).isEqualTo(HttpStatus.BAD_REQUEST.value())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .jsonPath(TestMethodSource.RESPONSE_ERROR_LOCALE).isEqualTo("must match \"^[a-z]{2,3}_[A-Z]{2}$\"");
    }

    @Test
    void whenValidRequest_returnHttp200AndUpdateQueueId() throws Exception {
        when(service.getAleHouseReviews(any())).thenReturn(new ReviewsDto());

        wtClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}
