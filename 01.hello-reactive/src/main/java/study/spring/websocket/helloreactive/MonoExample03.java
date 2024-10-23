package study.spring.websocket.helloreactive;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoExample03 {

  public static void main(String[] args) {
    URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("https")
        .host("jsonplaceholder.typicode.com")
        .port(443)
        .path("/todos/1")
        .build()
        .encode()
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    Mono.just(
        restTemplate.exchange(worldTimeUri, HttpMethod.GET, new HttpEntity<>(headers), String.class)
    )
        .map(response -> {
          String body = response.getBody();
          Map<String, Object> stringObjectMap = JsonParserFactory.getJsonParser().parseMap(body);
          return stringObjectMap.get("title");
        })
        .subscribe(
            data -> log.info("# emitted date: {}", data),
            error -> log.error("# onError", error),
            () -> log.info("# onComplete")
        );
  }

}
