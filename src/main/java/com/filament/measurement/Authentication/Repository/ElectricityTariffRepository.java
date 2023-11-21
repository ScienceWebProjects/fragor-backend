package com.filament.measurement.Authentication.Repository;


import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Model.ElectricityTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ElectricityTariffRepository extends JpaRepository<ElectricityTariff,Long> {
    List<ElectricityTariff> findAllByCompany(Company company);
    Optional<ElectricityTariff> findByCompanyAndId(Company company,Long id);
    @Query("""
           SELECT EXISTS (SELECT 1 FROM ElectricityTariff e WHERE e.company =?1 AND e.name =?2)
            """)
    Boolean nameExists(Company company,String name);
    Optional<ElectricityTariff> findByCompanyAndHourFrom(Company company,int HourFrom);

    @Query("SELECT et FROM ElectricityTariff et " +
            "WHERE et.hourFrom <= :hour AND et.hourTo >= :hour " +
            "AND et.workingDays = :workingDays " +
            "AND et.weekend = :weekend "+
            "AND et.company = :company")
    Optional<ElectricityTariff> findValidTariff(int hour,boolean workingDays, boolean weekend, Company company);
}
