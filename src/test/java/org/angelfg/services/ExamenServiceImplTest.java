package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;
import org.angelfg.repositories.ExamenRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    @Test
    void findExamenPorNombre() {
        // con mock nunca se llama el metodo real, si no se genera un mock
        ExamenRepository repository = mock(ExamenRepository.class);

        List<Examen> datos = Arrays.asList(
                new Examen(5L, "Matemáticas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia")
        );

        when(repository.findAll()).thenReturn(datos);

        ExamenService service = new ExamenServiceImpl(repository);
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        // con mock nunca se llama el metodo real, si no se genera un mock
        ExamenRepository repository = mock(ExamenRepository.class);

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);

        ExamenService service = new ExamenServiceImpl(repository);
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        assertFalse(examen.isPresent());
    }

}