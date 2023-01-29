package br.com.convcard.apicepreactive.repository;

import br.com.convcard.apicepreactive.domain.Book;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookRepository extends R2dbcRepository<Book, Long> {

}
