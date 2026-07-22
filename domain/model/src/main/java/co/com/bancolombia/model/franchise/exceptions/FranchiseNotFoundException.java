package co.com.bancolombia.model.franchise.exceptions;

public class FranchiseNotFoundException extends FranchiseException {

    public FranchiseNotFoundException() {
        super("Franchise not found");
    }
}
