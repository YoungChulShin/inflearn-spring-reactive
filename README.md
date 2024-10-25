# 저장소 설명
인프런 `Kevin의 알기 쉬운 Spring Reactive` 강의 학습 저장소 

# 강의 메모
## Hot & Cold sequence
cold sequence
- subscribe 할 때마다 타임라인이 만들어진다. 
- 사용자 1이 A producer를 subscribe하고 몇 초 뒤에 사용자2가 A producer를 subscribe 하면 사용자2는 A producer를 처음부터 subscribe 한다. 

hot sequence
- timeline이 1개로 유지된다. 
- 사용자 1이 A producer를 subscribe하고 몇 초 뒤에 사용자2가 A producer를 subscribe 하면 사용자2는 subscribe하는 시점부터의 producer 데이터를 받게 된다. 
- `share()` 메서드를 이용해서 cold sequence를 hot sequence로 변경할 수 있다. 

## Backpressure
개념
- 들어오는 데이터를 제어해서 과부하가 걸리지 않도록 한다. 
- publisher에서 emit되는 데이터를 subscriber에서 안정적으로 처리하기 위한 제어. 

방법
1. subscriber에서 요청 데이터 수를 제어
   - subscriber가 처리할 수 있는 수량의 데이터를 publisher에 요청. 
   - 예시
     ```java
     public static void main(String[] args) {
        Flux.range(1, 5)
            .doOnNext(x -> log.info("doOnNext: {}", x)) // upstream에서 emit한 데이터를 로그로 출력
            .doOnRequest(x -> log.info("doOnRequest: {}", x)) // subscriber에서 요청한 데이터의 수를 출력
            .subscribe(new BaseSubscriber<Integer>() {  // BaseSubscriber를 이용해서 reuqest 수를 조정
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                // subscribe 시점에 2개를 요청
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                count++;
                log.info("hookOnNext: {}", value);

                // 2개의 데이터를 받았을 때, 2초를 쉬었다가 2개를 다시 요청
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
     }
     ```
2. reactor가 제공하는 backpressure 전략을 사용
   1. ignore
   2. error: downstream으로 전달할 버퍼가 가득차면 에러를 throw한다. (`reactor.core.Exceptions$OverflowException`)
   3. drop: 버퍼가 가득차면 다음에 들어오는 데이터는 버퍼가 비워질 때까지 drop 된다.
   4. latest: 버퍼가 가득차면 다음에 들어오는 데이터는 버퍼가 비워질 때까지 최신의 데이터만 남게 된다. 
      - 버퍼가 다 차고 1이 들어오면 1은 남아있는다. 이후에 2가 들어오면 1이 drop되고 2가 남게 된다. 
   5. buffer-drop-latest: 버퍼가 가득차면 최근에 버퍼로 들어온 데이터를 drop한다. 
   6. buffer-drop-olest: 버퍼가 가득차면 처음 들어온 데이터를 drop한다. 

## Sinks
개념
- Reactive Stream에서 발생하는 signal을 프로그래밍 적으로 push 할 수 있는 기능을 가지는 publisher의 일종이다. 
- Thread-safe 하지 않을 수 있는 processor보다 더 나은 대안이 된다. 
   - 기존의 processor는 onNext, onComplete같은 signal을 직접 처리하고 있기 때문
- Thread-safe하게 signal을 발생시킨다. 

종류
- SinkOne
   - 1개의 데이터만 전달할 수 있는 producer. Mono와 같은 개념. 
   - 2개를 emit하면 예외가 발생한다. 
   - 예제
      ```java
      One<String> sinkOne = Sinks.one();
      Mono<String> mono = sinkOne.asMono();

      sinkOne.emitValue("Hello Reactor", EmitFailureHandler.FAIL_FAST);
      ```
- SinkMany
   - 여러 데이터를 emit 할 수 있는 producer. Flux와 같은 개념. 
   - 전략
      - unicast: 1개의 subscriber만 가질 수 있다. 2개 이상 subscriber가 추가되면 에러가 발생한다. 
         ```java
         Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
         Flux<Integer> fluxView = unicastSink.asFlux();
         ```
      - multicast: 2개 이상의 subscriber를 가질 수 있다. `hot sequence` 방식으로 동작한다. 
         ```java
         Many<Integer> unicastSink = Sinks.many().multicast().onBackpressureBuffer();
         Flux<Integer> fluxView = unicastSink.asFlux();
         ```
      - replay: 이전 데이터를 가져올 수 있는 옵션을 제공한다. 
         - limit(n): subscriber 시점에 최근 n개 만큼의 데이터를 가져온다.
           ```java
           Many<Integer> unicastSink = Sinks.many().replay().limit(2);
           Flux<Integer> fluxView = unicastSink.asFlux();
           ```
         - all(): 전체 데이터를 가져온다. 
           ```java
           Many<Integer> unicastSink = Sinks.many().replay().all();
           Flux<Integer> fluxView = unicastSink.asFlux();
           ```