package study.spring.websocket.bookservice.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import study.spring.websocket.bookservice.application.BookMapper;
import study.spring.websocket.bookservice.application.BookServiceV2;
import study.spring.websocket.bookservice.application.BookDto.Patch;
import study.spring.websocket.bookservice.application.BookDto.Post;
import study.spring.websocket.bookservice.application.BookDto.Response;

/**
 * V1에서 DtoMapping 간에 블로킹 요소를 제거
 */
@RestController
@RequestMapping("/v2/books")
@RequiredArgsConstructor
public class BookControllerV2 {

  private final BookServiceV2 bookService;
  private final BookMapper bookMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Response> postBook(@RequestBody Mono<Post> requestBody) {
    return bookService.createBook(requestBody)
        .flatMap(data -> Mono.just(bookMapper.bookToResponse(data)));
  }

  @PatchMapping("/{book-id}")
  public Mono<Response> patchBook(
      @PathVariable(value = "book-id") Long bookId,
      @RequestBody Mono<Patch> requestBody) {
    return bookService.updateBook(bookId, requestBody)
        .flatMap(data -> Mono.just(bookMapper.bookToResponse(data)));
  }

  @GetMapping("/{book-id}")
  public Mono<Response> getBook(@PathVariable(value = "book-id") Long bookId) {
    return bookService.findBook(bookId)
        .flatMap(data -> Mono.just(bookMapper.bookToResponse(data)));
  }
}

