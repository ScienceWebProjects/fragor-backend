package com.filament.measurement.Device.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Device.Model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.InetAddress;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device,Long> {

    Optional<Device> findByPrinterId(Long id);
    Optional<Device> findByCompanyAndId(Company company,Long id);
    Optional<Device> findByIp(InetAddress ip);
}
