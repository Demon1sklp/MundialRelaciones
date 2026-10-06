package com.relaciones.app.modelo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "club")
public class Club {

    public static final String SEQUENCE_NAME = "club_sequence";

    @Id
    private Long id;
    private String nombre;
    private String ciudad;

    // Relacion uno a uno (@OneToOne)
    @DocumentReference
    private Entrenador entrenador;

    // Relacion uno a muchos (@OneToMany)
    @DocumentReference
    private List<Jugador> jugadores = new ArrayList<>();

    // Relacion muchos a uno (@ManyToOne)
    @DocumentReference
    private Asociacion asociacion;

    // Relacion muchos a muchos (@ManyToMany)
    @DocumentReference
    private List<Competicion> competiciones = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    public Asociacion getAsociacion() { return asociacion; }
    public void setAsociacion(Asociacion asociacion) { this.asociacion = asociacion; }

    public List<Competicion> getCompeticiones() { return competiciones; }
    public void setCompeticiones(List<Competicion> competiciones) { this.competiciones = competiciones; }
}