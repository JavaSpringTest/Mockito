package org.angelfg.repositories;

import org.angelfg.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
}
