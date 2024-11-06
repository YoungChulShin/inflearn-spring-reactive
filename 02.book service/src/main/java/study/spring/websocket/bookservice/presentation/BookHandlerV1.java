package study.spring.websocket.bookservice.presentation;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import study.spring.websocket.bookservice.application.BookDto.Patch;
import study.spring.websocket.bookservice.application.BookDto.Post;
import study.spring.websocket.bookservice.application.BookMapper;
import study.spring.websocket.bookservice.domain.Book;

@Component
@RequiredArgsConstructor
public class BookHandlerV1 {

  private final BookMapper bookMapper;

  public Mono<ServerResponse> createBook(ServerRequest request) {
    return request.bodyToMono(Post.class)
        .map(bookMapper::bookPostToBook)
        .flatMap(book ->
          ServerResponse
              .created(URI.create("/route/v1/books/" + book.getId()))
              .build());
  }

  public Mono<ServerResponse> getBook(ServerRequest request) {
    long bookId = Long.parseLong(request.pathVariable("book-id"));
    Book book =
        new Book(
            bookId,
            "Java 고급",
            "ycshin",
            "111-11-1111-111-1",
            "2024-01-01",
            LocalDateTime.now(),
            LocalDateTime.now());
    return ServerResponse
        .ok()
        .bodyValue(bookMapper.bookToResponse(book))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> updateBook(ServerRequest request) {
    final long bookId = Long.parseLong(request.pathVariable("book-id"));
    return request
        .bodyToMono(Patch.class)
        .map(patch -> {
          patch.setId(bookId);
          return bookMapper.bookPatchToBook(patch);
        })
        .flatMap(book -> ServerResponse.ok().bodyValue(bookMapper.bookToResponse(book)));
  }

  public Mono<ServerResponse> getBooks(ServerRequest request) {
    List<Book> books = List.of(
        new Book(
            1L,
            "Java 고급 1",
            "ycshin",
            "111-11-1111-111-1",
            "2024-01-01",
            LocalDateTime.now(),
            LocalDateTime.now()),
        new Book(
            2L,
            "Java 고급 2",
            "ycshin",
            "111-11-1111-111-1",
            "2024-01-01",
            LocalDateTime.now(),
            LocalDateTime.now()),
        new Book(
            3L,
            "Java 고급 3",
            "ycshin",
            "111-11-1111-111-1",
            "2024-01-01",
            LocalDateTime.now(),
            LocalDateTime.now())
    );

    return ServerResponse
        .ok()
        .bodyValue(bookMapper.booksToResponse(books));
  }


}
