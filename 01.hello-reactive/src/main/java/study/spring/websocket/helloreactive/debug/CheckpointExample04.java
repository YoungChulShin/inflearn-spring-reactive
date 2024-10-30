package study.spring.websocket.helloreactive.debug;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CheckpointExample04 {

  public static void main(String[] args) {
    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
        .checkpoint("checkpoint 01")
        .map(num -> num + 2)
        .subscribe(
            data -> log.info("subscribe: {}", data),
            error -> log.error("error", error)
        );
  }

}
