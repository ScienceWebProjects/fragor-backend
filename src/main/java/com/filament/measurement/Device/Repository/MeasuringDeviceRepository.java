package com.filament.measurement.Device.Repository;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Device.Model.MeasuringDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.util.Optional;
@Repository
public interface MeasuringDeviceRepository extends JpaRepository<MeasuringDevice,Long> {

    Optional<MeasuringDevice> findByPrinterId(Long id);
    Optional<MeasuringDevice> findByCompanyAndId(Company company, Long id);
    Optional<MeasuringDevice> findByIp(InetAddress ip);
}
