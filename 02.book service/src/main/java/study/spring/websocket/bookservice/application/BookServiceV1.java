package study.spring.websocket.bookservice.application;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import study.spring.websocket.bookservice.domain.Book;

@Service
public class BookServiceV1 {

  public Mono<Book> createBook(Book book) {
    return Mono.just(book);
  }

  public Mono<Book> updateBook(Book book) {
    return Mono.just(book);
  }

  public Mono<Book> findBook(long bookId) {
    return Mono.just(
        new Book(
            bookId,
            "Java 고급",
            "ycshin",
            "111-11-1111-111-1",
            "2024-01-01",
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    );
  }
}
