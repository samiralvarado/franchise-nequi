package co.com.bancolombia.model.franchise.exceptions;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super("Product not found");
    }
}
