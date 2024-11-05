package study.spring.websocket.bookservice;

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
import study.spring.websocket.bookservice.BookDto.Response;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;
  private final BookMapper bookMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BookDto.Response> postBook(@RequestBody BookDto.Post requestBody) {
    Mono<Book> book = bookService.createBook(bookMapper.bookPostToBook(requestBody));

    return bookMapper.bookToBookResponse(book);
  }

  @PatchMapping("/{book-id}")
  public Mono<BookDto.Response> patchBook(
      @PathVariable(value = "book-id") Long bookId,
      @RequestBody BookDto.Patch requestBody) {
    requestBody.setId(bookId);
    Mono<Book> book = bookService.updateBook(bookMapper.bookPathToBook(requestBody));

    return bookMapper.bookToBookResponse(book);
  }

  @GetMapping("/{book-id}")
  public Mono<BookDto.Response> getBook(@PathVariable(value = "book-id") Long bookId) {
    Mono<Book> book = bookService.findBook(bookId);

    return bookMapper.bookToBookResponse(book);
  }




}

