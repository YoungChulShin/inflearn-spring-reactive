package study.spring.websocket.helloreactive;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class FluxExample03 {

  public static void main(String[] args) {
    Mono.justOrEmpty(null)
        .concatWith(Mono.justOrEmpty("Jobs"))
        .concatWith(Mono.justOrEmpty("YC"))
        .map(data -> "Hello " + data)
        .subscribe(
            result -> log.info("onNext: {}", result),
            error -> {},
            () -> log.info("onComplete")
        );

  }

}
