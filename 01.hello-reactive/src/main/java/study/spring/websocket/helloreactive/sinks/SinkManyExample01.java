package study.spring.websocket.helloreactive.sinks;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;

@Slf4j
public class SinkManyExample01 {

  public static void main(String[] args) {
    Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
    Flux<Integer> fluxView = unicastSink.asFlux();

    unicastSink.emitNext(1, EmitFailureHandler.FAIL_FAST);
    unicastSink.emitNext(2, EmitFailureHandler.FAIL_FAST);

    fluxView.subscribe(data -> log.info("subscriber1: {}", data));

    unicastSink.emitNext(2, EmitFailureHandler.FAIL_FAST);

    fluxView.subscribe(data -> log.info("subscriber2: {}", data));
  }

}
