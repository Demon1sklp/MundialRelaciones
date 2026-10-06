package com.relaciones.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.relaciones.app.modelo.Jugador;

public interface Jugador_repositorio extends MongoRepository<Jugador, Long> {
}