package study.spring.websocket.helloreactive.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ContextExample01 {

  public static void main(String[] args) throws InterruptedException {
    String key = "message";

    Mono<String> mono = Mono.deferContextual(ctx ->
            Mono.just("Hello " + ctx.get(key))
                .doOnNext(data -> log.info("onNext: {}", data)))
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.parallel())
        .transformDeferredContextual((mono2, ctx) -> mono2.map(data -> data + " " + ctx.get(key)))
        .contextWrite(context -> context.put(key, "Reactor"));

    mono.subscribe(data -> log.info("subscribe: {}", data));

    Thread.sleep(1000L);
  }

}
