package co.com.bancolombia.model.franchise.exceptions;

public class RequestValidationException extends DomainException {

    public RequestValidationException(String message) {
        super(message);
    }
}
