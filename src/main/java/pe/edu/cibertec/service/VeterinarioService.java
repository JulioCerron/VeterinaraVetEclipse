package pe.edu.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.entity.Veterinario;
import pe.edu.cibertec.repository.VeterinarioRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class VeterinarioService {
	 private final VeterinarioRepository veterinarioRepository;

  
    public List<Veterinario> obtenerTodos() {
        return veterinarioRepository.findAll();
    }
    public List<Veterinario> busquedaGlobal(String termino) {
        // Si el término está vacío, devolvemos todos para no dejar la tabla vacía
        if (termino == null || termino.trim().isEmpty()) {
            return veterinarioRepository.findAll();
        }
        List<Veterinario> lista= veterinarioRepository.buscarGlobal(termino);
        return lista;

    }
    
    public List<Veterinario> obtenerActivos() {
        return veterinarioRepository.findAllActivos();
    }

  
    public Optional<Veterinario> obtenerPorId(Long id) {
        return veterinarioRepository.findById(id);
    }


    public List<Veterinario> buscarPorNombre(String nombre) {
        return veterinarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

 
    public List<Veterinario> buscarPorEspecialidad(String especialidad) {
        return veterinarioRepository.findByEspecialidadActivos(especialidad);
    }

    public Optional<Veterinario> obtenerPorNumeroColegiado(String numeroColegiado) {
        return veterinarioRepository.findByNumeroColegiado(numeroColegiado);
    }


    public Veterinario crear(Veterinario veterinario) {
        // Validaciones
        if (veterinario.getNombre() == null || veterinario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (veterinario.getApellido() == null || veterinario.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }

        // 
        if (veterinario.getNumeroColegiado() != null && !veterinario.getNumeroColegiado().isEmpty()) {
            if (veterinarioRepository.findByNumeroColegiado(veterinario.getNumeroColegiado()).isPresent()) {
                throw new IllegalArgumentException("El numero colegiado ya existe");
            }
        }

        return veterinarioRepository.save(veterinario);
    }

    public Veterinario actualizar(Long id, Veterinario veterinarioActualizado) {
        return veterinarioRepository.findById(id).map(veterinario -> {
            if (veterinarioActualizado.getNombre() != null) {
                veterinario.setNombre(veterinarioActualizado.getNombre());
            }
            if (veterinarioActualizado.getApellido() != null) {
                veterinario.setApellido(veterinarioActualizado.getApellido());
            }
            if (veterinarioActualizado.getNumeroColegiado() != null) {
                veterinario.setNumeroColegiado(veterinarioActualizado.getNumeroColegiado());
            }
            if (veterinarioActualizado.getEspecialidad() != null) {
                veterinario.setEspecialidad(veterinarioActualizado.getEspecialidad());
            }
            if (veterinarioActualizado.getCorreo() != null) {
                veterinario.setCorreo(veterinarioActualizado.getCorreo());
            }
            if (veterinarioActualizado.getTelefono() != null) {
                veterinario.setTelefono(veterinarioActualizado.getTelefono());
            }
            if (veterinarioActualizado.getEstado() != null) {
                veterinario.setEstado(veterinarioActualizado.getEstado());
            }
            return veterinarioRepository.save(veterinario);
        }).orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
    }

    public void eliminar(Long id) {
        veterinarioRepository.findById(id).ifPresent(veterinario -> {
            veterinario.setEstado(false);
            veterinarioRepository.save(veterinario);
        });
    }

    public long contarActivos() {
        return veterinarioRepository.countActivos();
    }
}
