package com.filament.measurement.Filament.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filament.measurement.Authentication.Model.Company;
import com.filament.measurement.Authentication.Model.ElectricityTariff;
import com.filament.measurement.Authentication.Model.User;
import com.filament.measurement.Authentication.Repository.ElectricityTariffRepository;
import com.filament.measurement.Authentication.Service.JwtService;
import com.filament.measurement.Device.Model.AddingDevice;
import com.filament.measurement.Device.Model.MeasuringDevice;
import com.filament.measurement.Device.Repository.AddingDeviceRepository;
import com.filament.measurement.Device.Repository.MeasuringDeviceRepository;
import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Filament.DTO.FilamentChartDTO;
import com.filament.measurement.Filament.DTO.FilamentDTO;
import com.filament.measurement.Filament.DTOMapper.FilamentChartDTOMapper;
import com.filament.measurement.Filament.DTOMapper.FilamentDTOMapper;
import com.filament.measurement.Filament.Model.*;
import com.filament.measurement.Filament.Repository.*;
import com.filament.measurement.Filament.Request.FilamentNotesRequest;
import com.filament.measurement.Filament.Request.FilamentRequest;
import com.filament.measurement.Filament.Request.FilamentSubtractionRequest;
import com.filament.measurement.Printer.Model.Printer;
import com.filament.measurement.Printer.Model.PrinterFilaments;
import com.filament.measurement.Printer.Repository.PrinterFilamentsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilamentService {
    @Value("${client.url}")
    private String clientUrl;
    private final JwtService jwtService;
    private final MeasuringDeviceRepository measuringDeviceRepository;
    private final FilamentDTOMapper filamentDTOMapper;
    private final FilamentRepository filamentRepository;
    private final FilamentColorRepository filamentColorRepository;
    private final FilamentBrandRepository filamentBrandRepository;
    private final FilamentMaterialRepository filamentMaterialRepository;
    private final PrinterFilamentsRepository printerFilamentsRepository;
    private final AddingDeviceRepository addingDeviceRepository;
    private final FilamentNoteRepository filamentNoteRepository;
    private final ElectricityTariffRepository electricityTariffRepository;
    private final FilamentChartRepository filamentChartRepository;
    private final FilamentChartDTOMapper filamentChartDTOMapper;


    public FilamentService(
            JwtService jwtService,
            MeasuringDeviceRepository measuringDeviceRepository,
            FilamentDTOMapper filamentDTOMapper,
            FilamentRepository filamentRepository,
            FilamentColorRepository filamentColorRepository,
            FilamentBrandRepository filamentBrandRepository,
            FilamentMaterialRepository filamentMaterialRepository,
            PrinterFilamentsRepository printerFilamentsRepository,
            AddingDeviceRepository addingDeviceRepository,
            FilamentNoteRepository filamentNoteRepository,
            ElectricityTariffRepository electricityTariffRepository,
            FilamentChartRepository filamentChartRepository,
            FilamentChartDTOMapper filamentChartDTOMapper) {
        this.jwtService = jwtService;
        this.measuringDeviceRepository = measuringDeviceRepository;
        this.filamentDTOMapper = filamentDTOMapper;
        this.filamentRepository = filamentRepository;
        this.filamentColorRepository = filamentColorRepository;
        this.filamentBrandRepository = filamentBrandRepository;
        this.filamentMaterialRepository = filamentMaterialRepository;
        this.printerFilamentsRepository = printerFilamentsRepository;
        this.addingDeviceRepository = addingDeviceRepository;
        this.filamentNoteRepository = filamentNoteRepository;
        this.electricityTariffRepository = electricityTariffRepository;
        this.filamentChartRepository = filamentChartRepository;
        this.filamentChartDTOMapper = filamentChartDTOMapper;
    }
    public FilamentDTO addFilament(FilamentRequest form, HttpServletRequest request) throws IOException, InterruptedException {
        User user = jwtService.extractUser(request);
//        Long uid = getFilamentUidRandom();
        FilamentColor filamentColor = getFilamentColor(form.getColor(),user);
        FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
        FilamentBrand filamentBrand = getFilamentBrand(form.getBrand(),user);
        Long uid = getFilamentUid(form.getDevice(),user.getCompany());

        if(filamentRepository.findByUidAndCompany(uid,user.getCompany()).isPresent())
            throw new CustomValidationException("Filament already exists.");
        Filament filament = saveFilamentIntoTheDB(form.getPrice(),user.getCompany(), form, filamentColor, filamentMaterial,filamentBrand,uid);
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
        return filamentDTOMapper.apply(getFilamentFromDB(request,id));
    }

    public FilamentDTO updateFilament(Long id, HttpServletRequest request, FilamentRequest form) {
        User user = jwtService.extractUser(request);
        Filament filament = getFilamentFromDB(request,id);

        if(!filament.getColor().getColor().equals(form.getColor())){
            FilamentColor filamentColor = getFilamentColor(form.getColor(), user);
            filament.setColor(filamentColor);
        }
        if(!filament.getMaterial().getMaterial().equals(form.getMaterial())) {
            FilamentMaterial filamentMaterial = getFilamentMaterial(form.getMaterial());
            filament.setMaterial(filamentMaterial);
        }
        if(!filament.getBrand().getBrand().equals(form.getBrand())){
            FilamentBrand filamentBrand = getFilamentBrand(form.getBrand(),user);
            filament.setBrand(filamentBrand);
        }
        if(filament.getDiameter() != form.getDiameter()){
            filament.setDiameter(form.getDiameter());
        }
        if(filament.getPrice() != form.getPrice()){
            filament.setPrice(form.getPrice());
        }

        filamentRepository.save(filament);
        return filamentDTOMapper.apply(filament);
    }

    public Object findFilament(HttpServletRequest request, Long deviceId) throws IOException, InterruptedException {
        Company company = jwtService.extractUser(request).getCompany();
        long uid = getFilamentUid(deviceId,company);
        Filament filament = getFilamentByUid(uid,company);
        return filamentDTOMapper.apply(filament);
    }

    public void deleteFilament(Long id, HttpServletRequest request) {
        Filament filament = getFilamentFromDB(request,id);
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
                            .diameter(1.75)
                            .price(50.0)
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
        if(!brand.equals("all")) filamentBrand = getFilamentBrand(brand,user);

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
        Company company = jwtService.extractUser(request).getCompany();
        System.out.println(form.getCompany());
        Printer printer = getPrinterFromMeasuringDeviceIpAndCompany(form.getIp(),company);
        if (printer == null) return;
        printer.setWorkHours(printer.getWorkHours() + (double) form.getHours());

        Filament filament = getFilamentByUidAndCompany(form.getUid(), company);
        if (filament == null) return;

        Optional<PrinterFilaments> printerFilamentsOptional = getPrinterFilaments(printer, filament);

        double mass = calculateFilamentMass(filament, form.getQuantity());
        double electricalTariffPrice = getElectricityTariffPrice(company);
        double price = calculatePrice(form.getHours(),printer.getPower(),mass,filament.getPrice(),electricalTariffPrice);

        filament.setQuantity(filament.getQuantity() - mass);
        filamentRepository.save(filament);
        addDataToChart(filament,mass,company);
        if(printerFilamentsOptional.isEmpty()){
            PrinterFilaments printerFilaments = PrinterFilaments.builder()
                    .printer(printer)
                    .amount(mass)
                    .filamentMaterial(filament.getMaterial())
                    .price(price)
                    .build();
            printerFilamentsRepository.save(printerFilaments);
            return;
        }

        PrinterFilaments printerFilaments = printerFilamentsOptional.get();
        printerFilaments.setAmount(printerFilaments.getAmount()+mass);
        printerFilaments.setPrice(printerFilaments.getPrice()+price);
        printerFilamentsRepository.save(printerFilaments);
    }
    public void addOrUpdateFilamentNote(Long filament_id, HttpServletRequest request, FilamentNotesRequest form) {
        Filament filament = getFilamentFromDB(request, filament_id);
        if(form.getNoteID() == null)
            addNewNote(filament,form.getNote());
        else{
            updateNotes(filament_id,form,request);
        }
    }
    public void deleteFilamentNote(Long filament_id, Long note_id, HttpServletRequest request) {
        FilamentNotes filamentNote = getFilamentNote(filament_id, note_id, request);
        filamentNoteRepository.delete(filamentNote);
    }
    public List<FilamentChartDTO> getFilamentDataChart(HttpServletRequest request) {
        Company company = jwtService.extractUser(request).getCompany();
        return filamentChartRepository.findAllByCompany(company).stream()
                .map(filamentChartDTOMapper)
                .collect(Collectors.toList());
    }

    private void addDataToChart(Filament filament, double mass, Company company) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateString = currentDate.format(formatter);
        Optional<FilamentChart> filamentChartOptional = filamentChartRepository.findByColorAndMaterialAndBrandAndTimeAndCompany(
                filament.getColor().getColor(),
                filament.getMaterial().getMaterial(),
                filament.getBrand().getBrand(),
                formattedDateString,
                company);
        if(filamentChartOptional.isEmpty()){
            FilamentChart filamentChart = FilamentChart.builder()
                    .brand(filament.getBrand().getBrand())
                    .color(filament.getColor().getColor())
                    .company(company)
                    .time(formattedDateString)
                    .material(filament.getMaterial().getMaterial())
                    .quantity(mass)
                    .build();
            filamentChartRepository.save(filamentChart);
            return;
        }
        FilamentChart filamentChart = filamentChartOptional.get();
        filamentChart.setQuantity(filamentChart.getQuantity()+mass);
        filamentChartRepository.save(filamentChart);

    }

    private double getElectricityTariffPrice(Company company) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate currentDate = LocalDate.now();
        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        boolean isWeekend = currentDayOfWeek == DayOfWeek.SATURDAY || currentDayOfWeek == DayOfWeek.SUNDAY;
        boolean workingDays = true;
        boolean weekend = false;
        if(isWeekend){
            workingDays = false;
            weekend = true;
        }
        Optional<ElectricityTariff> tariffOptional = electricityTariffRepository.findValidTariff(localDateTime.getHour(), workingDays, weekend,company);
        if(tariffOptional.isEmpty())
            return 0.0;
        ElectricityTariff electricityTariff = tariffOptional.get();
        return electricityTariff.getPrice();
    }

    private double calculatePrice(double hours, double power, double mass, double price, double electricalTariffPrice) {
        return (hours * (power/1000) * electricalTariffPrice) + (mass * price/1000);
    }

    private double calculateFilamentMass(Filament filament, int quantity) {
        double volume = quantity/10 * 3.14*filament.getDiameter()/20*filament.getDiameter()/20;
        return filament.getMaterial().getDensity() * volume;
    }

    private Optional<PrinterFilaments> getPrinterFilaments(Printer printer, Filament filament) {
        return printerFilamentsRepository
                .findByPrinterAndFilamentMaterial(printer, filament.getMaterial());
    }

    private Filament getFilamentByUidAndCompany(Long uid, Company company) {
        Optional<Filament> filamentOptional = filamentRepository.findByUidAndCompany(uid, company);
        if(filamentOptional.isEmpty()) return null;
        return filamentOptional.get();
    }

    private Printer getPrinterFromMeasuringDeviceIpAndCompany(InetAddress ip, Company company) {
        Optional<MeasuringDevice> deviceOptional = measuringDeviceRepository.findByIpAndCompany(ip,company);
        if (deviceOptional.isEmpty()) return null;
        MeasuringDevice measuringDevice = deviceOptional.get();
        return measuringDevice.getPrinter();
    }


    private Filament getFilamentByUid(long uid, Company company) {
        Optional<Filament> filamentOptional = filamentRepository.findByUidAndCompany(uid,company);
        if(filamentOptional.isEmpty())
            throw new NotFound404Exception("Filament doesn't found");
        return filamentOptional.get();
    }

    private Filament getFilamentFromDB(HttpServletRequest request,Long id) {
        Company company = jwtService.extractUser(request).getCompany();
        Optional<Filament> filament = filamentRepository.findByIdAndCompany(id, company);
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

    private FilamentBrand getFilamentBrand(String brand,User user) {
        Optional<FilamentBrand> filamentBrand = filamentBrandRepository.findByCompanyAndBrand(user.getCompany(),brand);
        if(filamentBrand.isEmpty()) throw new NotFound404Exception("Filament's brand doesn't found.");
        return filamentBrand.get();
    }

    private Filament saveFilamentIntoTheDB(
            double price,
            Company company,
            FilamentRequest form,
            FilamentColor filamentColor,
            FilamentMaterial filamentMaterial,
            FilamentBrand filamentBrand,
            Long uid
    ) {
        Filament filament = Filament.builder()
                .uid(uid)
                .color(filamentColor)
                .company(company)
                .quantity(form.getQuantity())
                .material(filamentMaterial)
                .brand(filamentBrand)
                .diameter(form.getDiameter())
                .price(price)
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
    private Long getFilamentUid(long deviceId, Company company) throws InterruptedException, IOException {
        AddingDevice addingDevice = getAddingDevice(deviceId,company);

        HashMap<String,Object> data = new HashMap<>();
        data.put("port",addingDevice.getPort());
        data.put("ip",addingDevice.getIp().toString().substring(1));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(clientUrl+"filaments/add/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.uri());
        if (postResponse.statusCode() == 504)
            throw new IOException();
        return Long.parseLong(postResponse.body());
    }

    private AddingDevice getAddingDevice(long deviceId, Company company) {
        Optional<AddingDevice> addingDeviceOptional = addingDeviceRepository.findByIdAndCompany(deviceId,company);
        if (addingDeviceOptional.isEmpty())
            throw new NotFound404Exception("Adding device doesn't found.");
        return addingDeviceOptional.get();
    }

    private Long getFilamentUidRandom() {
        Random random = new Random();
        return random.nextLong(10000000,99999999);

    }

    public List<FilamentNotes> getAllFilamentNotes(Long filament_id, HttpServletRequest request) {
        Filament filament = getFilamentFromDB(request, filament_id);
        return filament.getNotes();
    }

    private void updateNotes(Long filament_id, FilamentNotesRequest form, HttpServletRequest request) {
        FilamentNotes filamentNote = getFilamentNote(filament_id, form.getNoteID(), request);
        filamentNote.setNote(form.getNote());
        filamentNoteRepository.save(filamentNote);
    }

    private FilamentNotes getFilamentNote(Long filament_id, Long note_id, HttpServletRequest request) {
        Filament filament = getFilamentFromDB(request, filament_id);
        Optional<FilamentNotes> filamentNotesOptional = filamentNoteRepository.findByFilamentAndId(filament, note_id);
        if(filamentNotesOptional.isEmpty())
            throw new CustomValidationException("Filament's note not found.");
        return filamentNotesOptional.get();
    }

    private void addNewNote(Filament filament, String note) {
        FilamentNotes filamentNotes = FilamentNotes.builder()
                .filament(filament)
                .note(note)
                .build();
        filamentNoteRepository.save(filamentNotes);
    }
}
