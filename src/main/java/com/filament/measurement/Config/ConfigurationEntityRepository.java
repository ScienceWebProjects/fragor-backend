package com.filament.measurement.Config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationEntityRepository extends JpaRepository<ConfigurationEntity,Long> {
    Optional<ConfigurationEntity> findByKey(String key);
}
