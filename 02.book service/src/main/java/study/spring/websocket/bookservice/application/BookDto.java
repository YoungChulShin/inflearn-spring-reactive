package study.spring.websocket.bookservice.application;

import java.time.LocalDateTime;
import lombok.Data;

public class BookDto {

  @Data
  public static class Post {

    private Long id;

    private String name;

    private String author;

    private String isbn;

    private String publishDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
  }

  @Data
  public static class Patch {

    private Long id;

    private String name;

    private String author;

    private String isbn;

    private String publishDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
  }

  @Data
  public static class Response {

    private Long id;

    private String name;

    private String author;

    private String isbn;

    private String publishDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
  }

}
