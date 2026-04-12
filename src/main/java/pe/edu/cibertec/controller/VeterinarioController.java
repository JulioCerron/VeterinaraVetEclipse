package pe.edu.cibertec.controller;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.entity.Veterinario;
import pe.edu.cibertec.service.VeterinarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veterinarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasRole('ADMIN')")
public class VeterinarioController {
	private final VeterinarioService veterinarioService;

    @GetMapping
    public ResponseEntity<List<Veterinario>> obtenerTodos() {
        List<Veterinario> veterinarios = veterinarioService.obtenerTodos();
        return ResponseEntity.ok(veterinarios);
    }
    
    @GetMapping("/buscar/{termino}")
    public ResponseEntity< List<Veterinario>> buscar(@PathVariable("termino") String termino) {
    	List<Veterinario> veterinarios = veterinarioService.busquedaGlobal(termino);
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Veterinario>> obtenerActivos() {
        List<Veterinario> veterinarios = veterinarioService.obtenerActivos();
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> obtenerPorId(@PathVariable Long id) {
        Optional<Veterinario> veterinario = veterinarioService.obtenerPorId(id);
        return veterinario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Veterinario>> buscarPorNombre(@RequestParam String nombre) {
        List<Veterinario> veterinarios = veterinarioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/buscar/especialidad")
    public ResponseEntity<List<Veterinario>> buscarPorEspecialidad(@RequestParam String especialidad) {
        List<Veterinario> veterinarios = veterinarioService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(veterinarios);
    }

    @GetMapping("/colegiado/{numeroColegiado}")
    public ResponseEntity<Veterinario> obtenerPorNumeroColegiado(@PathVariable String numeroColegiado) {
        Optional<Veterinario> veterinario = veterinarioService.obtenerPorNumeroColegiado(numeroColegiado);
        return veterinario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Veterinario> crear(@RequestBody Veterinario veterinario) {
        try {
            Veterinario nuevoVeterinario = veterinarioService.crear(veterinario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVeterinario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> actualizar(
            @PathVariable Long id,
            @RequestBody Veterinario veterinario) {
        try {
            Veterinario veterinarioActualizado = veterinarioService.actualizar(id, veterinario);
            return ResponseEntity.ok(veterinarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            veterinarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Long> contarActivos() {
        long total = veterinarioService.contarActivos();
        return ResponseEntity.ok(total);
    }
}
