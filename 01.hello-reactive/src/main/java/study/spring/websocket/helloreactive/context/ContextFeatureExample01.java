package study.spring.websocket.helloreactive.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

@Slf4j
public class ContextFeatureExample01 {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "id";

    Mono<String> mono = Mono.deferContextual(ctx ->
            Mono.just("Id " + ctx.get(key1))
        )
        .publishOn(Schedulers.parallel());

    mono.contextWrite(ctx -> ctx.put(key1, "value1"))
            .subscribe(data -> log.info("subscribe1: {}", data));

    // subscriber마다 context가 있어야 한다.
    mono.contextWrite(ctx -> ctx.put(key1, "value2"))
        .subscribe(data -> log.info("subscribe2: {}", data));
//    mono.subscribe(data -> log.info("subscribe2: {}", data)); -> 에러

    Thread.sleep(1000L);
  }

}
