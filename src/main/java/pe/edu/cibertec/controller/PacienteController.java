package pe.edu.cibertec.controller;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Paciente;
import pe.edu.cibertec.service.PacienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
public class PacienteController {
	private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> obtenerTodos() {
        List<Paciente> pacientes = pacienteService.obtenerTodos();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Paciente>> obtenerActivos() {
        List<Paciente> pacientes = pacienteService.obtenerActivos();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.obtenerPorId(id);
        return paciente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@RequestParam String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/buscar/especie")
    public ResponseEntity<List<Paciente>> buscarPorEspecie(@RequestParam String especie) {
        List<Paciente> pacientes = pacienteService.buscarPorEspecie(especie);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/buscar/raza")
    public ResponseEntity<List<Paciente>> buscarPorRaza(@RequestParam String raza) {
        List<Paciente> pacientes = pacienteService.buscarPorRaza(raza);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Paciente>> obtenerPorCliente(@PathVariable Long idCliente) {
        List<Paciente> pacientes = pacienteService.obtenerPacientesDelCliente(idCliente);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/alergias/listado")
    public ResponseEntity<List<Paciente>> obtenerConAlergias() {
        List<Paciente> pacientes = pacienteService.obtenerPacientesConAlergias();
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        try {
            Paciente nuevoPaciente = pacienteService.crear(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(
            @PathVariable Long id,
            @RequestBody Paciente paciente) {
        try {
            Paciente pacienteActualizado = pacienteService.actualizar(id, paciente);
            return ResponseEntity.ok(pacienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            pacienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Long> contarActivos() {
        long total = pacienteService.contarActivos();
        return ResponseEntity.ok(total);
    }
}
