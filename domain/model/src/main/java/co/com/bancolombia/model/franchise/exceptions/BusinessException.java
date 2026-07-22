package co.com.bancolombia.model.franchise.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends DomainException {

    public BusinessException(String message) {
        super(message);
    }
}
