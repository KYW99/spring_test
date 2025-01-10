package com.example.article.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO {

    private int no;

    private String title;
    private String content;
    private String author;
    private LocalDateTime rdate;
}
