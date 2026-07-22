package co.com.bancolombia.model.franchise.exceptions;

public class BranchAlreadyExistsException extends BusinessException {

    public BranchAlreadyExistsException() {
        super("Branch already exists in this franchise");
    }
}
