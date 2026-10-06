package com.relaciones.app.controlador;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.relaciones.app.modelo.Asociacion;
import com.relaciones.app.repositorio.Asociacion_repositorio;
import com.relaciones.app.servicio.ClubServicio;
import com.relaciones.app.servicio.SequenceGeneratorService;

@RestController
@RequestMapping("/api/asociaciones")
public class Asociacion_apirest {

    private final Asociacion_repositorio repositorio;
    private final SequenceGeneratorService generador;
    private final ClubServicio clubServicio;

    public Asociacion_apirest(Asociacion_repositorio repositorio, SequenceGeneratorService generador,
            ClubServicio clubServicio) {
        this.repositorio = repositorio;
        this.generador = generador;
        this.clubServicio = clubServicio;
    }

    @GetMapping
    public List<Asociacion> listar() {
        return repositorio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asociacion> buscar(@PathVariable Long id) {
        return repositorio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Asociacion> crear(@RequestBody Asociacion asociacion) {
        asociacion.setId(generador.generarSecuencia(Asociacion.SEQUENCE_NAME));
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(asociacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asociacion> actualizar(@PathVariable Long id, @RequestBody Asociacion datos) {
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
        if (clubServicio.asociacionEnUso(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No se puede eliminar: hay clubes afiliados a la asociacion"));
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}