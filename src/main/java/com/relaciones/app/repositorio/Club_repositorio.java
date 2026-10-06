package com.relaciones.app.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.relaciones.app.modelo.Club;

public interface Club_repositorio extends MongoRepository<Club, Long> {
}