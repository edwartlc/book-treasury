package com.edwartlc.book_treasury.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record BookData(
       String title,
       @JsonAlias("authors") List<AuthorData> author,
       @JsonAlias("languages") List<String> language,
       @JsonAlias("download_count") Integer downloadCount
) {
}
