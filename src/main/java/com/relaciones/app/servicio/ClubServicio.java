package com.relaciones.app.servicio;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.relaciones.app.modelo.Club;
import com.relaciones.app.modelo.Competicion;
import com.relaciones.app.modelo.Jugador;
import com.relaciones.app.repositorio.Asociacion_repositorio;
import com.relaciones.app.repositorio.Club_repositorio;
import com.relaciones.app.repositorio.Competicion_repositorio;
import com.relaciones.app.repositorio.Entrenador_repositorio;
import com.relaciones.app.repositorio.Jugador_repositorio;

@Service
public class ClubServicio {

    private final Club_repositorio clubRepo;
    private final Entrenador_repositorio entrenadorRepo;
    private final Jugador_repositorio jugadorRepo;
    private final Asociacion_repositorio asociacionRepo;
    private final Competicion_repositorio competicionRepo;
    private final SequenceGeneratorService generador;

    public ClubServicio(Club_repositorio clubRepo, Entrenador_repositorio entrenadorRepo,
            Jugador_repositorio jugadorRepo, Asociacion_repositorio asociacionRepo,
            Competicion_repositorio competicionRepo, SequenceGeneratorService generador) {
        this.clubRepo = clubRepo;
        this.entrenadorRepo = entrenadorRepo;
        this.jugadorRepo = jugadorRepo;
        this.asociacionRepo = asociacionRepo;
        this.competicionRepo = competicionRepo;
        this.generador = generador;
    }

    // Usado por el formulario web: recibe solo los ids elegidos en los select
    public Club guardarConIds(Club club, Long entrenadorId, Long asociacionId,
            List<Long> jugadoresIds, List<Long> competicionesIds) {

        club.setEntrenador(entrenadorId == null ? null
                : entrenadorRepo.findById(entrenadorId).orElse(null));
        club.setAsociacion(asociacionId == null ? null
                : asociacionRepo.findById(asociacionId).orElse(null));
        club.setJugadores(jugadoresIds == null ? new ArrayList<>()
                : new ArrayList<>(jugadorRepo.findAllById(jugadoresIds)));
        club.setCompeticiones(competicionesIds == null ? new ArrayList<>()
                : new ArrayList<>(competicionRepo.findAllById(competicionesIds)));

        return guardar(club);
    }

    // Usado por la API REST y por el metodo anterior
    public Club guardar(Club club) {

        // 1. Resolver las referencias (en el JSON solo llega {"id": 1})
        if (club.getEntrenador() != null) {
            Long id = club.getEntrenador().getId();
            club.setEntrenador(entrenadorRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("El entrenador " + id + " no existe")));
        }
        if (club.getAsociacion() != null) {
            Long id = club.getAsociacion().getId();
            club.setAsociacion(asociacionRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("La asociacion " + id + " no existe")));
        }

        List<Jugador> jugadores = new ArrayList<>();
        if (club.getJugadores() != null) {
            for (Jugador j : club.getJugadores()) {
                jugadores.add(jugadorRepo.findById(j.getId())
                        .orElseThrow(() -> new IllegalArgumentException("El jugador " + j.getId() + " no existe")));
            }
        }
        club.setJugadores(jugadores);

        List<Competicion> competiciones = new ArrayList<>();
        if (club.getCompeticiones() != null) {
            for (Competicion c : club.getCompeticiones()) {
                competiciones.add(competicionRepo.findById(c.getId())
                        .orElseThrow(() -> new IllegalArgumentException("La competicion " + c.getId() + " no existe")));
            }
        }
        club.setCompeticiones(competiciones);

        // 2. Validar uno a uno y uno a muchos contra los demas clubes
        for (Club otro : clubRepo.findAll()) {
            if (otro.getId().equals(club.getId())) {
                continue;
            }
            if (club.getEntrenador() != null && otro.getEntrenador() != null
                    && club.getEntrenador().getId().equals(otro.getEntrenador().getId())) {
                throw new IllegalArgumentException(
                        "El entrenador ya dirige al club " + otro.getNombre() + " (relacion uno a uno)");
            }
            for (Jugador j : club.getJugadores()) {
                for (Jugador oj : otro.getJugadores()) {
                    if (j.getId().equals(oj.getId())) {
                        throw new IllegalArgumentException("El jugador " + j.getNombre() + " "
                                + j.getApellido() + " ya pertenece al club " + otro.getNombre()
                                + " (relacion uno a muchos)");
                    }
                }
            }
        }

        // 3. Id incremental y guardado
        if (club.getId() == null) {
            club.setId(generador.generarSecuencia(Club.SEQUENCE_NAME));
        }
        return clubRepo.save(club);
    }

    // Restriccion "foreign key": no borrar lo que un club esta usando
    public boolean entrenadorEnUso(Long id) {
        return clubRepo.findAll().stream()
                .anyMatch(c -> c.getEntrenador() != null && id.equals(c.getEntrenador().getId()));
    }

    public boolean asociacionEnUso(Long id) {
        return clubRepo.findAll().stream()
                .anyMatch(c -> c.getAsociacion() != null && id.equals(c.getAsociacion().getId()));
    }

    public boolean jugadorEnUso(Long id) {
        return clubRepo.findAll().stream()
                .anyMatch(c -> c.getJugadores().stream().anyMatch(j -> id.equals(j.getId())));
    }

    public boolean competicionEnUso(Long id) {
        return clubRepo.findAll().stream()
                .anyMatch(c -> c.getCompeticiones().stream().anyMatch(x -> id.equals(x.getId())));
    }
}