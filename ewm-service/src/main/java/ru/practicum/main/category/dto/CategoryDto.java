package ru.practicum.main.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CategoryDto {
   private Long id;
   private String name;
}
