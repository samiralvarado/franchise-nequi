package co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper;


import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.FranchiseRequest;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.values.FranchiseName;

public class FranchiseMapper {
    public static Franchise toDomain(FranchiseRequest request) {
        return Franchise.builder()
                .name(FranchiseName.of(request.getName()))
                .build();
    }
}
