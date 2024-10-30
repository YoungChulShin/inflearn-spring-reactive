package study.spring.websocket.helloreactive.debug;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class CheckpointExample06 {

  public static void main(String[] args) {
    Flux<Integer> source = Flux.just(2, 4, 6, 8);
    Flux<Integer> other = Flux.just(1, 2, 3, 0);

    Flux<Integer> multiplySource = divide(source, other).checkpoint();
    Flux<Integer> plusSource = plus(multiplySource).checkpoint();

    plusSource.subscribe(
        data -> log.info("subscribe: {}", data),
        error -> log.error("error", error)
    );
  }

  private static Flux<Integer> divide(Flux<Integer> source, Flux<Integer> other) {
    return source.zipWith(other, (x, y) -> x / y);
  }

  private static Flux<Integer> plus(Flux<Integer> source) {
    return source.map(data -> data + 2);
  }

}
