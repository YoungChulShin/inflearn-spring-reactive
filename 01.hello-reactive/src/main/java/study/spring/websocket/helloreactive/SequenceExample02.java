package study.spring.websocket.helloreactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class SequenceExample02 {

  public static void main(String[] args) throws InterruptedException {
    // share()를 사용하면 cold sequence를 hot sequence로 변경한다.
    Flux<String> concertFlux = Flux.fromStream(
            Stream.of("Singer A", "Singer B", "Singer C", "Singer D", "Singer E"))
        .delayElements(Duration.ofSeconds(1L)).share();

    concertFlux.subscribe(data -> log.info("Subscriber 1: {}", data));

    Thread.sleep(2500);

    concertFlux.subscribe(data -> log.info("Subscriber 2: {}", data));

    Thread.sleep(10000);
  }

}
