package study.spring.websocket.helloreactive.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

@Slf4j
public class ContextExample02 {

  public static void main(String[] args) throws InterruptedException {
    String key1 = "id";
    String key2 = "name";

    Mono<String> mono = Mono.deferContextual(ctx ->
            Mono.just("Id " + ctx.get(key1) + ", Name: " + ctx.get(key2))
        )
        .publishOn(Schedulers.parallel())
        .contextWrite(Context.of(key1, "YC", key2, "go1323"));

    mono.subscribe(data -> log.info("onNext: {}", data));

    Thread.sleep(1000L);
  }

}
