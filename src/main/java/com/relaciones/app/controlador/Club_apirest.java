package com.relaciones.app.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.relaciones.app.modelo.Club;
import com.relaciones.app.repositorio.Club_repositorio;
import com.relaciones.app.servicio.ClubServicio;

@RestController
@RequestMapping("/api/clubes")
public class Club_apirest {

    private final Club_repositorio repositorio;
    private final ClubServicio servicio;

    public Club_apirest(Club_repositorio repositorio, ClubServicio servicio) {
        this.repositorio = repositorio;
        this.servicio = servicio;
    }

    @GetMapping
    public List<Club> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Club club) {
        try {
            club.setId(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(club));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Club datos) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            datos.setId(id);
            return ResponseEntity.ok(servicio.guardar(datos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}