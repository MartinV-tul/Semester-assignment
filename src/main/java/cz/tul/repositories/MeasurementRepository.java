package cz.tul.repositories;

import cz.tul.data.Measurement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeasurementRepository extends MongoRepository<Measurement,Long> {
    List<Measurement> findAllByTownIdOrderByTsDesc(int townId);

    List<Measurement> findAllByCountryCodeOrderByTsDesc(String code);

    Measurement findFirstByTownIdOrderByTsDesc(int townId);

    void deleteAllByTownId(int townId);

    void deleteAllByCountryCode(String countryCode);
}
