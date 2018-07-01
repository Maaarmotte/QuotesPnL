package com.marmottes.pnl.front.impl;

import com.marmottes.pnl.front.FrontView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FrontViewImpl implements FrontView {
    @RequestMapping("/home")
    @Override
    public String home() {
        return "home";
    }
}
