package com.github.xsssecurity.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xss")
public class XssSecurityTestController {

    @PostMapping("/json-clear")
    public String jsonClear(@RequestBody String body) {
        return body;
    }

    @PostMapping("/form-clear")
    public String getFormClear(@RequestParam("p1") String p1, @RequestParam("p2") String p2) {
        return "p1=" + p1 + "&p2=" + p2;
    }

    @GetMapping("/form-clear")
    public String postFormClear(@RequestParam("p1") String p1, @RequestParam("p2") String p2) {
        return "p1=" + p1 + "&p2=" + p2;
    }
}
