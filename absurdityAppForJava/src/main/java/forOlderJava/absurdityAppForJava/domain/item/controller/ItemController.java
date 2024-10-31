package forOlderJava.absurdityAppForJava.domain.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/items")
public class ItemController {
//    private final ItemService itemService;
    private final String DEFAULT_PREVIOUS_ID = "-1";
    private static final String BASE_URI = "/v1/items/";


}
