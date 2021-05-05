package com.lpdecastro.dtos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeName("error")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonPropertyOrder(value = { "code", "message", "detail" })
public class ErrorResponseDto {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("detail")
    @Setter(value = AccessLevel.NONE)
    private final Map<Object, List<String>> detail = new LinkedHashMap<>();

    public ErrorResponseDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addDetail(String field, String value) {
        List<String> valueList;
        if (detail.containsKey(field)) { // check for existing field
            valueList = detail.get(field);

        } else {
            valueList = new ArrayList<>();
        }

        valueList.add(value);
        detail.put(field, valueList);
    }
}
