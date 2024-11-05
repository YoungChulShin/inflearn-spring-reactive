//package study.spring.websocket.bookservice;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Stream;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@RequestMapping(value = "/v1/books")
//@RestController
//public class SpringReactiveBranchOfficeController {
//
//  private Map<Long, Book> bookMap;
//
//  public SpringReactiveBranchOfficeController() {
//    bookMap = new HashMap<>();
//
//    for (long i = 1; i < 100; i++) {
//      bookMap.put(i, new Book(i, "IT Book " + i));
//    }
//  }
//
//  @GetMapping("/{book-id}")
//  public Mono<Book> getBook(@PathVariable(value = "book-id") Long bookId) throws InterruptedException {
//    Thread.sleep(200);
//
//    Book book = bookMap.get(bookId);
//    log.info("# book for response: {}, {}", book.getId(), book.getName());
//    return Mono.just(book);
//  }
//}
