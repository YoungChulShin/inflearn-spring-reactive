package study.spring.websocket.helloreactive.debug;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class LogExample01 {

  public static Map<String, String> fruits = new HashMap<>();

  static {
    fruits.put("banana", "바나나");
    fruits.put("apple", "사과");
    fruits.put("pear", "배");
    fruits.put("grape", "포도");
  }

  public static void main(String[] args) {
    Flux.fromArray(new String[] {"BANANAS", "APPLES", "PEARS", "MELONES"})
        .log()
        .map(String::toLowerCase)
        .map(data -> data.substring(0, data.length() - 1))
        .map(data -> fruits.get(data))
        .subscribe(
            data -> log.info("subscribe: {}", data),
            error -> log.error("error", error)
        );
  }



}
