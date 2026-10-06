package com.relaciones.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.relaciones.app.modelo.Entrenador;

public interface Entrenador_repositorio extends MongoRepository<Entrenador, Long> {
}