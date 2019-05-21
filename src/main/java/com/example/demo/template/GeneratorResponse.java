package com.example.demo.template;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeneratorResponse<T> implements Serializable {

    private int code;

    private String message;

    private T result;

    public GeneratorResponse(){

    }

    public GeneratorResponse(int code,String message){
        this.code = code;
        this.message = message;
    }

    public GeneratorResponse(int code,String message,T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static GeneratorResponse fail(int code,String message){
        return new GeneratorResponse(code,message);
    }

    public static GeneratorResponse ok(Object result){
        return new GeneratorResponse(200,"",result);
    }
}
