package study.spring.websocket.bookservice;

import lombok.Getter;

@Getter
public class Book {

  private Long id;

  private String name;

  public Book(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
