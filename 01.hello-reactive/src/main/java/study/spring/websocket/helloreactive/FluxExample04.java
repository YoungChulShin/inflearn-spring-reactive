package study.spring.websocket.helloreactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxExample04 {

  public static void main(String[] args) {
    Flux.concat(
//        Mono.just("Venus"),
            Flux.just("Earth"),
            Flux.just("Gold"),
            Flux.just("Mars", "Sun"))
        .collectList()
        .subscribe(data -> log.info("onNext: {}", data));
  }

}
