package co.com.bancolombia.config;

import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.AddBranchUseCase;
import co.com.bancolombia.usecase.AddProductUseCase;
import co.com.bancolombia.usecase.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.DeleteProductUseCase;
import co.com.bancolombia.usecase.GetMaxStockUseCase;
import co.com.bancolombia.usecase.UpdateBranchNameUseCase;
import co.com.bancolombia.usecase.UpdateFranchiseUseCase;
import co.com.bancolombia.usecase.UpdateProductNameUseCase;
import co.com.bancolombia.usecase.UpdateStockUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepository franchiseRepository) {
                return new CreateFranchiseUseCase(franchiseRepository);
        }

        @Bean
        public AddBranchUseCase addBranchUseCase(FranchiseRepository franchiseRepository) {
                return new AddBranchUseCase(franchiseRepository);
        }

        @Bean
        public AddProductUseCase addProductUseCase(FranchiseRepository franchiseRepository) {
                return new AddProductUseCase(franchiseRepository);
        }

        @Bean
        public DeleteProductUseCase deleteProductUseCase(FranchiseRepository franchiseRepository) {
                return new DeleteProductUseCase(franchiseRepository);
        }

        @Bean
        public UpdateStockUseCase updateStockUseCase(FranchiseRepository franchiseRepository) {
                return new UpdateStockUseCase(franchiseRepository);
        }

        @Bean
        public GetMaxStockUseCase getMaxStockUseCase(FranchiseRepository franchiseRepository) {
                return new GetMaxStockUseCase(franchiseRepository);
        }

        @Bean
        public UpdateFranchiseUseCase updateFranchiseUseCase(FranchiseRepository franchiseRepository) {
                return new UpdateFranchiseUseCase(franchiseRepository);
        }

        @Bean
        public UpdateBranchNameUseCase updateBranchNameUseCase(FranchiseRepository franchiseRepository) {
                return new UpdateBranchNameUseCase(franchiseRepository);
        }

        @Bean
        public UpdateProductNameUseCase updateProductNameUseCase(FranchiseRepository franchiseRepository) {
                return new UpdateProductNameUseCase(franchiseRepository);
        }
}
