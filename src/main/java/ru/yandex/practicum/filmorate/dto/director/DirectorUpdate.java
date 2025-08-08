package ru.yandex.practicum.filmorate.dto.director;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class DirectorUpdate {
    @NonNull
    private Long id;
    @NotBlank(message = "Имя режиссера не может быть пустым")
    private String name;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }
}
