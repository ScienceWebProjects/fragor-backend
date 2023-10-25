package com.filament.measurement.Device.Controller;

import com.filament.measurement.Device.DTO.AddingDeviceDTO;
import com.filament.measurement.Device.Request.AddingDeviceRequest;
import com.filament.measurement.Device.Service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/device/")
@SuppressWarnings("unused")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping("adding/add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<String> addAddingDevice(@Valid @RequestBody AddingDeviceRequest form, HttpServletRequest request) {
       try {
           deviceService.addAddingDevice(request, form);
           return ResponseEntity.status(HttpStatus.CREATED).body(null);
       }catch(IOException | InterruptedException ignored){
           return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
       }
    }

    @GetMapping("adding/get/all/")
    @PreAuthorize("hasAuthority('changer:get')")
    public ResponseEntity<List<AddingDeviceDTO>> getAllCompanyAddingDevice(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(deviceService.getAllCompanyAddingDevice(request));
    }

    @DeleteMapping("adding/delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> deleteAddingDevice(@PathVariable Long id, HttpServletRequest request){
        deviceService.deleteAddingDevice(id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("connect/{printerId}/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<Void> connectMeasureDeviceToPrinter(@PathVariable Long printerId, HttpServletRequest request) {
        try{
            deviceService.connectMeasureDeviceToPrinter(printerId,request);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch(IOException | InterruptedException ignored){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @DeleteMapping("disconnect/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> disconnectMeasureDevice(@PathVariable Long id,HttpServletRequest request){
        deviceService.disconnectMeasureDevice(id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
