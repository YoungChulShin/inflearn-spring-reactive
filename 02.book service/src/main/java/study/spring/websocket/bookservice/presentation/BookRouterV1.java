package study.spring.websocket.bookservice.presentation;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration(value = "BookRouterV1")
public class BookRouterV1 {

  @Bean
  public RouterFunction<?> routeBook(BookHandlerV1 handler) {
    return route()
        .POST("/route/v1/books", handler::createBook)
        .PATCH("/route/v1/books/{book-id}", handler::updateBook)
        .GET("/route/v1/books", handler::getBooks)
        .GET("/route/v1/books/{book-id}", handler::getBook)
        .build();


  }
}
