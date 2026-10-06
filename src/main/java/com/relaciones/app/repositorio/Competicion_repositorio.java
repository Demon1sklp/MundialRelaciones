package com.relaciones.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.relaciones.app.modelo.Competicion;

public interface Competicion_repositorio extends MongoRepository<Competicion, Long> {
}