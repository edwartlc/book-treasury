package com.edwartlc.book_treasury.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record Data(
        List<BookData> results
) {
}
