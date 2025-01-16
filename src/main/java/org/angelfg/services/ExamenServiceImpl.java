package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {

        return this.examenRepository.findAll()
                .stream()
                .filter(examen -> examen.getNombre().contains(nombre))
                .findFirst();

//        Examen examen = null;
//
//        if (examenOptional.isPresent()) {
//            examen = examenOptional.orElseThrow();
//        }
//        return examen;
    }

}
