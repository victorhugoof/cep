package br.com.convcard.apicepreactive.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Book {
    @Id
    private Long id;
    private String title;
    private String author;
}
