package ru.yandex.practicum.filmorate.exeptions;

public class DuplicatedDataException extends RuntimeException {
  public DuplicatedDataException(String msg) {
      super(msg);
  }
}
