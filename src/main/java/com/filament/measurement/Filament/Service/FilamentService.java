package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.Model.Device;
import com.filament.measurement.Device.Repository.DeviceRepository;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentDTOMapper;
import com.filament.measurement.Filament.Form.FilamentForm;
import com.filament.measurement.Filament.Form.FilamentSubtraction;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import com.filament.measurement.Filament.Repository.FilamentRepository;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import com.filament.measurement.Printer.Repository.PrinterFilamentsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FilamentService {
    private final JwtService jwtService;
    private final DeviceRepository deviceRepository;
    private final FilamentDTOMapper filamentDTOMapper;
    private final FilamentRepository filamentRepository;
    private final FilamentColorRepository filamentColorRepository;
    private final FilamentMaterialRepository filamentMaterialRepository;
    private final PrinterFilamentsRepository printerFilamentsRepository;
    @Autowired
    public FilamentService(
            JwtService jwtService,
            DeviceRepository deviceRepository,
            FilamentDTOMapper filamentDTOMapper,
            FilamentRepository filamentRepository,
            FilamentColorRepository filamentColorRepository,
            PrinterFilamentsRepository printerFilamentsRepository,
            FilamentMaterialRepository filamentMaterialRepository
    ) {
        this.jwtService = jwtService;
        this.deviceRepository = deviceRepository;
        this.filamentDTOMapper = filamentDTOMapper;
        this.filamentRepository = filamentRepository;
        this.filamentColorRepository = filamentColorRepository;
        this.printerFilamentsRepository = printerFilamentsRepository;
        this.filamentMaterialRepository = filamentMaterialRepository;
    }

    public FilamentDTO addFilament(FilamentForm form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        FilamentColor filamentColor = getFilamentColor(form.getColor(),user);
        FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
        Filament filament = Filament.builder()
                .uid(form.getUid())
                .color(filamentColor)
                .company(user.getCompany())
                .quantity(form.getQuantity())
                .material(filamentMaterial)
                .build();
        filamentRepository.save(filament);
        return filamentDTOMapper.apply(filament);
    }

    public List<FilamentDTO> getAllFilaments(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentRepository.findAllByCompany(user.getCompany()).stream()
                .map(filamentDTOMapper)
                .collect(Collectors.toList());
    }

    public FilamentDTO getFilament(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentDTOMapper.apply(getFilament(user,id));
    }

    public Filament updateFilament(Long id, HttpServletRequest request, FilamentForm form) {
        User user = jwtService.extractUser(request);
        Filament filament = getFilament(user,id);
        if(!filament.getColor().getColor().equals(form.getColor())){
            FilamentColor filamentColor = getFilamentColor(form.getColor(), user);
            filament.setColor(filamentColor);
        }
        if(!filament.getMaterial().getMaterial().equals(form.getMaterial())) {
            FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
            filament.setMaterial(filamentMaterial);
        }
        filamentRepository.save(filament);
        return filament;
    }

    public void deleteFilament(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        Optional<Filament> filament = filamentRepository.findByIdAndCompany(id,user.getCompany());
        if(filament.isEmpty()) throw new NotFound404Exception("Filament doesn't found.");
        filamentRepository.delete(filament.get());
    }

    public ArrayList<Filament> addRandomFilaments(int amount, HttpServletRequest request) {
        ArrayList<Filament> filaments = new ArrayList<>();
        User user = jwtService.extractUser(request);
        ArrayList<FilamentColor> filamentColor = new ArrayList<>(
                filamentColorRepository.findAllByCompanyId(user.getCompany().getId())
        );
        ArrayList<FilamentMaterial> filamentMaterials = new ArrayList<>(
                filamentMaterialRepository.findAll()
        );
        Random random = new Random();
        for(int i=0;i<amount;i++){
            filaments.add(
                    Filament.builder()
                            .quantity(random.nextInt(999)+1)
                            .uid(random.nextLong(999999999)+1)
                            .color(filamentColor.get(random.nextInt(filamentColor.size())))
                            .material(filamentMaterials.get(random.nextInt(filamentMaterials.size())))
                            .company(user.getCompany())
                            .build()
            );
        }
        filamentRepository.saveAll(filaments);
        return filaments;
    }

    public List<Filament> getFilteredFilament(
            String color,
            String material,
            double quantity,
            HttpServletRequest request
    ) {
        FilamentColor filamentColor;
        FilamentMaterial filamentMaterial;
        User user = jwtService.extractUser(request);

        if(color.equals("all")) {filamentColor=null;}
        else {filamentColor = getFilamentColor(color,user);}

        if(material.equals("all")) filamentMaterial=null;
        else {filamentMaterial = getFilamentMaterial(material);}

        return filamentRepository.findByColorAndMaterialAndQuantityLessThanAndCompany(
                filamentColor,
                filamentMaterial,
                quantity,
                user.getCompany()
        );
    }

    public void subtraction(FilamentSubtraction form, HttpServletRequest request) {

        Optional<Device> deviceOptional = deviceRepository.findByIp(form.getIp());
        if (deviceOptional.isEmpty()) return;
        Device device = deviceOptional.get();
        Printer printer = device.getPrinter();

        Optional<Filament> filamentOptional = filamentRepository.findByUid(form.getUid());
        if(filamentOptional.isEmpty()) return;
        Filament filament = filamentOptional.get();

        filament.setQuantity(filament.getQuantity()-form.getQuantity());
        filamentRepository.save(filament);

        Optional<PrinterFilaments> printerFilamentsOptional = printerFilamentsRepository
                .findByPrinterAndFilamentMaterial(printer,filament.getMaterial());
        printer.setWorkHours(printer.getWorkHours() + (double) form.getHours()/3600);

        if(printerFilamentsOptional.isEmpty()){
            PrinterFilaments printerFilaments = PrinterFilaments.builder()
                    .printer(printer)
                    .amount(form.getQuantity())
                    .filamentMaterial(filament.getMaterial())
                    .build();
            printerFilamentsRepository.save(printerFilaments);
            return;
        }
        PrinterFilaments printerFilaments = printerFilamentsOptional.get();
        printerFilaments.setAmount(printerFilaments.getAmount()+form.getQuantity());
        printerFilamentsRepository.save(printerFilaments);
    }
    private Filament getFilament(User user,Long id) {
        Optional<Filament> filament = filamentRepository.findByIdAndCompany(id, user.getCompany());
        if (filament.isEmpty()) throw new NotFound404Exception("Filament not found.");
        return filament.get();
    }
    private FilamentColor getFilamentColor(String color,User user){
        Optional<FilamentColor> filamentColor = filamentColorRepository.findByColorAndCompany(
                color,user.getCompany()
        );
        if(filamentColor.isEmpty()) throw new NotFound404Exception("Filament's color doesn't found.");
        return filamentColor.get();
    }
    private FilamentMaterial getFilamentMaterial(String material){
        Optional<FilamentMaterial> filamentMaterial = filamentMaterialRepository.findByMaterial(material);
        if(filamentMaterial.isEmpty()) throw new NotFound404Exception("Filament's material doesn't found.");
        return filamentMaterial.get();
    }
}
