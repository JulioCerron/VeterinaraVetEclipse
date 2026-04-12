package pe.edu.cibertec.controller;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cita;
import pe.edu.cibertec.service.CitaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'USUARIO')")
public class CitaController {
	private final CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> obtenerTodas() {
        List<Cita> citas = citaService.obtenerTodas();
        return ResponseEntity.ok(citas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        Optional<Cita> cita = citaService.obtenerPorId(id);
        return cita.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Cita>> obtenerPorPaciente(@PathVariable Long idPaciente) {
        List<Cita> citas = citaService.obtenerCitasPaciente(idPaciente);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<Cita>> obtenerPorVeterinario(@PathVariable Long idVeterinario) {
        List<Cita> citas = citaService.obtenerCitasVeterinario(idVeterinario);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/veterinario/{idVeterinario}/programadas")
    public ResponseEntity<List<Cita>> obtenerCitasProgramadas(@PathVariable Long idVeterinario) {
        List<Cita> citas = citaService.obtenerCitasProgramadasVeterinario(idVeterinario);
        return ResponseEntity.ok(citas);
    }


    @GetMapping("/rango")
    public ResponseEntity<List<Cita>> obtenerEnRangoFechas(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
        LocalDateTime fin = LocalDateTime.parse(fechaFin);
        List<Cita> citas = citaService.obtenerCitasEnRangoFechas(inicio, fin);
        return ResponseEntity.ok(citas);
    }


    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cita>> obtenerPorEstado(@PathVariable String estado) {
        List<Cita> citas = citaService.obtenerCitasPorEstado(estado);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/completadas")
    public ResponseEntity<List<Cita>> obtenerCompletadas() {
        List<Cita> citas = citaService.obtenerCitasCompletadas();
        return ResponseEntity.ok(citas);
    }

    @PostMapping
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {
        try {
            Cita nuevaCita = citaService.crear(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizar(
            @PathVariable Long id,
            @RequestBody Cita cita) {
        try {
            Cita citaActualizada = citaService.actualizar(id, cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Cambia el estado de una cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            Cita citaActualizada = citaService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            citaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Ccitas programadas
    @GetMapping("/estadisticas/programadas")
    public ResponseEntity<Long> contarProgramadas() {
        long total = citaService.contarCitasProgramadas();
        return ResponseEntity.ok(total);
    }
}
