package study.spring.websocket.helloreactive.sinks;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.One;

@Slf4j
public class SinkOneExample01 {

  public static void main(String[] args) {
    One<String> sinkOne = Sinks.one();
    Mono<String> mono = sinkOne.asMono();

    sinkOne.emitValue("Hello Reactor", EmitFailureHandler.FAIL_FAST);

    mono.subscribe(data -> log.info("Subscriber1: {}" , data));
    mono.subscribe(data -> log.info("Subscriber2: {}" , data));
  }

}
