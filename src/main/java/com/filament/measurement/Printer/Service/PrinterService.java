package com.filament.measurement.Printer.Service;

import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Printer.DTO.PrinterDTO;
import com.filament.measurement.Printer.DTOMapper.PrinterDTOMapper;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterModel;
import com.filament.measurement.Printer.Model.PrinterNotes;
import com.filament.measurement.Printer.Repository.PrinterModelRepository;
import com.filament.measurement.Printer.Repository.PrinterNotesRepository;
import com.filament.measurement.Printer.Repository.PrinterRepository;
import com.filament.measurement.Printer.Request.PrinterNotesRequest;
import com.filament.measurement.Printer.Request.PrinterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrinterService {
    @Value("${printer.imagePath}")
    private String printerImagePath;
    private final JwtService jwtService;
    private final PrinterRepository printerRepository;
    private final PrinterModelRepository printerModelRepository;
    private final PrinterDTOMapper printerDTOMapper;
    private final PrinterNotesRepository printerNotesRepository;
    public PrinterService(
            JwtService jwtService,
            PrinterRepository printerRepository,
            PrinterModelRepository printerModelRepository,
            PrinterDTOMapper printerDTOMapper,
            PrinterNotesRepository printerNotesRepository) {
        this.jwtService = jwtService;
        this.printerRepository = printerRepository;
        this.printerModelRepository = printerModelRepository;
        this.printerDTOMapper = printerDTOMapper;
        this.printerNotesRepository = printerNotesRepository;
    }

    public void addPrinter(HttpServletRequest request, PrinterRequest form){
        Company company = jwtService.extractUser(request).getCompany();
        PrinterModel printerModel = getPrinterModel(company, form.getModel());
        Printer printer = savePrinterIntoDB(printerModel,form.getName(),company);
        printerDTOMapper.apply(printer);
    }
    public void updatePrinterImage(Long id, MultipartFile image, HttpServletRequest request) throws IOException {
        Printer printer = getPrinter(request,id);
        String imagePath = saveImage(image);
        if(!printer.getImage().equals("defaultPrinter")){
            if(deleteOldImageFromFile(printer.getImage())){
                throw new IOException();
            }
        }
        printer.setImage(imagePath);
        printerRepository.save(printer);
    }

    private boolean deleteOldImageFromFile(String imagePath) {
        File image = new File(printerImagePath + imagePath);
        if (image.exists())
            return !image.delete();
        return false;
    }

    public void updateNameModel(Long id, PrinterRequest form, HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        Printer printer = getPrinter(request,id);
        PrinterModel printerModel = getPrinterModel(company, form.getModel());
        printer.setName(form.getName());
        printer.setPrinterModel(printerModel);
        printerRepository.save(printer);
    }

    public byte[] getPrinterImage(String name) throws IOException {
        return Files.readAllBytes(new File(printerImagePath+name).toPath());
    }

    public List<PrinterDTO> getAll(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return printerRepository.findAllByCompany(company).stream()
                .map(printerDTOMapper)
                .collect(Collectors.toList());
    }

    public PrinterDTO get(HttpServletRequest request, Long id) {
        return printerDTOMapper.apply(getPrinter(request,id));
    }

    public void delete(Long id, HttpServletRequest request) {
        Printer printer = getPrinter(request,id);
        deleteOldImageFromFile(printer.getImage());
      printerRepository.delete(printer);
    }
    public List<PrinterNotes> getAllPrinterNotes(Long id, HttpServletRequest request) {
        Printer printer = getPrinter(request,id);
        return printerNotesRepository.findByPrinter(printer);
    }
    public Printer getPrinter(HttpServletRequest request, Long id){
        Company company = jwtService.extractUser(request).getCompany();
        Optional<Printer> printer = printerRepository.findByCompanyAndId(company,id);
        if(printer.isEmpty()) throw new CustomValidationException("Printer doesn't exists.");
        return printer.get();
    }
    public void addOrUpdateNotes(Long printer_id, HttpServletRequest request, PrinterNotesRequest form) {
        Printer printer = getPrinter(request,printer_id);
        if(form.getNoteID() == null){
            addNewNotes(printer,form.getNote());
        }else {
            updateNotes(printer,form);
        }
    }
    private void updateNotes(Printer printer, PrinterNotesRequest form) {
        PrinterNotes printerNotes = getPrinterNotes(printer, form.getNoteID());
        printerNotes.setNote(form.getNote());
        printerNotesRepository.save(printerNotes);
    }

    public void deleteNotes(Long printer_id, Long note_id, HttpServletRequest request) {
        Printer printer = getPrinter(request,printer_id);
        PrinterNotes printerNotes = getPrinterNotes(printer,note_id);
        printerNotesRepository.delete(printerNotes);
    }
    private PrinterNotes getPrinterNotes(Printer printer, Long note_id) {
        Optional<PrinterNotes> optionalPrinterNotes = printerNotesRepository.findByPrinterAndId(printer, note_id);
        if(optionalPrinterNotes.isEmpty())
            throw new NotFound404Exception("Notes not found.");
        return optionalPrinterNotes.get();
    }
    private void addNewNotes(Printer printer, String note) {
        PrinterNotes printerNotes = PrinterNotes.builder()
                .printer(printer)
                .note(note)
                .build();
        printerNotesRepository.save(printerNotes);
    }

    private PrinterModel getPrinterModel(Company company, String model) {
        Optional<PrinterModel> printerModel = printerModelRepository.findByCompanyAndModel(company,model);
        if(printerModel.isEmpty())
            throw new CustomValidationException("Model doesn't exists.");
        return printerModel.get();
    }
    private String saveImage(MultipartFile image) throws IOException {
        if(image.isEmpty()) return "defaultPrinter";
        else if(!Objects.requireNonNull(image.getContentType()).startsWith("image/"))
            throw new CustomValidationException("Invalid file extension");
        String name = LocalDateTime.now().toString();
        image.transferTo(new File(printerImagePath+name));
        return name;
    }
    private Printer savePrinterIntoDB(PrinterModel printerModel, String name, Company company) {
        Printer printer = Printer.builder()
                .name(name)
                .company(company)
                .workHours(0.0)
                .printerModel(printerModel)
                .filaments(Collections.emptyList())
                .image("defaultPrinter")
                .build();
        printerRepository.save(printer);
        return printer;
    }



}
