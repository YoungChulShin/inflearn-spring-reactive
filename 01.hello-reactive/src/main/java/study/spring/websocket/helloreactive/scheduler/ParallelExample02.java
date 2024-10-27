package study.spring.websocket.helloreactive.scheduler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ParallelExample02 {

  public static void main(String[] args) throws InterruptedException {
    Flux.fromArray(new Integer[] {1, 3, 5, 7, 9})
        .parallel()
        .runOn(Schedulers.parallel())
        .subscribe(data -> log.info("subscribe: {}", data));

    Thread.sleep(2000);
  }

}
