package co.com.bancolombia.model.branch;

import java.util.ArrayList;
import java.util.List;

import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.product.Product;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class Branch {

    String id;
    BranchName name;
    @Builder.Default
    List<Product> products = new ArrayList<>();

}
