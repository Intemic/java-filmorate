package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.director.DirectorCreate;
import ru.yandex.practicum.filmorate.dto.director.DirectorDTO;
import ru.yandex.practicum.filmorate.dto.director.DirectorUpdate;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("/{id}")
    public DirectorDTO getDirector(@PathVariable @Positive Long id) {
        return directorService.getDirector(id);
    }

    @GetMapping
    public List<DirectorDTO> getAll() {
        return directorService.getAll();
    }

    @PostMapping
    public DirectorDTO create(@RequestBody @Valid DirectorCreate director) {
        return directorService.create(director);
    }

    @PutMapping
    public DirectorDTO update(@RequestBody @Valid DirectorUpdate director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive Long id) {
        directorService.delete(id);
    }
}
