package study.spring.websocket.bookservice;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Book {

  private Long id;

  private String name;

  private String author;

  private String isbn;

  private String publishDate;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public Book(
      Long id,
      String name,
      String author,
      String isbn,
      String publishDate,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.name = name;
    this.author = author;
    this.isbn = isbn;
    this.publishDate = publishDate;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
