package study.spring.websocket.helloreactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelloReactor {

  public static void main(String[] args) {
    // Publisher가 데이터를 생성
    Flux<String> sequence = Flux.just("Hello", "Reactor");
    sequence
        // 데이터를 가공
        .map(data -> data.toLowerCase())
        // 가공한 데이터를 구독
        // data -> System.out.println(data) 이게 Subscriber
        .subscribe(data -> System.out.println(data));

    // Subscriber의 onSubscribe 호출
  }
}
