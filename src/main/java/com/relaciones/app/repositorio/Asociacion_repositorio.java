package com.relaciones.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.relaciones.app.modelo.Asociacion;

public interface Asociacion_repositorio extends MongoRepository<Asociacion, Long> {
}