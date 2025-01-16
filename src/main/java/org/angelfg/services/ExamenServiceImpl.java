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
    public Examen findExamenPorNombre(String nombre) {

        Optional<Examen> examenOptional = this.examenRepository.findAll()
                .stream()
                .filter(examen -> examen.getNombre().contains(nombre))
                .findFirst();

        Examen examen = null;

        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
        }

        return examen;
    }

}
