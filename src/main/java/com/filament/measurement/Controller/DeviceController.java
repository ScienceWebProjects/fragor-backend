package com.filament.measurement.Controller;

import com.filament.measurement.Model.Device;
import com.filament.measurement.Service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device/")
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @GetMapping("connect/{printerId}/")
    public ResponseEntity<Device> addDevdsadice(@PathVariable Long printerId, HttpServletRequest request){
        Device device = deviceService.add(printerId,request);
        if(device == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(device);
    }
    @DeleteMapping("delete/{id}/")
    public ResponseEntity<Void> delete(@PathVariable Long id,HttpServletRequest request){
        deviceService.delete(id,request);
        return ResponseEntity.noContent().build();
    }
}
