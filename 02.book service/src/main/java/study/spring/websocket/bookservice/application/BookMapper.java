package study.spring.websocket.bookservice.application;

import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;
import study.spring.websocket.bookservice.domain.Book;
import study.spring.websocket.bookservice.application.BookDto.Patch;
import study.spring.websocket.bookservice.application.BookDto.Post;
import study.spring.websocket.bookservice.application.BookDto.Response;

@Mapper(componentModel = "spring")
public interface BookMapper {

  Book bookPostToBook(Post requestBody);

  Book bookPatchToBook(Patch requestBody);

  Response bookToResponse(Book book);

  default Mono<Response> bookToBookResponse(Mono<Book> mono) {
    return mono.flatMap(book -> Mono.just(bookToResponse(book)));
  }
}
