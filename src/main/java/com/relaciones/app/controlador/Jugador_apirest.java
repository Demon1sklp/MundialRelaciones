package com.relaciones.app.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.relaciones.app.modelo.Jugador;
import com.relaciones.app.repositorio.Jugador_repositorio;
import com.relaciones.app.servicio.ClubServicio;
import com.relaciones.app.servicio.SequenceGeneratorService;

@RestController
@RequestMapping("/api/jugadores")
public class Jugador_apirest {

    private final Jugador_repositorio repositorio;
    private final SequenceGeneratorService generador;
    private final ClubServicio clubServicio;

    public Jugador_apirest(Jugador_repositorio repositorio, SequenceGeneratorService generador,
            ClubServicio clubServicio) {
        this.repositorio = repositorio;
        this.generador = generador;
        this.clubServicio = clubServicio;
    }

    @GetMapping
    public List<Jugador> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jugador> buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Jugador> crear(@RequestBody Jugador jugador) {
        jugador.setId(generador.generarSecuencia(Jugador.SEQUENCE_NAME));
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(jugador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizar(@PathVariable Long id, @RequestBody Jugador datos) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        datos.setId(id);
        return ResponseEntity.ok(repositorio.save(datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (clubServicio.jugadorEnUso(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar: el jugador pertenece a un club"));
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}