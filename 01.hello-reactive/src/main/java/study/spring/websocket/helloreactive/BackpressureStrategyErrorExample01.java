package study.spring.websocket.helloreactive;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class BackpressureStrategyErrorExample01 {

  public static void main(String[] args) throws InterruptedException {
    Flux
        .interval(Duration.ofMillis(1L))
        .onBackpressureError()
        .doOnNext(data -> log.info("doOnNext: {}", data))
        .publishOn(Schedulers.parallel())
        .subscribe(
            data -> {
              try {
                Thread.sleep(5L);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }

              log.info("onNext: {}" , data);
            },
            error -> {
              // reactor.core.Exceptions$OverflowException: 에러 발생
              log.error("onError", error);
            }
        );

    Thread.sleep(2000L);
  }

}
