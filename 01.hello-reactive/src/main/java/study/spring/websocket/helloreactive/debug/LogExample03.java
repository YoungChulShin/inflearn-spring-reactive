package study.spring.websocket.helloreactive.debug;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class LogExample03 {

  public static Map<String, String> fruits = new HashMap<>();

  static {
    fruits.put("banana", "바나나");
    fruits.put("apple", "사과");
    fruits.put("pear", "배");
    fruits.put("grape", "포도");
  }

  public static void main(String[] args) throws InterruptedException {
    Flux.fromArray(new String[] {"BANANAS", "APPLES", "PEARS", "MELONES"})
        .subscribeOn(Schedulers.boundedElastic())
        .log("Fruit.source")
        .publishOn(Schedulers.parallel())
        .map(String::toLowerCase)
        .log("Fruit.lower")
        .map(data -> data.substring(0, data.length() - 1))
        .log("Fruit.substring")
        .map(data -> fruits.get(data))
        .log("Fruit.name")
        .subscribe(
            data -> log.info("subscribe: {}", data),
            error -> log.error("error", error)
        );

    Thread.sleep(1000);
  }



}
