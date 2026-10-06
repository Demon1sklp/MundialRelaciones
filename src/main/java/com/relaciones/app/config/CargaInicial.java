package com.relaciones.app.config;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.relaciones.app.modelo.Asociacion;
import com.relaciones.app.modelo.Competicion;
import com.relaciones.app.modelo.Entrenador;
import com.relaciones.app.modelo.Jugador;
import com.relaciones.app.repositorio.Asociacion_repositorio;
import com.relaciones.app.repositorio.Competicion_repositorio;
import com.relaciones.app.repositorio.Entrenador_repositorio;
import com.relaciones.app.repositorio.Jugador_repositorio;
import com.relaciones.app.servicio.SequenceGeneratorService;

@Component
public class CargaInicial implements CommandLineRunner {

    private final Entrenador_repositorio entrenadorRepo;
    private final Jugador_repositorio jugadorRepo;
    private final Asociacion_repositorio asociacionRepo;
    private final Competicion_repositorio competicionRepo;
    private final SequenceGeneratorService generador;

    public CargaInicial(Entrenador_repositorio entrenadorRepo, Jugador_repositorio jugadorRepo,
            Asociacion_repositorio asociacionRepo, Competicion_repositorio competicionRepo,
            SequenceGeneratorService generador) {
        this.entrenadorRepo = entrenadorRepo;
        this.jugadorRepo = jugadorRepo;
        this.asociacionRepo = asociacionRepo;
        this.competicionRepo = competicionRepo;
        this.generador = generador;
    }

    @Override
    public void run(String... args) {
        if (entrenadorRepo.count() > 0 || jugadorRepo.count() > 0) {
            return;
        }

        entrenador("Carlos", "Mendoza", 52, "Colombiana");
        entrenador("Luis", "Herrera", 47, "Argentina");
        entrenador("Andres", "Salazar", 55, "Uruguaya");

        jugador("Juan", "Torres", 1, "Portero");
        jugador("Diego", "Rojas", 4, "Defensa");
        jugador("Mateo", "Cardenas", 8, "Mediocampista");
        jugador("Santiago", "Vargas", 9, "Delantero");
        jugador("Felipe", "Ortiz", 10, "Mediocampista");
        jugador("Camilo", "Duarte", 11, "Delantero");

        asociacion("Federacion Colombiana de Futbol", "Colombia", "Fernando Gil");
        asociacion("Asociacion Sudamericana de Clubes", "Paraguay", "Ricardo Benitez");

        competicion("Liga Nacional", 5000000, LocalDate.of(2026, 1, 25), LocalDate.of(2026, 6, 20));
        competicion("Copa Nacional", 2000000, LocalDate.of(2026, 3, 10), LocalDate.of(2026, 11, 15));
        competicion("Copa Continental", 20000000, LocalDate.of(2026, 2, 5), LocalDate.of(2026, 11, 28));
    }

    private void entrenador(String nombre, String apellido, int edad, String nacionalidad) {
        Entrenador e = new Entrenador();
        e.setId(generador.generarSecuencia(Entrenador.SEQUENCE_NAME));
        e.setNombre(nombre);
        e.setApellido(apellido);
        e.setEdad(edad);
        e.setNacionalidad(nacionalidad);
        entrenadorRepo.save(e);
    }

    private void jugador(String nombre, String apellido, int numero, String posicion) {
        Jugador j = new Jugador();
        j.setId(generador.generarSecuencia(Jugador.SEQUENCE_NAME));
        j.setNombre(nombre);
        j.setApellido(apellido);
        j.setNumero(numero);
        j.setPosicion(posicion);
        jugadorRepo.save(j);
    }

    private void asociacion(String nombre, String pais, String presidente) {
        Asociacion a = new Asociacion();
        a.setId(generador.generarSecuencia(Asociacion.SEQUENCE_NAME));
        a.setNombre(nombre);
        a.setPais(pais);
        a.setPresidente(presidente);
        asociacionRepo.save(a);
    }

    private void competicion(String nombre, int monto, LocalDate inicio, LocalDate fin) {
        Competicion c = new Competicion();
        c.setId(generador.generarSecuencia(Competicion.SEQUENCE_NAME));
        c.setNombre(nombre);
        c.setMontoPremio(monto);
        c.setFechaInicio(inicio);
        c.setFechaFin(fin);
        competicionRepo.save(c);
    }
}