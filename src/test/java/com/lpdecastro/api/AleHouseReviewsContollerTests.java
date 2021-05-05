package com.lpdecastro.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.lpdecastro.dtos.yelp.ReviewsDto;
import com.lpdecastro.services.AleHouseReviewsService;
import com.lpdecastro.utils.TestMethodSource;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@WebMvcTest(controllers = AleHouseReviewsController.class)
class AleHouseReviewsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AleHouseReviewsService service;

    @SpyBean
    private AleHouseReviewsController controller;

    @Test
    @DisplayName("smoketest - context loads")
    void testContextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void whenInvalidURI_returnHttp404() throws Exception {
        mockMvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE)
                        .value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_EXCEPTION).value("No handler found for GET /"))
                .andDo(print());
    }

    @ParameterizedTest(name = "#{index} params -> {arguments}")
    @MethodSource("com.lpdecastro.utils.TestMethodSource#invalidHttpMethodController_source")
    void whenInvalidMethod_returnHttp405(MockHttpServletRequestBuilder method, String errMsg) throws Exception {
        mockMvc.perform(method
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).value(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE)
                        .value(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_EXCEPTION).value(errMsg))
                .andDo(print());
    }

    @Test
    void whenInvalidQueryParam_returnHttp400() throws Exception {
        mockMvc.perform(get(TestMethodSource.URL)
                .queryParam("locale", "INVALID")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_CODE).value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_MESSAGE)
                        .value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath(TestMethodSource.RESPONSE_ERROR_LOCALE)
                        .value("must match \"^[a-z]{2,3}_[A-Z]{2}$\""))
                .andDo(print());
    }

    @Test
    void whenValidRequest_returnHttp200() throws Exception {
        when(service.getAleHouseReviews(any())).thenReturn(new ReviewsDto());

        mockMvc.perform(get(TestMethodSource.URL)
                .queryParam("locale", "en_PH")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
