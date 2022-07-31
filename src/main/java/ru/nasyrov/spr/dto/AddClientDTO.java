package ru.nasyrov.spr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddClientDTO {

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "\\d{11}")
    private String phone;

    @Email
    private String email;
}
