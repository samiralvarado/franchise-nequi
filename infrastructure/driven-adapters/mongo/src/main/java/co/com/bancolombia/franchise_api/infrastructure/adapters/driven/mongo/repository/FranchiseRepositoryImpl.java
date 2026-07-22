package co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.repository;

import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.mapper.MongoMapper;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.dto.ProductReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final MongoReactiveRepository mongoReactiveRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return mongoReactiveRepository.save(MongoMapper.toEntity(franchise))
                .map(MongoMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoReactiveRepository.findById(id)
                .map(MongoMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findByName(String name) {
        return mongoReactiveRepository.findByName(name)
                .map(MongoMapper::toDomain);
    }

    @Override
    public Flux<ProductReport> getMaxStockPerBranch(String franchiseId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(franchiseId)),
                Aggregation.unwind("branches"),
                Aggregation.unwind("branches.products"),
                Aggregation.group("branches.name")
                        .max("branches.products.stock").as("maxStock")
                        .first("branches.products").as("product"),
                Aggregation.project("product", "maxStock")
                        .and("_id").as("branchName")
        );

        return mongoTemplate.aggregate(aggregation, "franchises", ProductReport.class);
    }
}