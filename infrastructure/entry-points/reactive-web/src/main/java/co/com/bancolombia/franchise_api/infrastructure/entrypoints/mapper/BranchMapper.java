package co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper;

import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.BranchRequest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;

import java.util.ArrayList;

public class BranchMapper {
    public static Branch toDomain(BranchRequest request) {
        return Branch.builder()
                .name(BranchName.of(request.getName()))
                .products(new ArrayList<>())
                .build();
    }
}
