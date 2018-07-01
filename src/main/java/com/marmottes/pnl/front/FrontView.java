package com.marmottes.pnl.front;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface FrontView {
    @RequestMapping("/home")
    String home();
}
