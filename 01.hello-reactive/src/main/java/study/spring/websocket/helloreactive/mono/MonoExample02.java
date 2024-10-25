package study.spring.websocket.helloreactive.mono;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoExample02 {

  public static void main(String[] args) {
    Mono.empty()
        .subscribe(
            data -> log.info("emitted data: {}", data),
            error -> { },
            () -> log.info("completed")
        );
  }
}
