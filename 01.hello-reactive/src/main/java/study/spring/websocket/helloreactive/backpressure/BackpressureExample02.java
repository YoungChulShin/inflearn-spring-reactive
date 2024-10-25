package study.spring.websocket.helloreactive.backpressure;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureExample02 {

  public static int count = 0;

  public static void main(String[] args) {
    Flux.range(1, 5)
        .doOnNext(x -> log.info("doOnNext: {}", x)) // upstream에서 emit한 데이터를 로그로 출력
        .doOnRequest(x -> log.info("doOnRequest: {}", x)) // subscriber에서 요청한 데이터의 수를 출력
        .subscribe(new BaseSubscriber<Integer>() {
          @Override
          protected void hookOnSubscribe(Subscription subscription) {
            request(2);
          }

          @Override
          protected void hookOnNext(Integer value) {
            count++;
            log.info("hookOnNext: {}", value);

            if (count == 2) {
              try {
                Thread.sleep(2000L);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }

              request(2);
              count = 0;
            }
          }

          @Override
          protected void hookOnComplete() {
            log.info("hookOnComplete");
          }
        });
  }

}
