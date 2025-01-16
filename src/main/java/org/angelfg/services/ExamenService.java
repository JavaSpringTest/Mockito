package org.angelfg.services;

import org.angelfg.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
