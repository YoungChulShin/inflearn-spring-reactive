package study.spring.websocket.helloreactive.context;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
public class ContextRealExample01 {

  public static final String HEADER_NAME_AUTH_TOKEN = "authToken";

  public static void main(String[] args) {
    postBook(Mono.just(
        new Book("abcd-1111-2222-3333", "testBook", "tester"))
    ).contextWrite(Context.of(HEADER_NAME_AUTH_TOKEN, "abcde"))
        .subscribe(data -> log.info("subscribe: {}", data));

  }

  private static Mono<String> postBook(Mono<Book> book) {
    return Mono.zip(
            book,
            Mono.deferContextual(ctx -> Mono.just(ctx.get(HEADER_NAME_AUTH_TOKEN))))
        .flatMap(Mono::just)
        .flatMap(tuple -> {
          String response =
              "POST the book(" + tuple.getT1().bookName() + "," + tuple.getT1().author()
                  + ") with token: " + tuple.getT2();
          return Mono.just(response);
        });
  }
}
