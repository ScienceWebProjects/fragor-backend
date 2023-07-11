package com.filament.measurement.Service;

import com.filament.measurement.Exception.CustomValidationException;
import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Form.FilamentColorForm;
import com.filament.measurement.Model.FilamentColor;
import com.filament.measurement.Model.User;
import com.filament.measurement.Repository.FilamentColorRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilamentColorService {
    @Autowired
    JwtService jwtService;
    @Autowired
    FilamentColorRepository filamentColorRepository;
    public List<FilamentColor> getAllColor(HttpServletRequest request) {
        User user = jwtService.extractUser(request);

        return filamentColorRepository.findAllByCompanyId(user.getCompany().getId());
    }

    public FilamentColor addNewFilamentsColor(
            @RequestBody FilamentColorForm form,
            HttpServletRequest request
    )
    {
        User user = jwtService.extractUser(request);
        //////////////////////////////////////////////////////////////////////////////////
        if(filamentColorRepository.colorExists(user.getCompany(), form.getColor()))
            throw new CustomValidationException("Color already exists");
        //////////////////////////////////////////////////////////////////////////////////
        FilamentColor filamentColor = FilamentColor.builder()
                .color(form.getColor())
                .company(user.getCompany())
                .build();
        filamentColorRepository.save(filamentColor);
        return  filamentColor;
    }

    public void deleteFilamentColor(Long id, HttpServletRequest request) {
        User user = jwtService.extractUser(request);
        Optional<FilamentColor> filamentColor = filamentColorRepository.findByIdAndCompany(id,user.getCompany());
        if (filamentColor.isEmpty()) throw new NotFound404Exception("Filament color doesn't exists");
        filamentColorRepository.delete(filamentColor.get());
    }
}
