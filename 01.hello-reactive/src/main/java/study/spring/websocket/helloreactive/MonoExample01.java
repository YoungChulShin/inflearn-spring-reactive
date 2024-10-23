package study.spring.websocket.helloreactive;

import org.slf4j.Logger;
import reactor.core.publisher.Mono;

public class MonoExample01 {

  public static void main(String[] args) {
    Mono.just("Hello Reactor")
        .subscribe(data -> System.out.println(data + " emitted"));
  }
}
