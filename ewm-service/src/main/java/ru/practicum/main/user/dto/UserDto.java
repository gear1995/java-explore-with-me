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

    @Size(min = 2, max = 250)
    @NotBlank(message = "User name must be not blank")
    @NotNull
    private String name;

    @NotBlank(message = "User email must be not blank")
    @Size(min = 6, max = 254)
    @Email
    private String email;
}
