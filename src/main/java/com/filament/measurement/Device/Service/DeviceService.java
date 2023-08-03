package com.filament.measurement.Device.Service;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.DTO.DeviceDTO;
import com.filament.measurement.Device.DTOMapper.DeviceDTOMapper;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Device.Model.Device;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Device.Repository.DeviceRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import com.filament.measurement.Printer.Service.PrinterService;
import com.filament.measurement.UDP.UDP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.IOException;
import java.util.Optional;

@Service
public class DeviceService {
    private final UDP udp;
    private final JwtService jwtService;
    private final PrinterService printerService;
    private final DeviceDTOMapper deviceDTOMapper;
    private final DeviceRepository deviceRepository;
    private final PrinterRepository printerRepository;

    public DeviceService(
            UDP udp,
            JwtService jwtService,
            PrinterService printerService,
            DeviceDTOMapper deviceDTOMapper,
            DeviceRepository deviceRepository,
            PrinterRepository printerRepository
    ) {
        this.udp = udp;
        this.jwtService = jwtService;
        this.printerService = printerService;
        this.deviceDTOMapper = deviceDTOMapper;
        this.deviceRepository = deviceRepository;
        this.printerRepository = printerRepository;
    }

    public DeviceDTO add(Long printerId, HttpServletRequest request) {
        DatagramPacket receivePacket = scanLocalHost();
        validateDevicePassword(receivePacket);
        connectedDevice(printerId);
        Printer printer = printerService.getPrinter(request, printerId);
        Device device = saveDeviceIntoDb(receivePacket,jwtService.extractUser(request).getCompany(),printer);
        connectDeviceWithPrinter(device,printer);
        return deviceDTOMapper.apply(device);
    }

    private void validateDevicePassword(DatagramPacket receivePacket) {
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (!receivedMessage.equals("respond_device"))
            throw new CustomValidationException("Wrong password.");
    }

    private DatagramPacket scanLocalHost() {
        for (int i = 2; i < 254; i++) {
            try {
                 return udp.send("password_rfid", 3984, "192.168.0." + i, 5);
            } catch (IOException e) {
                continue;
            }
        }
        throw new CustomValidationException("Device not found.");
    }

    private void connectDeviceWithPrinter(Device device, Printer printer) {
        printer.setDevice(device);
        printerRepository.save(printer);
    }

    private Device saveDeviceIntoDb(DatagramPacket receivePacket, Company company, Printer printer) {
        Device device = Device.builder()
                .ip(receivePacket.getAddress())
                .company(company)
                .port(receivePacket.getPort())
                .printer(printer)
                .build();
        deviceRepository.save(device);
        return device;
    }

    public void delete(Long id, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        Device device = getDevice(company,id);
        deviceRepository.delete(device);
    }
    private Device getDevice(Company company, Long id){
        Optional<Device> device = deviceRepository.findByCompanyAndId(company,id);
        if(device.isEmpty()) throw new CustomValidationException("Device doesn't exists.");
        return device.get();
    }
    private void connectedDevice(Long printerId) {
        Optional<Device> existsDevice = deviceRepository.findByPrinterId(printerId);
        if (existsDevice.isPresent()) {
            throw new CustomValidationException("Printer's device already exists.");
        }
    }
}
