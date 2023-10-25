package com.filament.measurement.Device.Service;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Permission.Role;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.DTO.AddingDeviceDTO;
import com.filament.measurement.Device.DTOMapper.AddingDeviceDTOMapper;
import com.filament.measurement.Device.DTOMapper.MeasuringDeviceDTOMapper;
import com.filament.measurement.Device.Model.AddingDevice;
import com.filament.measurement.Device.Model.MeasuringDevice;
import com.filament.measurement.Device.Repository.AddingDeviceRepository;
import com.filament.measurement.Device.Request.AddingDeviceRequest;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Device.Repository.MeasuringDeviceRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import com.filament.measurement.Printer.Service.PrinterService;
import com.filament.measurement.UDP.UDP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class DeviceService {
    @Value("${client.url}")
    private String clientUrl;
    private final UDP udp;
    private final JwtService jwtService;
    private final PrinterService printerService;
    private final MeasuringDeviceDTOMapper measuringDeviceDTOMapper;
    private final AddingDeviceDTOMapper addingDeviceDTOMapper;
    private final MeasuringDeviceRepository measuringDeviceRepository;
    private final PrinterRepository printerRepository;
    private final AddingDeviceRepository addingDeviceRepository;

    public DeviceService(
            UDP udp,
            JwtService jwtService,
            PrinterService printerService,
            MeasuringDeviceDTOMapper measuringDeviceDTOMapper,
            AddingDeviceDTOMapper addingDeviceDTOMapper,
            MeasuringDeviceRepository measuringDeviceRepository,
            PrinterRepository printerRepository,
            AddingDeviceRepository addingDeviceRepository
    ) {
        this.udp = udp;
        this.jwtService = jwtService;
        this.printerService = printerService;
        this.measuringDeviceDTOMapper = measuringDeviceDTOMapper;
        this.addingDeviceDTOMapper = addingDeviceDTOMapper;
        this.measuringDeviceRepository = measuringDeviceRepository;
        this.printerRepository = printerRepository;
        this.addingDeviceRepository = addingDeviceRepository;
    }

    public void connectMeasureDeviceToPrinter(Long printerId, HttpServletRequest request) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(clientUrl+"device/adding/add/"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        if(getResponse.statusCode() == 404)
            throw new NotFound404Exception("Device not found.");
        connectedDevice(printerId);
        Printer printer = printerService.getPrinter(request, printerId);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(getResponse.body(), Map.class);
        MeasuringDevice measuringDevice = saveMeasuringDeviceIntoDb(responseMap,jwtService.extractUser(request).getCompany(),printer);
        connectDeviceWithPrinter(measuringDevice,printer);
    }

    public void disconnectMeasureDevice(Long id, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        MeasuringDevice measuringDevice = getMeasuringDevice(company,id);
        measuringDeviceRepository.delete(measuringDevice);
    }

    public void addAddingDevice(HttpServletRequest request,AddingDeviceRequest form) throws IOException, InterruptedException {
        Company company = jwtService.extractUser(request).getCompany();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(clientUrl+"device/adding/add/"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        if(getResponse.statusCode() == 404)
            throw new NotFound404Exception("Device not found.");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(getResponse.body(), Map.class);
        validateDevicePassword(responseMap.get("password").toString(),"avnrhewirb78943qnvpofdpgfv");
        deviceAlreadyExists(responseMap.get("ip").toString(),company);
        saveAddingDeviceIntoDb(responseMap,company,form);
    }

    public List<AddingDeviceDTO> getAllCompanyAddingDevice(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        List<AddingDeviceDTO> collect = addingDeviceRepository.findAllByCompany(company).stream()
                .map(addingDeviceDTOMapper).toList();
        if(collect.isEmpty())
            throw new NotFound404Exception("No added devices.");
        return collect;
    }

    public void deleteAddingDevice(Long id, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        AddingDevice addingDevice = getAddingDevice(company,id);
        addingDeviceRepository.delete(addingDevice);
    }

    private void deviceAlreadyExists(String address, Company company) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(address);
        Optional<?> device = addingDeviceRepository.findByIpAndCompany(inetAddress,company);
        if(device.isPresent())
            throw new CustomValidationException("Adding device already exists");
    }
    private void saveAddingDeviceIntoDb(Map<String,Object> map, Company company, AddingDeviceRequest form) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(map.get("ip").toString());
        int port = (Integer) map.get("port");
        AddingDevice addingDevice = AddingDevice.builder()
                .role(Role.DEVICE)
                .ip(inetAddress)
                .port(port)
                .company(company)
                .name(form.getName())
                .build();
        addingDeviceRepository.save(addingDevice);
    }

    private void validateDevicePassword(String receivedMessage,String password) {
        if (!receivedMessage.equals(password))
            throw new CustomValidationException("Wrong password.");
    }

    private void connectDeviceWithPrinter(MeasuringDevice measuringDevice, Printer printer) {
        printer.setMeasuringDevice(measuringDevice);
        printerRepository.save(printer);
    }

    private MeasuringDevice saveMeasuringDeviceIntoDb(Map<String,Object> map, Company company, Printer printer) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName(map.get("ip").toString());
        int port = (Integer) map.get("port");
        MeasuringDevice measuringDevice = MeasuringDevice.builder()
                .company(company)
                .ip(inetAddress)
                .port(port)
                .role(Role.DEVICE)
                .printer(printer)
                .build();
        measuringDeviceRepository.save(measuringDevice);
        return measuringDevice;
    }

    private AddingDevice getAddingDevice(Company company, Long id){
        Optional<AddingDevice> addingDeviceOptional = addingDeviceRepository.findByIdAndCompany(id, company);
        if(addingDeviceOptional.isEmpty())
            throw new NotFound404Exception("Device doesn't found.");
        return addingDeviceOptional.get();
    }

    private MeasuringDevice getMeasuringDevice(Company company, Long id){
        Optional<MeasuringDevice> device = measuringDeviceRepository.findByCompanyAndId(company,id);
        if(device.isEmpty()) throw new CustomValidationException("Measuring device doesn't exists.");
        return device.get();
    }

    private void connectedDevice(Long printerId) {
        Optional<MeasuringDevice> existsDevice = measuringDeviceRepository.findByPrinterId(printerId);
        if (existsDevice.isPresent()) {
            throw new CustomValidationException("Printer's measuring device already exists.");
        }
    }
}
