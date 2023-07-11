package com.filament.measurement.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilamentColorForm {

    @NotBlank(message = "Filament's color can't be blank")
    private String color;
}
