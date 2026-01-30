package br.com.gestao.financeira.processorConsumer.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
