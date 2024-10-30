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

## Scheduler
종류
- publishOn(): Operator 체인에서 DownstreamOperator의 실행을 위한 스레드를 지정한다. 
- subscribeOn(): 최 상위 Upstream Publisher의 실행을 위한 스레드를 지정한다. 원본 데이터 소스 emit을 하기 위한 스케줄러를 지정한다. 
- parallel(): Downstream에 대한 데이터 처리를 병렬로 분할 처리하기 위한 스레드를 지정한다. 

Parallel()
- `parallel()`을 사용하면 `ParallelFlux`를 리턴한다. 
- 이대로 사용하면 안되고, `runOn`과 함께 사용되어야 한다. 
- 샘플
   ```java
   Flux.fromArray(new Integer[] {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23 ,25, 27, 29, 31})
        .parallel(4) // 4개의 스레드를 사용하겠다.
        .runOn(Schedulers.parallel())
        .subscribe(data -> log.info("subscribe: {}", data));
   ```

동작 
- Opeator 체인에서 최초의 스레드는 subscribe()가 호출되는 scope에 있는 스레드이다. 
- publishOn()이 호출되면 publishOn() 호출 이후의 Operator 체인은 다음 publishOn()을 만나기까지 publishOn()에서 지정한 스레드에서 실행된다. 
   - subscribe()도 같은 스레드에서 실행된다. 
   - 예시
      ```java
      Flux.fromArray(new Integer[] {1, 3, 5, 7})
         .doOnNext(data -> log.info("fromArray: {}", data))  // main thread
         .publishOn(Schedulers.parallel())
         .filter(data -> data > 3)
         .doOnNext(data -> log.info("filter: {}", data))   // thread A
         .publishOn(Schedulers.parallel())
         .map(data -> data * 10)
         .doOnNext(data -> log.info("map: {}", data))  // thread B
         .subscribe(data -> log.info("Subscribe: {}", data));   // thread B
      ```
- subscribeOn()이 호출되면, emit하는 스레드가 별도의 스레드에서 실행된다. 그리고 publishOn()을 만나기 전까지는 새로운 스레드에서 실행된다. 
   - subscribeOn()은 어디에 위치하던 최상의 업스트림 오퍼레이터의 스레드를 지정한다. 
   - 예시
      ```java
       Flux.fromArray(new Integer[] {1, 3, 5, 7})
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext(data -> log.info("fromArray: {}", data))    // thread A
        .filter(data -> data > 3)
        .doOnNext(data -> log.info("filter: {}", data))    // thread A
        .publishOn(Schedulers.parallel())
        .map(data -> data * 10)
        .doOnNext(data -> log.info("map: {}", data))    // thread B
        .subscribe(data -> log.info("Subscribe: {}", data));     // thread B
      ```

스케줄러 종류
- immediate(): 현재 스레드에서 실행한다. 
- single(): 하나의 스레드를 재사용한다. 저지연(low latency) 일회성 실행에 최적화 되어 있다. 
- boundedElastic(): 스레드풀을 생성하여 생성된 스레드를 재사용한다. 
   - 생성할 수 있는 스레드 수의 제한이 있다. 기본: CPU core * 10
   - 긴 실행시간을 가진 Blocking I/O 작업에 최적화 되어 있다.
- parallel(): 여러 스레드를 할당해서 동시에 작업을 수행할 수 있다. 
   - non-Blocking I/O 작업에 최적화 되어 있다. 
   - CPU core 수 만큼 스레드를 생성한다. 
- fromExecutorService(): 기존의 ExecutorService를 사용해서 처리한다. 
   - Metric에서 주료 사용된다. 
   - 추천하지는 않는다. 
- newXXXX(): 새로운 스케줄러 인스턴스를 생성할 수 있다.
   - 이름을 직접 지정할 수 있다. 

## Context
개념
- sequence상에서 상태를 저장할 수 있고, 저장된 값을 operator 체인에서 공유할 수 있는 인터페이스.
- key, value로 저장
   - 저장: contextWrite()
   - 읽어오기: ContextView
- 실행 스레드가 달라도 context에 저장된 값은 가져올 수 있다. 

예시
```java
String key = "message";

Mono<String> mono = Mono.deferContextual(ctx ->
            Mono.just("Hello " + ctx.get(key))
                .doOnNext(data -> log.info("onNext: {}", data)))  // context 정보를 가져와서 emit
        .subscribeOn(Schedulers.boundedElastic())
        .publishOn(Schedulers.parallel()) // emit 스레드와 tranform 스레드가 다르다. 그래도 context를 같이 사용할 수 있다. 
        .transformDeferredContextual((mono2, ctx) -> mono2.map(data -> data + " " + ctx.get(key)))
        .contextWrite(context -> context.put(key, "Reactor")); // context 저장
```

Context API
- put(key, value): 값을 context에 쓴다
- Context.of(key1, value1, key2, value2..): 여러개의 key/value를 쓴다. 
- putAll(ContextView): 파라미터로 입력된 ContextView를 merge한다. 
- delete(key): 삭제한다. 

ContextView API
- get(key): key에 해당하는 value를 반환한다. 
- getOrEmpty(key): key에 해당하는 데이터를 Optional로 반환한다. 
- getOrDefault(key, default): key에 해당하는 값이 없으면 default value를 읽어온다. 
- hasKey(key): 특정 key가 있는지 확인한다. 
- isEmpty(): context가 비어있는지
- size(): key/value 쌍의 개수를 반환한다. 

특징
- Subscriber를 통해서 Context는 연결된다. 그래서 Operator가 달라도 접근할 수 있다. 
- 체인의 맨 아래(=Downstream)부터 위(=Upstream)로 전파된다. 그래서 Operator 체인의 제일 마지막에 둔다. 

## 디버깅
방법
1. Debug 모드를 활성화 (Global)
2. checkpoint() Operator를 사용
3. log() Operator를 사용

용어
- stacktface
- assembly: 새로운 flux가 선언된 지점
   - operator가 선언된 지점
- traceback: 에러가 발생한 operator의 정보를 캡처한 정보 

디버그 모드
- Hooks.onOperatorDebug()를 통해서 활성화한다. 
- Operator 체인에서 에러 발생 시, 에러가 발생한 Operator의 위치를 알려준다. 
- 모든 Operator의 assembly를 캡쳐하기 때문에 비용이 많이 든다. 
- 에러 발생 정보
   ```
   Error has been observed at the following site(s):
	*__Flux.zipWith ⇢ at study.spring.websocket.helloreactive.debug.DebugExample02.main(DebugExample02.java:15) // traceback
   ```