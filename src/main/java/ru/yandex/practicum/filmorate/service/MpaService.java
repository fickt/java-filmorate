package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.datastorage.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
   private MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public ResponseEntity<List<Mpa>> getAllMpa() {
       return mpaDao.getAllMpa();
    }

    public ResponseEntity<Mpa> getMpa(int id) {
        if(id < 0) {
            throw new NotFoundException("Mpa with id " + id + " has not been found!");
        }

       return mpaDao.getMpa(id);
    }
}
