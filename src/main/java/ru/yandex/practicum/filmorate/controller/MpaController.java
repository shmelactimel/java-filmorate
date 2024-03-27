package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getMpas() {
        log.info("get all mpas");
        return mpaService.getMpas();
    }

    @GetMapping(value = "/{id}")
    public Mpa getMpa(@PathVariable(required = true) Long id) {
        log.info("get mpa id :" + id);
        return mpaService.getMpa(id);
    }
}