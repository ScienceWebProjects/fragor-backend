package com.filament.measurement.Service;

import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Model.Device;
import com.filament.measurement.Model.Printer;
import com.filament.measurement.Repository.DeviceRepository;
import com.filament.measurement.Repository.PrinterRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.*;
import java.io.IOException;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    JwtService jwtService;
    @Autowired
    PrinterService printerService;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    PrinterRepository printerRepository;
    public Device add(Long printerId, HttpServletRequest request) {
        String ipAddress = "192.168.0.";
        int port = 3984;
        int timeout = 50;
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            String messageToSend = "password_rfid";
            String answer = "respond_rfid";

            byte[] sendData = messageToSend.getBytes();
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            for(int i=2;i<254;i++) {
                InetAddress destinationIP = InetAddress.getByName(ipAddress+i);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, destinationIP, port);
                socket.send(sendPacket);
                try {
                    socket.receive(receivePacket);
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    if (receivedMessage.equals(answer)) {
                        Optional<Device> existsDevice = deviceRepository.findByPrinterId(printerId);
                        if(existsDevice.isPresent()){
                            throw new CustomValidationException("Printer's device already exists.");
                        }
                        Printer printer = printerService.get(request,printerId);
                        Device device = Device.builder()
                                .ip(receivePacket.getAddress())
                                .company(jwtService.extractUser(request).getCompany())
                                .port(receivePacket.getPort())
                                .printer(printer)
                                .build();
                        deviceRepository.save(device);
                        printer.setDevice(device);

                        printerRepository.save(printer);
                        socket.close();

                        return device;
                    }
                } catch (IOException e) {
//                    System.out.println(destinationIP);
                }
            }
            socket.close();
            throw new CustomValidationException("Device not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void delete(Long id, HttpServletRequest request) {
        Optional<Device> device = deviceRepository.findByCompanyAndId(jwtService.extractUser(request).getCompany(),id);
        if(device.isEmpty()) throw new CustomValidationException("Device doesn't exists.");
        deviceRepository.delete(device.get());
    }
}
