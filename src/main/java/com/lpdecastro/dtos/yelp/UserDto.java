package com.lpdecastro.dtos.yelp;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lpdecastro.dtos.google.vision.FaceAnnotationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDto {

    private String id;

    private URL profileUrl;

    private URL imageUrl;

    private String name;

    private List<FaceAnnotationDto> emotions;
}
