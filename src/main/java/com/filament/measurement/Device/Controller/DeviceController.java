package com.filament.measurement.Device.Controller;

import com.filament.measurement.Device.DTO.DeviceDTO;
import com.filament.measurement.Device.Model.Device;
import com.filament.measurement.Device.Service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device/")
public class DeviceController {
    private final DeviceService deviceService;
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("connect/{printerId}/")
    public ResponseEntity<DeviceDTO> device(@PathVariable Long printerId, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.add(printerId,request));
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id,HttpServletRequest request){
        deviceService.delete(id,request);
        return ResponseEntity.noContent().build();
    }
}
