package co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.repository;


import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoReactiveRepository extends ReactiveMongoRepository<FranchiseEntity, String> {
    Mono<FranchiseEntity> findByName(String name);
}
