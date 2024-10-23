package study.spring.websocket.helloreactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxExample02 {

  public static void main(String[] args) {
    Flux.fromArray(new Integer[] {3, 6, 7, 9})
        .filter(num -> num > 6)
        .map(num -> num * 2)
        .subscribe(result -> log.info("result {}", result));
  }

}
