package com.example.demo.controller;

import com.example.demo.template.GeneratorResponse;
import com.example.demo.template.GeneratorResponseTemplate;
import com.example.demo.vo.DemoVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RequestMapping(value = "/demo")
@RestController
public class DemoController {


    @GetMapping("/{id}")
    public GeneratorResponse<DemoVo> getResponse(@PathVariable("id") int id) throws Exception{
         return GeneratorResponseTemplate.doTemplate((Callable<GeneratorResponse<DemoVo>>) () -> {
             DemoVo vo = new DemoVo();
             vo.setId(1);
             vo.setName("LI SI");
             return GeneratorResponse.ok(vo);
         });
    }
}
