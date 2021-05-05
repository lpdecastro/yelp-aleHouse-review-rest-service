package com.lpdecastro.dtos.google.vision;

import java.util.List;

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
public class FaceAnnotationsDto {

    private List<FaceAnnotationDto> faceAnnotations;
}
