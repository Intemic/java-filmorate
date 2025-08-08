package ru.yandex.practicum.filmorate.dto.director;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorShort {
    @NonNull
    private Long id;
}
