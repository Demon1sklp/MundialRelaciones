package com.relaciones.app.controlador;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.relaciones.app.modelo.Club;
import com.relaciones.app.repositorio.Asociacion_repositorio;
import com.relaciones.app.repositorio.Club_repositorio;
import com.relaciones.app.repositorio.Competicion_repositorio;
import com.relaciones.app.repositorio.Entrenador_repositorio;
import com.relaciones.app.repositorio.Jugador_repositorio;
import com.relaciones.app.servicio.ClubServicio;

@Controller
public class Club_web {

    private final Club_repositorio clubRepo;
    private final Entrenador_repositorio entrenadorRepo;
    private final Jugador_repositorio jugadorRepo;
    private final Asociacion_repositorio asociacionRepo;
    private final Competicion_repositorio competicionRepo;
    private final ClubServicio servicio;

    public Club_web(Club_repositorio clubRepo, Entrenador_repositorio entrenadorRepo,
            Jugador_repositorio jugadorRepo, Asociacion_repositorio asociacionRepo,
            Competicion_repositorio competicionRepo, ClubServicio servicio) {
        this.clubRepo = clubRepo;
        this.entrenadorRepo = entrenadorRepo;
        this.jugadorRepo = jugadorRepo;
        this.asociacionRepo = asociacionRepo;
        this.competicionRepo = competicionRepo;
        this.servicio = servicio;
    }

    // Llena los select del formulario
    private void cargarListas(Model model) {
        model.addAttribute("entrenadores", entrenadorRepo.findAll());
        model.addAttribute("asociaciones", asociacionRepo.findAll());
        model.addAttribute("jugadores", jugadorRepo.findAll());
        model.addAttribute("competiciones", competicionRepo.findAll());
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("club", new Club());
        cargarListas(model);
        return "index";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("club") Club club,
            @RequestParam(required = false) Long entrenadorId,
            @RequestParam(required = false) Long asociacionId,
            @RequestParam(required = false) List<Long> jugadoresIds,
            @RequestParam(required = false) List<Long> competicionesIds,
            Model model) {
        try {
            servicio.guardarConIds(club, entrenadorId, asociacionId, jugadoresIds, competicionesIds);
            return "redirect:/listar";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarListas(model);
            return "index";
        }
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("clubes", clubRepo.findAll());
        return "listar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("club", clubRepo.findById(id).orElse(new Club()));
        cargarListas(model);
        return "index";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        clubRepo.deleteById(id);
        return "redirect:/listar";
    }
}