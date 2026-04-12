package pe.edu.cibertec.service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.repository.ClienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
	
	 private final ClienteRepository clienteRepository;

	    
	    public List<Cliente> obtenerTodos() {
	        return clienteRepository.findAll();
	    }
	    
	    public List<Cliente> busquedaGlobal(String termino) {
	        // Si el término está vacío, devolvemos todos para no dejar la tabla vacía
	        if (termino == null || termino.trim().isEmpty()) {
	            return clienteRepository.findAll();
	        }
	        List<Cliente> lista= clienteRepository.buscarGlobal(termino);
	        return lista;
	    }
	    public List<Cliente> obtenerActivos() {
	        return clienteRepository.findAllActivos();
	    }

	    //esto lodejo hasta aqui
	    public Optional<Cliente> obtenerPorCodigo(String codigo) {
	        return clienteRepository.findByCodigo(codigo);
	    }

	    public List<Cliente> buscarPorNombre(String nombre) {
	        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
	    }


	    public List<Cliente> buscarPorCiudad(String ciudad) {
	        return clienteRepository.findByCiudadIgnoreCase(ciudad);
	    }

	    public Optional<Cliente> obtenerPorCorreo(String correo) {
	        return clienteRepository.findByCorreo(correo);
	    }


	    public Optional<Cliente> obtenerPorTelefono(String telefono) {
	        return clienteRepository.findByTelefono(telefono);
	    }

	    public Cliente crear(Cliente cliente) {
	        // Validaciones
	        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
	            throw new IllegalArgumentException("El nombre es requerido");
	        }
	        if (cliente.getApellido() == null || cliente.getApellido().isEmpty()) {
	            throw new IllegalArgumentException("El apellido es requerido");
	        }

	        // Verificar correo unico
	        if (cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()) {
	            if (clienteRepository.findByCorreo(cliente.getCorreo()).isPresent()) {
	                throw new IllegalArgumentException("El correo ya existe");
	            }
	        }

	        return clienteRepository.save(cliente);
	    }

	    public Cliente actualizar(Long id, Cliente clienteActualizado) {
	        return clienteRepository.findById(id).map(cliente -> {
	            if (clienteActualizado.getNombre() != null) {
	                cliente.setNombre(clienteActualizado.getNombre());
	            }
	            if (clienteActualizado.getApellido() != null) {
	                cliente.setApellido(clienteActualizado.getApellido());
	            }
	            if (clienteActualizado.getCorreo() != null) {
	                cliente.setCorreo(clienteActualizado.getCorreo());
	            }
	            if (clienteActualizado.getTelefono() != null) {
	                cliente.setTelefono(clienteActualizado.getTelefono());
	            }
	            if (clienteActualizado.getDireccion() != null) {
	                cliente.setDireccion(clienteActualizado.getDireccion());
	            }
	            if (clienteActualizado.getCiudad() != null) {
	                cliente.setCiudad(clienteActualizado.getCiudad());
	            }
	            if (clienteActualizado.getEstado() != null) {
	                cliente.setEstado(clienteActualizado.getEstado());
	            }
	            return clienteRepository.save(cliente);
	        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
	    }

	    public void eliminar(Long id) {
	        clienteRepository.findById(id).ifPresent(cliente -> {
	            cliente.setEstado(false);
	            clienteRepository.save(cliente);
	        });
	    }
	    
	    public void eliminarCliente(Long id) {
	    	clienteRepository.deleteById(id);;
	    }


	    public long contarActivos() {
	        return clienteRepository.countActivos();
	    }
}
