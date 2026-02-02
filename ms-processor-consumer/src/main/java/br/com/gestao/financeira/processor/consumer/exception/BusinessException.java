package br.com.gestao.financeira.processor.consumer.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
