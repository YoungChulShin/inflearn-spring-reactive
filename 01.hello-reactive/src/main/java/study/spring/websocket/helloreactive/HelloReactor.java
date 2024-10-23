package study.spring.websocket.helloreactive;

import reactor.core.publisher.Mono;

public class HelloReactor {

  public static void main(String[] args) {
    Mono.just("Hello Reactor")
        .subscribe(System.out::println);
  }
}
