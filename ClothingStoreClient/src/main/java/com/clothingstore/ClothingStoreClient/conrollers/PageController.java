package com.clothingstore.ClothingStoreClient.conrollers;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @Timed(value = "page.load.time", description = "Time taken to load the main page")
    @Counted(value = "page.load.count", description = "Count of requests to load the main page")
    @GetMapping("/")
    public String getMainPage() {
        return "mainPage.html";
    }
}
