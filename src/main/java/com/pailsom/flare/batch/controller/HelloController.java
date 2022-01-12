package com.pailsom.flare.batch.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/{user}")
    public String hello(@PathVariable String user){
        return "<p><h2>Hello Mr. </h2><h1>"+user+" !</h1></p>";
    }
}
