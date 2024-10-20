package com.shop.jsshop.exception;

public class OutOfStockException extends RuntimeException{ //사용자정의 예외(p.296)
    public OutOfStockException(String message){
        super(message);
    }
}
