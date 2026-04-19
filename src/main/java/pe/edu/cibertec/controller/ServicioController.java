package pe.edu.cibertec.controller;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Servicio;
import pe.edu.cibertec.entity.Veterinario;
import pe.edu.cibertec.service.ServicioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO', 'USUARIO')")
public class ServicioController {
	private final ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> obtenerTodos() {
        List<Servicio> servicios = servicioService.obtenerTodos();
        return ResponseEntity.ok(servicios);
    }
    
    @GetMapping("/buscar/{termino}")
    public ResponseEntity< List<Servicio>> buscar(@PathVariable("termino") String termino) {
    	List<Servicio> servicio = servicioService.busquedaGlobal(termino);
        return ResponseEntity.ok(servicio);
    }


    @GetMapping("/activos")
    public ResponseEntity<List<Servicio>> obtenerActivos() {
        List<Servicio> servicios = servicioService.obtenerActivos();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable ("id") Long id) {
        Optional<Servicio> servicio = servicioService.obtenerPorId(id);
        return servicio.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Servicio>> buscarPorNombre(@RequestParam String nombre) {
        List<Servicio> servicios = servicioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/buscar/rango")
    public ResponseEntity<List<Servicio>> buscarPorRango(
            @RequestParam Double precioMin,
            @RequestParam Double precioMax) {
        List<Servicio> servicios = servicioService.buscarPorRangoPrecio(precioMin, precioMax);
        return ResponseEntity.ok(servicios);
    }


    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        try {
            Servicio nuevoServicio = servicioService.crear(servicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizar(
            @PathVariable ("id")Long id,
            @RequestBody Servicio servicio) {
        try {
            Servicio servicioActualizado = servicioService.actualizar(id, servicio);
            return ResponseEntity.ok(servicioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            servicioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Long> contarActivos() {
        long total = servicioService.contarActivos();
        return ResponseEntity.ok(total);
    }
}
