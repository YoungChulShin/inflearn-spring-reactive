package study.spring.websocket.helloreactive.scheduler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class SchedulerOperatorExample05 {

  public static void main(String[] args) throws InterruptedException {
    Flux.fromArray(new Integer[] {1, 3, 5, 7})
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext(data -> log.info("fromArray: {}", data))
        .filter(data -> data > 3)
        .doOnNext(data -> log.info("filter: {}", data))
        .publishOn(Schedulers.parallel())
        .map(data -> data * 10)
        .doOnNext(data -> log.info("map: {}", data))
        .subscribe(data -> log.info("Subscribe: {}", data));

    Thread.sleep(500L);
  }
}
