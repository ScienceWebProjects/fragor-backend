package com.filament.measurement.Filament.Controller;

import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.Model.FilamentNotes;
import com.filament.measurement.Filament.Request.FilamentNotesRequest;
import com.filament.measurement.Filament.Request.FilamentRequest;
import com.filament.measurement.Filament.Request.FilamentSubtractionRequest;
import com.filament.measurement.Filament.Service.FilamentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/api/filaments/")
@SuppressWarnings("unused")
public class FilamentController {
    private final FilamentService filamentService;
    public FilamentController(FilamentService filamentService) {
        this.filamentService = filamentService;
    }

    @PostMapping("add/")
    @PreAuthorize("hasAuthority('changer:create')")
    public ResponseEntity<?> addFilament(@Valid @RequestBody FilamentRequest form, HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(filamentService.addFilament(form, request));
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Timeout");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("find/{deviceId}/")
    public ResponseEntity<?> findFilamentByUid(@PathVariable Long deviceId,HttpServletRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(filamentService.findFilament(request, deviceId));
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Timeout");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("get/all/")
    public ResponseEntity<List<FilamentDTO>> getAllFilaments(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getAllFilaments(request));
    }

    @GetMapping("get/{id}/")
    public ResponseEntity<FilamentDTO> getFilament (@PathVariable Long id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getFilament(id,request));
    }
    @PatchMapping("update/{id}/")
    @PreAuthorize("hasAuthority('changer:update')")
    public ResponseEntity<FilamentDTO> updateFilament(
            @PathVariable Long id,
            HttpServletRequest request,
            @Valid @RequestBody FilamentRequest form){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.updateFilament(id,request,form));
    }
    @DeleteMapping("delete/{id}/")
    @PreAuthorize("hasAuthority('changer:delete')")
    public ResponseEntity<Void> deleteFilament(@PathVariable Long id,HttpServletRequest request){
        filamentService.deleteFilament(id,request);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("filter/get/all/")
    public ResponseEntity<HashMap<String, Set<String>>> getFilamentsFilters(HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body( filamentService.getFilamentsFilters(request));
    }

    @GetMapping("filter/{color}/{material}/{brand}/{quantity}/")
    public ResponseEntity<List<FilamentDTO>> getFilteredFilaments(
            @PathVariable String color,
            @PathVariable String material,
            @PathVariable String brand,
            @PathVariable double quantity,
            HttpServletRequest request
    ){
    return ResponseEntity.status(HttpStatus.OK).body(
            filamentService.getFilteredFilament(
                    color,
                    material,
                    brand,
                    quantity,
                    request
            )
            );

    }
    @PutMapping("subtraction/")
    @PreAuthorize("hasRole('DEVICE')")
    public ResponseEntity<Void> subtraction(@RequestBody FilamentSubtractionRequest form, HttpServletRequest request){
        filamentService.subtraction(form,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/add/{amount}/")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<Void> addRandomFilaments(@PathVariable int amount,HttpServletRequest request){
        filamentService.addRandomFilaments(amount,request);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("notes/get/all/{filament_id}/")
    public ResponseEntity<List<FilamentNotes>> getAllFilamentNotes (@PathVariable Long filament_id, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(filamentService.getAllFilamentNotes(filament_id,request));
    }

    @PostMapping("notes/update/{filament_id}/")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<Void> addOrUpdateFilamentNote (
            @PathVariable Long filament_id,
            @RequestBody FilamentNotesRequest form,
            HttpServletRequest request){
        filamentService.addOrUpdateFilamentNote(filament_id,request,form);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @DeleteMapping("{filament_id}/notes/delete/{note_id}/")
    @PreAuthorize("hasAuthority('owner:delete')")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long filament_id,
            @PathVariable Long note_id,
            HttpServletRequest request)
    {
        filamentService.deleteFilamentNote(filament_id,note_id,request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
