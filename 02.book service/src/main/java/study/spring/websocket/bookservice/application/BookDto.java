package study.spring.websocket.bookservice.application;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

public class BookDto {

  @Data
  public static class Post {

    @NotNull
    private Long id;

    @NotEmpty
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

    @NotEmpty
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
