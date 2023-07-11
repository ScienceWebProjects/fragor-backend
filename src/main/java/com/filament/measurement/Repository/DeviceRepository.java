package com.filament.measurement.Repository;

import com.filament.measurement.Model.Company;
import com.filament.measurement.Model.Device;
import com.filament.measurement.Model.Printer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.InetAddress;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Optional<Device> findByPrinterId(Long id);
    Optional<Device> findByCompanyAndId(Company company,Long id);

    Optional<Device> findByIp(InetAddress ip);
}
