package study.spring.websocket.helloreactive.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ContextFeatureExample02 {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "id";

    Mono.deferContextual(ctx ->
            Mono.just("Id " + ctx.get(key1))
        )
        .publishOn(Schedulers.parallel())
        .contextWrite(ctx -> ctx.put(key1, "value2"))
        .contextWrite(ctx -> ctx.put(key1, "value1"))
        .subscribe(data -> log.info("subscriber: {}", data));

    Thread.sleep(1000L);
  }

}
