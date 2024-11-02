package study.spring.websocket.helloreactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class StepVerifierTestExample01 {

  @Test
  void sayHelloReactorTest() {
    StepVerifier
        .create(Mono.just("Hello Reactor"))
        .expectNext("Hello Reactor")  // onNext Signal에 대한 emit 된 데이터
        .expectComplete() // onCompete 검증
        .verify();  // 검증 실행

  }

}
