package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;
import org.angelfg.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.angelfg.services.Datos.EXAMENES;
import static org.angelfg.services.Datos.PREGUNTAS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    @Mock // Generar mock para no integrar instancia
    private ExamenRepository repository;

    @Mock // Generar mock para no integrar instancia
    private PreguntaRepository preguntaRepository;

    @InjectMocks // Aqui es donde se inyectan los mocks
    private ExamenServiceImpl service; // no integrar la interfaz, sino la implementacion del servicio

    @BeforeEach
    void setUp() {
        // Manejar inyeccion de depednencias
        MockitoAnnotations.openMocks(this); // Habilitamos las anotaciones de inyeccion de mocks

        // con mock nunca se llama el metodo real, si no se genera un mock
//        this.repository = mock(ExamenRepository.class);
//        this.preguntaRepository = mock(PreguntaRepository.class);
//        this.service = new ExamenServiceImpl(repository, preguntaRepository);
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

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAll()).thenReturn(EXAMENES);
        // Era 5, se pone anyLong() para tener cualquier long generico
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(PREGUNTAS);

        Examen examen = this.service.findExamenPorNombreConPreguntas("Matemáticas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmética"));

        // Se verifica si se llama internamente los metodos
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(PREGUNTAS);

        Examen examen = this.service.findExamenPorNombreConPreguntas("Matemáticas");

        assertNull(examen);

        // Se verifica si se llama internamente los metodos
        verify(repository).findAll();
        // verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

}