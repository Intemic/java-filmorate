package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.director.DirectorCreate;
import ru.yandex.practicum.filmorate.dto.director.DirectorDTO;
import ru.yandex.practicum.filmorate.dto.director.DirectorUpdate;
import ru.yandex.practicum.filmorate.exeption.NotFoundResource;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorDTO getDirector(Long id) {
        return DirectorMapper.mapDirectorDTO(
                directorStorage.get(id).orElseThrow(() -> new NotFoundResource("Директор не найден")));
    }

    public List<DirectorDTO> getAll() {
        return directorStorage.getAll().stream()
                .map(director -> DirectorMapper.mapDirectorDTO(director))
                .toList();
    }

    public DirectorDTO create(DirectorCreate directorCreate) {
        return DirectorMapper.mapDirectorDTO(directorStorage.create(DirectorMapper.mapToDirector(directorCreate)));
    }

    public DirectorDTO update(DirectorUpdate directorUpdate) {
        Director directorOld = directorStorage.get(
                directorUpdate.getId()).orElseThrow(() -> new NotFoundResource("Директор не найден"));

        return DirectorMapper.mapDirectorDTO(
                directorStorage.update(DirectorMapper.updateDirectorFields(directorOld, directorUpdate)));
    }

    public void delete(Long id) {
        directorStorage.delete(id);
    }
}
