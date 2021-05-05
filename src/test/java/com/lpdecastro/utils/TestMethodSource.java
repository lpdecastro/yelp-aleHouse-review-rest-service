package com.lpdecastro.utils;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author liandre.p.de.castro
 *
 * @since 2020/10/19
 */
public class TestMethodSource {

    public static final String URL = "/yelp/aleHouse/reviews";
    public static final String QUERY_STRING = "?locale=en_PH";
    public static final String RESPONSE_ERROR_MESSAGE = "$.error.message";
    public static final String RESPONSE_ERROR_CODE = "$.error.code";
    public static final String RESPONSE_ERROR_EXCEPTION = "$.error.detail.exception";
    public static final String RESPONSE_ERROR_LOCALE = "$.error.detail.locale";
    public static final String ADAPTER_CLASS = "com.lpdecastro.rd.poc.eaz.helper.StandardEazAdapter";
    public static final String EAZ_FOLDER_PATH = "../ee_eaz-disb_name_list_get/src/test/resources/eazBaseFolder";
    public static final String EAZ_FOLDER_PATH_NO_CATEGORIES = "../ee_eaz-disb_name_list_get/src/test/resources/eazBaseFolder_empty";

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

    /**
     * @return
     */
    static Stream<Arguments> validRequest_source() {
        final Set<String> disbNames01 = doSome();
        final Set<String> disbNames02 = doSome2();
        return Stream.of(
                Arguments.of("user1", "model-01", disbNames01, "$[0]", is("disbName-01-01")),
                Arguments.of("", "model-02", disbNames02, "$[0]",
                        anyOf(is("disbName-01-01"), is("disbName-01-02"), is("disbName-02-02"))));
    }

    /**
     * @return
     */
    static Stream<Arguments> validRequestController_source() {
        final Set<String> disbNames00 = new HashSet<>();
        final Set<String> disbNames01 = doSome();
        final Set<String> disbNames02 = doSome2();
        return Stream.of(
                Arguments.of("user1", "model-00", disbNames00, jsonPath("$").isEmpty()),
                Arguments.of("user1", "model-01", disbNames01, jsonPath("$[0]").value("disbName-01-01")),
                Arguments.of("", "model-02", disbNames02, jsonPath("$[0]")
                        .value(anyOf(is("disbName-01-01"), is("disbName-01-02"), is("disbName-02-02")))));
    }

    /**
     * @return
     */
    static Stream<Arguments> validRequestService_source() {
        return Stream.of(
                Arguments.of("", "model-01", 1, new String[] { "disb_name_01-01-01" }),
                Arguments.of("user1", "model-00-noServerXml", 0, new String[] {}),
                Arguments.of("user1", "model-01", 1, new String[] { "disb_name_01-01-01" }),
                Arguments.of("user1", "model-02", 2,
                        new String[] { "disb_name_01-02-01", "disb_name_02-02-01" }),
                Arguments.of("user1", "model-03", 3,
                        new String[] { "disb_name_01-03-01", "disb_name_02-03-01", "disb_name_03-03-01" }),
                Arguments.of("user1", "model-04", 4,
                        new String[] { "disb_name_01-04-01", "disb_name_02-04-01", "disb_name_03-04-01",
                                "disb_name_04-04-01" }),
                Arguments.of("user1", "model-05", 5,
                        new String[] { "disb_name_01-05-01", "disb_name_02-05-01", "disb_name_03-05-01",
                                "disb_name_04-05-01", "disb_name_05-05-01" }),
                Arguments.of("user1", "model-06-multipleNodes", 6,
                        new String[] { "disb_name_06-06-01", "disb_name_06-06-02", "disb_name_06-06-03",
                                "disb_name_06-06-04", "disb_name_06-06-05", "disb_name_06-06-06" }),
                Arguments.of("user1", "model-07-duplicateNodes", 7,
                        new String[] { "disb_name_07-07-01", "disb_name_07-07-02", "disb_name_07-07-03",
                                "disb_name_07-07-04", "disb_name_07-07-05", "disb_name_07-07-06-duplicate",
                                "disb_name_07-07-07" }),
                Arguments.of("user1", "model-08-emptyNode", 0, new String[] {}));
    }

    /**
     * @return
     */
    static Stream<Arguments> missingAndEmptyModel_source() {
        final String nullModel = null;
        return Stream.of(
                Arguments.of(nullModel),
                Arguments.of(""),
                Arguments.of("   "));
    }

    private static Set<String> doSome() {
        final Set<String> disbNames = new HashSet<>();
        disbNames.add("disbName-01-01");
        return disbNames;
    }

    private static Set<String> doSome2() {
        final Set<String> disbNames = new HashSet<>();
        disbNames.add("disbName-01-01");
        disbNames.add("disbName-01-02");
        disbNames.add("disbName-02-02");
        return disbNames;
    }
}
