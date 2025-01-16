package org.angelfg.services;

import org.angelfg.models.Examen;
import org.angelfg.repositories.ExamenRepository;
import org.angelfg.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.angelfg.services.Datos.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) // Habilitamos las anotaciones de inyeccion de mocks
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

    @Test
    void testGuardarExamen() {
        // Cualquier tipo de examen
        when(repository.guardar(any(Examen.class))).thenReturn(EXAMEN);

        Examen examen = this.service.guardar(EXAMEN);

        assertNotNull(examen.getId());
        //assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        // verify(preguntaRepository).guardarVarias(anyList()); // no se invoca
    }

    @Test
    void testGuardarExamenConPreguntas() {

        // Desarrollo impulsado al comportarmiento - Behavior-driven development (BDD)

        // GIVEN -> Dado a
        Examen newExamen = EXAMEN;
        newExamen.setPreguntas(PREGUNTAS);

        // Cualquier tipo de examen
        // Se incrementa automaticamente si requerimos añadir mas modelos
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }

        });

        // WHEN -> cuando
        Examen examen = this.service.guardar(newExamen);

        // THEN -> entonces
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList()); // no se invoca
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong()))
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.service.findExamenPorNombreConPreguntas("Matemáticas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testManejoExceptionConNull() {
        when(repository.findAll()).thenReturn(EXAMENES_ID_NULL);

        // Al vez de utilizar null, usar isNull()
        when(preguntaRepository.findPreguntasPorExamenId(isNull()))
                .thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.service.findExamenPorNombreConPreguntas("Matemáticas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }

}