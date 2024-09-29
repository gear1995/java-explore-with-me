package ru.practicum.main.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Size(max = 250)
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 254)
    @Email
    private String email;
}
