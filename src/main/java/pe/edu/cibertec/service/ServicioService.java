package pe.edu.cibertec.service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Servicio;
import pe.edu.cibertec.entity.Veterinario;
import pe.edu.cibertec.repository.ServicioRepository;
import pe.edu.cibertec.repository.VeterinarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ServicioService {
	private final ServicioRepository servicioRepository;

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }
    
    public List<Servicio> busquedaGlobal(String termino){
    	 // Si el término está vacío, devolvemos todos para no dejar la tabla vacía
        if (termino == null || termino.trim().isEmpty()) {
            return servicioRepository.findAll();
        }
        List<Servicio> lista= servicioRepository.buscarGlobal(termino);
        return lista;
    	
    }

    public List<Servicio> obtenerActivos() {
        return servicioRepository.findAllActivos();
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }

    public List<Servicio> buscarPorNombre(String nombre) {
        return servicioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Servicio> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return servicioRepository.findByRangoPrecio(precioMin, precioMax);
    }

    public Servicio crear(Servicio servicio) {
        // Validaciones - regresa mas tarde aqui
        if (servicio.getNombre() == null || servicio.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es requerido");
        }
        if (servicio.getPrecio() == null || servicio.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        return servicioRepository.save(servicio);
    }


    public Servicio actualizar(Long id, Servicio servicioActualizado) {
        return servicioRepository.findById(id).map(servicio -> {
            if (servicioActualizado.getNombre() != null) {
                servicio.setNombre(servicioActualizado.getNombre());
            }
            if (servicioActualizado.getDescripcion() != null) {
                servicio.setDescripcion(servicioActualizado.getDescripcion());
            }
            if (servicioActualizado.getPrecio() != null) {
                servicio.setPrecio(servicioActualizado.getPrecio());
            }
            if (servicioActualizado.getDuracionEstimada() != null) {
                servicio.setDuracionEstimada(servicioActualizado.getDuracionEstimada());
            }
            if (servicioActualizado.getEstado() != null) {
                servicio.setEstado(servicioActualizado.getEstado());
            }
            return servicioRepository.save(servicio);
        }).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }


    public void eliminar(Long id) {
        servicioRepository.findById(id).ifPresent(servicio -> {
            servicio.setEstado(false);
            servicioRepository.save(servicio);
        });
    }

    public long contarActivos() {
        return servicioRepository.countActivos();
    }
}
