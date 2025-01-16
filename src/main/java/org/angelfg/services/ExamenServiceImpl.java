package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;
import org.angelfg.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private final ExamenRepository examenRepository;
    private final PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(
        ExamenRepository examenRepository,
        PreguntaRepository preguntaRepository
    ) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
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

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);

        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = this.preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }

        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {

        if (!examen.getPreguntas().isEmpty()) {
            this.preguntaRepository.guardarVarias(examen.getPreguntas());
        }

        return examenRepository.guardar(examen);
    }

}
