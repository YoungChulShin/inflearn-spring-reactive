package study.spring.websocket.helloreactive.debug;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

@Slf4j
public class DebugExample02 {

  public static void main(String[] args) {
    // Global debug mode 활성화
    Hooks.onOperatorDebug();

    Flux.just(2, 4, 6, 8)
        .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
        .subscribe(
            data -> log.info("subscribe: {}", data),
            error -> log.error("error", error));
  }

}
