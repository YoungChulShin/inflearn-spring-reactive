package study.spring.websocket.bookservice;

import org.mapstruct.Mapper;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface BookMapper {

  Book bookPostToBook(BookDto.Post requestBody);

  Book bookPathToBook(BookDto.Patch requestBody);

  BookDto.Response bookToResponse(Book book);

  default Mono<BookDto.Response> bookToBookResponse(Mono<Book> mono) {
    return mono.flatMap(book -> Mono.just(bookToResponse(book)));
  }
}
