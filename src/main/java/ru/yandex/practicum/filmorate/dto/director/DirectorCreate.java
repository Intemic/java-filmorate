package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class DirectorCreate {
    private Long id;
    @NotBlank
    private String name;
}
