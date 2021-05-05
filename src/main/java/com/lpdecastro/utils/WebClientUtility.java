package com.lpdecastro.utils;

import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.lpdecastro.dtos.google.vision.ResponsesDto;
import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.exceptions.AleHouseReviewsException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@Slf4j
public class WebClientUtility {

    private static final String LOG_TEMPLATE = "{}::{}() - {}";

    private final WebClient webClient;

    public WebClientUtility(String baseUrl, String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public ReviewsDto executeYelpApiRequest(String path, String locale) {
        try {
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParamIfPresent("locale", Optional.ofNullable(locale))
                            .build())
                    .retrieve()
                    .bodyToMono(ReviewsDto.class)
                    .toFuture()
                    .join();

        } catch (WebClientResponseException | CompletionException | CancellationException ex) {
            log.error(LOG_TEMPLATE, getClass().getSimpleName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

            throw new AleHouseReviewsException("Call to external webservice failed.");
        }
    }

    public ResponsesDto executeGoogleVisionApiRequest(String requestsString) {
        try {
            return this.webClient.post()
                    .bodyValue(requestsString)
                    .retrieve()
                    .bodyToMono(ResponsesDto.class)
                    .toFuture()
                    .join();

        } catch (WebClientResponseException | CompletionException | CancellationException ex) {
            log.error(LOG_TEMPLATE, getClass().getSimpleName(),
                    Thread.currentThread().getStackTrace()[1].getMethodName(), ex);

            throw new AleHouseReviewsException("Call to external webservice failed.");
        }
    }
}
