package com.lpdecastro.utils;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
public class TestMethodSource {

    public static final String URL = "/yelp/aleHouse/reviews";
    public static final String QUERY_STRING = "?locale=en_PH";
    public static final String RESPONSE_ERROR_MESSAGE = "$.error.message";
    public static final String RESPONSE_ERROR_CODE = "$.error.code";
    public static final String RESPONSE_ERROR_EXCEPTION = "$.error.detail.exception";
    public static final String RESPONSE_ERROR_LOCALE = "$.error.detail.locale";

    /**
     * @return
     */
    static Stream<Arguments> invalidHttpMethod_source() {
        return Stream.of(
                Arguments.of(HttpMethod.PUT, "Request method 'PUT' not supported"),
                Arguments.of(HttpMethod.POST, "Request method 'POST' not supported"),
                Arguments.of(HttpMethod.DELETE, "Request method 'DELETE' not supported"));
    }

    /**
     * @return
     */
    static Stream<Arguments> invalidHttpMethodController_source() {
        return Stream.of(
                Arguments.of(MockMvcRequestBuilders.put(URL),
                        "Request method 'PUT' not supported"),
                Arguments.of(MockMvcRequestBuilders.post(URL),
                        "Request method 'POST' not supported"),
                Arguments.of(MockMvcRequestBuilders.delete(URL),
                        "Request method 'DELETE' not supported"));
    }
}
