package co.com.bancolombia.model.franchise.exceptions;

public class BranchNotFoundException extends BusinessException {

    public BranchNotFoundException() {
        super("Branch not found");
    }
}
