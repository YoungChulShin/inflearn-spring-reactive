package study.spring.websocket.helloreactive.sinks;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.core.publisher.Sinks.Many;

@Slf4j
public class SinkManyExample04 {

  public static void main(String[] args) {
    // multicast: hot sequence
    Many<Integer> unicastSink = Sinks.many().replay().limit(2);
    Flux<Integer> fluxView = unicastSink.asFlux();

    unicastSink.emitNext(1, EmitFailureHandler.FAIL_FAST);
    unicastSink.emitNext(2, EmitFailureHandler.FAIL_FAST);

    fluxView.subscribe(data -> log.info("subscriber1: {}", data));

    unicastSink.emitNext(3, EmitFailureHandler.FAIL_FAST);

    fluxView.subscribe(data -> log.info("subscriber2: {}", data));

    unicastSink.emitNext(4, EmitFailureHandler.FAIL_FAST);
  }

}
