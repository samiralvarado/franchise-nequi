package co.com.bancolombia.model.franchise.exceptions;

public class ProductAlreadyExistsException extends BusinessException {

    public ProductAlreadyExistsException() {
        super("Product already exists in this branch");
    }
}
