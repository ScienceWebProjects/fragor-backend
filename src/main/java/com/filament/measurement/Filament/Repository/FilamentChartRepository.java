package com.filament.measurement.Filament.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Filament.Model.FilamentChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FilamentChartRepository extends JpaRepository<FilamentChart, Long> {
    Optional<FilamentChart> findByColorAndMaterialAndBrandAndTimeAndCompany(String color,
                                                                            String material,
                                                                            String brand,
                                                                            String time,
                                                                            Company company);
    List<FilamentChart> findAllByCompany(Company company);
}
