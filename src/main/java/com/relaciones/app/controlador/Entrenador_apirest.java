package com.relaciones.app.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.relaciones.app.modelo.Entrenador;
import com.relaciones.app.repositorio.Entrenador_repositorio;
import com.relaciones.app.servicio.ClubServicio;
import com.relaciones.app.servicio.SequenceGeneratorService;

@RestController
@RequestMapping("/api/entrenadores")
public class Entrenador_apirest {

    private final Entrenador_repositorio repositorio;
    private final SequenceGeneratorService generador;
    private final ClubServicio clubServicio;

    public Entrenador_apirest(Entrenador_repositorio repositorio, SequenceGeneratorService generador,
            ClubServicio clubServicio) {
        this.repositorio = repositorio;
        this.generador = generador;
        this.clubServicio = clubServicio;
    }

    @GetMapping
    public List<Entrenador> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenador> buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Entrenador> crear(@RequestBody Entrenador entrenador) {
        entrenador.setId(generador.generarSecuencia(Entrenador.SEQUENCE_NAME));
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(entrenador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenador> actualizar(@PathVariable Long id, @RequestBody Entrenador datos) {
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
        if (clubServicio.entrenadorEnUso(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar: el entrenador dirige un club"));
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}