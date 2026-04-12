package pe.edu.cibertec.controller;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.service.ClienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
public class ClienteController {
	private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/buscar/{termino}")
    public List<Cliente> buscar(@PathVariable("termino") String termino) {
        return clienteService.busquedaGlobal(termino);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> obtenerActivos() {
        List<Cliente> clientes = clienteService.obtenerActivos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable("codigo") String codigo) {
        Optional<Cliente> cliente = clienteService.obtenerPorCodigo(codigo);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar/ciudad")
    public ResponseEntity<List<Cliente>> buscarPorCiudad(@RequestParam String ciudad) {
        List<Cliente> clientes = clienteService.buscarPorCiudad(ciudad);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Cliente> obtenerPorCorreo(@PathVariable String correo) {
        Optional<Cliente> cliente = clienteService.obtenerPorCorreo(correo);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<Cliente> obtenerPorTelefono(@PathVariable String telefono) {
        Optional<Cliente> cliente = clienteService.obtenerPorTelefono(telefono);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.crear(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @PathVariable ("id") Long id,
            @RequestBody Cliente cliente) {
    	System.out.println("cliente es : " + cliente);
        try {
            Cliente clienteActualizado = clienteService.actualizar(id, cliente);
            
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        System.out.print("eliminar" + id);
        try {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Long> contarActivos() {
        long total = clienteService.contarActivos();
        return ResponseEntity.ok(total);
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> EliminarCliente(@PathVariable("id") Long id){
    	clienteService.eliminarCliente(id); 
    	return ResponseEntity.ok(null);
    }
    
}
