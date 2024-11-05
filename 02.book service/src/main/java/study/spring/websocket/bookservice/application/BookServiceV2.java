package study.spring.websocket.bookservice.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import study.spring.websocket.bookservice.domain.Book;

@Service
@RequiredArgsConstructor
public class BookServiceV2 {

  private final BookMapper bookMapper;

  public Mono<Book> createBook(Mono<BookDto.Post> book) {
    return book.flatMap(post -> Mono.just(bookMapper.bookPostToBook(post)));
  }

  public Mono<Book> updateBook(Long bookId, Mono<BookDto.Patch> book) {
    return book.flatMap(patch -> {
      patch.setId(bookId);
      return Mono.just(bookMapper.bookPatchToBook(patch));
    });
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
