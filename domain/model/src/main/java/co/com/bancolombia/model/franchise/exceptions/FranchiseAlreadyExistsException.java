package co.com.bancolombia.model.franchise.exceptions;

public class FranchiseAlreadyExistsException extends FranchiseException {

    public FranchiseAlreadyExistsException() {
        super("A franchise with this name already exists");
    }
}
