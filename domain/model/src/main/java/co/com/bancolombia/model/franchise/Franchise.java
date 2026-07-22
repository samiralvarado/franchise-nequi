package co.com.bancolombia.model.franchise;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class Franchise {

    String id;
    FranchiseName name;
    @Builder.Default
    List<Branch> branches = new ArrayList<>();
}

