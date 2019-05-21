package com.example.demo.template;

import java.util.concurrent.Callable;

public class GeneratorResponseTemplate {

     public static GeneratorResponse doTemplate(Callable action ) throws Exception{
         return (GeneratorResponse)action.call();
    }

}
