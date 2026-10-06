package com.relaciones.app.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.relaciones.app.modelo.Competicion;
import com.relaciones.app.repositorio.Competicion_repositorio;
import com.relaciones.app.servicio.ClubServicio;
import com.relaciones.app.servicio.SequenceGeneratorService;

@RestController
@RequestMapping("/api/competiciones")
public class Competicion_apirest {

    private final Competicion_repositorio repositorio;
    private final SequenceGeneratorService generador;
    private final ClubServicio clubServicio;

    public Competicion_apirest(Competicion_repositorio repositorio, SequenceGeneratorService generador,
            ClubServicio clubServicio) {
        this.repositorio = repositorio;
        this.generador = generador;
        this.clubServicio = clubServicio;
    }

    @GetMapping
    public List<Competicion> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competicion> buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Competicion> crear(@RequestBody Competicion competicion) {
        competicion.setId(generador.generarSecuencia(Competicion.SEQUENCE_NAME));
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(competicion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Competicion> actualizar(@PathVariable Long id, @RequestBody Competicion datos) {
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
        if (clubServicio.competicionEnUso(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar: hay clubes participando en la competicion"));
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}