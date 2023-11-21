package com.filament.measurement.Device.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Device.Model.AddingDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

public interface AddingDeviceRepository extends JpaRepository<AddingDevice,Long> {
    Optional<AddingDevice> findByIpAndCompany(InetAddress ip, Company company);
    Optional<AddingDevice> findByIdAndCompany(Long id, Company company);
    @Query("""
            SELECT EXISTS (SELECT 1 FROM AddingDevice ad WHERE ad.company = ?1 AND name = ?2)
            """)
    boolean nameExists(Company company,String name);
    List<AddingDevice> findAllByCompany(Company company);
}
