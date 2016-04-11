package company.tothepoint.repository;

import company.tothepoint.model.businessunit.BusinessUnit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessUnitRepository extends MongoRepository<BusinessUnit, String> {
}
