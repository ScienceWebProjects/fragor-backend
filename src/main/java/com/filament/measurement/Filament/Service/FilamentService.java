package com.filament.measurement.Filament.Service;

import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.Model.Device;
import com.filament.measurement.Device.Repository.DeviceRepository;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentDTOMapper;
import com.filament.measurement.Filament.Model.Filament;
import com.filament.measurement.Filament.Model.FilamentBrand;
import com.filament.measurement.Filament.Model.FilamentColor;
import com.filament.measurement.Filament.Model.FilamentMaterial;
import com.filament.measurement.Filament.Repository.FilamentBrandRepository;
import com.filament.measurement.Filament.Repository.FilamentColorRepository;
import com.filament.measurement.Filament.Repository.FilamentMaterialRepository;
import com.filament.measurement.Filament.Repository.FilamentRepository;
import com.filament.measurement.Filament.Request.FilamentRequest;
import com.filament.measurement.Filament.Request.FilamentSubtractionRequest;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import com.filament.measurement.Printer.Repository.PrinterFilamentsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilamentService {
    private final JwtService jwtService;
    private final DeviceRepository deviceRepository;
    private final FilamentDTOMapper filamentDTOMapper;
    private final FilamentRepository filamentRepository;
    private final FilamentColorRepository filamentColorRepository;
    private final FilamentBrandRepository filamentBrandRepository;
    private final FilamentMaterialRepository filamentMaterialRepository;
    private final PrinterFilamentsRepository printerFilamentsRepository;

    public FilamentService(
            JwtService jwtService,
            DeviceRepository deviceRepository,
            FilamentDTOMapper filamentDTOMapper,
            FilamentRepository filamentRepository,
            FilamentColorRepository filamentColorRepository,
            FilamentBrandRepository filamentBrandRepository,
            FilamentMaterialRepository filamentMaterialRepository,
            PrinterFilamentsRepository printerFilamentsRepository
    ) {
        this.jwtService = jwtService;
        this.deviceRepository = deviceRepository;
        this.filamentDTOMapper = filamentDTOMapper;
        this.filamentRepository = filamentRepository;
        this.filamentColorRepository = filamentColorRepository;
        this.filamentBrandRepository = filamentBrandRepository;
        this.filamentMaterialRepository = filamentMaterialRepository;
        this.printerFilamentsRepository = printerFilamentsRepository;
    }
    public FilamentDTO addFilament(FilamentRequest form, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        FilamentColor filamentColor = getFilamentColor(form.getColor(),user);
        FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
        FilamentBrand filamentBrand = getFilamentBrand(form.getBrand());
        Filament filament = saveFilamentIntoTheDB(user.getCompany(), form, filamentColor, filamentMaterial,filamentBrand);
        return filamentDTOMapper.apply(filament);
    }

    public List<FilamentDTO> getAllFilaments(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentRepository.findAllByCompany(user.getCompany())
                .stream()
                .map(filamentDTOMapper)
                .collect(Collectors.toList());
    }
    public FilamentDTO getFilament(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        return filamentDTOMapper.apply(getFilamentFromDB(user,id));
    }

    public FilamentDTO updateFilament(Long id, HttpServletRequest request, FilamentRequest form) {
        User user = jwtService.extractUser(request);
        Filament filament = getFilamentFromDB(user,id);

        if(!filament.getColor().getColor().equals(form.getColor())){
            FilamentColor filamentColor = getFilamentColor(form.getColor(), user);
            filament.setColor(filamentColor);
        }
        if(!filament.getMaterial().getMaterial().equals(form.getMaterial())) {
            FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
            filament.setMaterial(filamentMaterial);
        }
        filamentRepository.save(filament);
        return filamentDTOMapper.apply(filament);
    }

    public void deleteFilament(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        Filament filament = getFilamentFromDB(user,id);
        filamentRepository.delete(filament);
    }

    public void addRandomFilaments(int amount, HttpServletRequest request) {
        // this is dummy function only for developer purpose
        List<Filament> filaments = new LinkedList<>();
        User user = jwtService.extractUser(request);
        List<FilamentColor> filamentColor = filamentColorRepository.findAllByCompanyId(user.getCompany().getId());
        List<FilamentMaterial> filamentMaterials = filamentMaterialRepository.findAll();
        List<FilamentBrand> filamentBrands = filamentBrandRepository.findAllByCompanyId(user.getCompany().getId());

        Random random = new Random();
        for(int i=0;i<amount;i++){
            filaments.add(
                    Filament.builder()
                            .quantity(random.nextInt(999)+1)
                            .uid(random.nextLong(999999999)+1)
                            .color(filamentColor.get(random.nextInt(filamentColor.size())))
                            .material(filamentMaterials.get(random.nextInt(filamentMaterials.size())))
                            .company(user.getCompany())
                            .brand(filamentBrands.get(random.nextInt(filamentBrands.size())))
                            .build()
            );
        }
        filamentRepository.saveAll(filaments);
    }
    public HashMap<String, Set<String>> getFilamentsFilters(HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        List<Filament> filamentList = filamentRepository.findAllByCompany(user.getCompany());
        HashMap<String,Set<String>> filters = new HashMap<>();
        filters.put("brand",getUniqueBrand(filamentList));
        filters.put("color",getUniqueColor(filamentList));
        filters.put("material",getUniqueMaterial(filamentList));
        return filters;
    }

    public List<FilamentDTO> getFilteredFilament(
            String color,
            String material,
            String brand,
            double quantity,
            HttpServletRequest request
    ) {
        FilamentColor filamentColor = null;
        FilamentMaterial filamentMaterial = null;
        FilamentBrand filamentBrand = null;
        User user = jwtService.extractUser(request);

        if(!color.equals("all")) filamentColor = getFilamentColor(color,user);
        if(!material.equals("all")) filamentMaterial = getFilamentMaterial(material);
        if(!brand.equals("all")) filamentBrand = getFilamentBrand(brand);

        return filamentRepository.findByColorAndMaterialAndCompanyAndBrandAndQuantityLessThan(
                        filamentColor,
                        filamentMaterial,
                        filamentBrand,
                        user.getCompany(),
                        quantity
                ).stream()
                .map(filamentDTOMapper)
                .collect(Collectors.toList());
    }

    public void subtraction(FilamentSubtractionRequest form, HttpServletRequest request) {

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

    private Filament getFilamentFromDB(User user,Long id) {
        Optional<Filament> filament = filamentRepository.findByIdAndCompany(id, user.getCompany());
        if (filament.isEmpty()) throw new NotFound404Exception("Filament doesn't found.");
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
    private FilamentBrand getFilamentBrand(String brand) {
        Optional<FilamentBrand> filamentBrand = filamentBrandRepository.findByBrand(brand);
        if(filamentBrand.isEmpty()) throw new NotFound404Exception("Filament's brand doesn't found.");
        return filamentBrand.get();
    }

    private Filament saveFilamentIntoTheDB(
            Company company,
            FilamentRequest form,
            FilamentColor filamentColor,
            FilamentMaterial filamentMaterial,
            FilamentBrand filamentBrand
    ) {
        Filament filament = Filament.builder()
                .uid(form.getUid())
                .color(filamentColor)
                .company(company)
                .quantity(form.getQuantity())
                .material(filamentMaterial)
                .brand(filamentBrand)
                .build();
        filamentRepository.save(filament);
        return filament;
    }
    private static Set<String> getUniqueMaterial(List<Filament> filamentList) {
        return filamentList.stream()
                .map(f-> f.getMaterial().getMaterial())
                .collect(Collectors.toSet());
    }

    private static Set<String> getUniqueColor(List<Filament> filamentList) {
        return filamentList.stream()
                .map(f-> f.getColor().getColor())
                .collect(Collectors.toSet());
    }

    private static Set<String> getUniqueBrand(List<Filament> filamentList) {
        return filamentList.stream()
                .map(f -> f.getBrand().getBrand())
                .collect(Collectors.toSet());
    }


}
