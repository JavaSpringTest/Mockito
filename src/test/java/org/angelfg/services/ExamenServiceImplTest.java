package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;
import org.angelfg.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.angelfg.services.Datos.EXAMENES;
import static org.angelfg.services.Datos.PREGUNTAS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamenServiceImplTest {

    private ExamenRepository repository;
    private ExamenService service;
    private PreguntaRepository preguntaRepository;

    @BeforeEach
    void setUp() {
        // con mock nunca se llama el metodo real, si no se genera un mock
        this.repository = mock(ExamenRepository.class);
        this.preguntaRepository = mock(PreguntaRepository.class);
        this.service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {

        when(this.repository.findAll()).thenReturn(EXAMENES);

        Optional<Examen> examen = this.service.findExamenPorNombre("Matemáticas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(this.repository.findAll()).thenReturn(datos);

        Optional<Examen> examen = this.service.findExamenPorNombre("Matemáticas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAll()).thenReturn(EXAMENES);
        // Era 5, se pone anyLong() para tener cualquier long generico
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(PREGUNTAS);

        Examen examen = this.service.findExamenPorNombreConPreguntas("Matemáticas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));
    }

}