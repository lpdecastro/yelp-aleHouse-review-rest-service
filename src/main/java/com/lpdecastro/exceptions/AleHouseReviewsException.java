package com.lpdecastro.exceptions;

import lombok.Getter;

/**
 * @author liandre.p.de.castro
 *
 * @since 2021/05/05
 */
@Getter
public class AleHouseReviewsException
        extends RuntimeException {

    private static final long serialVersionUID = -2346268788730400478L;

    private final String message;

    public AleHouseReviewsException(String message) {
        super(message);
        this.message = message;
    }
}
