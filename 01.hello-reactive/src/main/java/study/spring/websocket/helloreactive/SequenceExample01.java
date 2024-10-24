package study.spring.websocket.helloreactive;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SequenceExample01 {

  public static void main(String[] args) {
    Flux<String> coldFlux = Flux.fromIterable(Arrays.asList("RED", "YELLOW", "PINK"))
        .map(String::toLowerCase);

    coldFlux.subscribe(data -> log.info("Subscriber 1: {}", data));
    log.info("========================");
    coldFlux.subscribe(data -> log.info("Subscriber 2: {}", data));
  }

}
