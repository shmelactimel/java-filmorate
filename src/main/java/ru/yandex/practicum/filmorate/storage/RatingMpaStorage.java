package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;

public interface RatingMpaStorage {

    Collection<RatingMpa> findAll();

    RatingMpa findById(Integer id);
}