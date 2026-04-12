package pe.edu.cibertec.service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Paciente;
import pe.edu.cibertec.repository.PacienteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteService {
	private final PacienteRepository pacienteRepository;

    /**
     * Obtiene todos los pacientes
     */
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    /**
     * Obtiene todos los pacientes activos
     */
    public List<Paciente> obtenerActivos() {
        return pacienteRepository.findAllActivos();
    }

    /**
     * Obtiene un paciente por ID
     */
    public Optional<Paciente> obtenerPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    /**
     * Obtiene pacientes por nombre
     */
    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene pacientes por especie
     */
    public List<Paciente> buscarPorEspecie(String especie) {
        return pacienteRepository.findByEspecie(especie);
    }

    /**
     * Obtiene pacientes por raza
     */
    public List<Paciente> buscarPorRaza(String raza) {
        return pacienteRepository.findByRaza(raza);
    }

    /**
     * Obtiene los pacientes de un cliente especifico
     */
    public List<Paciente> obtenerPacientesDelCliente(Long idCliente) {
        return pacienteRepository.findByClienteActivos(idCliente);
    }

    /**
     * Obtiene pacientes que tienen alergias registradas
     */
    public List<Paciente> obtenerPacientesConAlergias() {
        return pacienteRepository.findPacientesConAlergias();
    }

    /**
     * Crea un nuevo paciente
     */
    public Paciente crear(Paciente paciente) {
        // Validaciones
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente es requerido");
        }
        if (paciente.getEspecie() == null || paciente.getEspecie().isEmpty()) {
            throw new IllegalArgumentException("La especie es requerida");
        }
        if (paciente.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es requerido");
        }

        return pacienteRepository.save(paciente);
    }

    /**
     * Actualiza un paciente
     */
    public Paciente actualizar(Long id, Paciente pacienteActualizado) {
        return pacienteRepository.findById(id).map(paciente -> {
            if (pacienteActualizado.getNombre() != null) {
                paciente.setNombre(pacienteActualizado.getNombre());
            }
            if (pacienteActualizado.getEspecie() != null) {
                paciente.setEspecie(pacienteActualizado.getEspecie());
            }
            if (pacienteActualizado.getRaza() != null) {
                paciente.setRaza(pacienteActualizado.getRaza());
            }
            if (pacienteActualizado.getEdad() != null) {
                paciente.setEdad(pacienteActualizado.getEdad());
            }
            if (pacienteActualizado.getPeso() != null) {
                paciente.setPeso(pacienteActualizado.getPeso());
            }
            if (pacienteActualizado.getColor() != null) {
                paciente.setColor(pacienteActualizado.getColor());
            }
            if (pacienteActualizado.getAlergias() != null) {
                paciente.setAlergias(pacienteActualizado.getAlergias());
            }
            if (pacienteActualizado.getEstado() != null) {
                paciente.setEstado(pacienteActualizado.getEstado());
            }
            return pacienteRepository.save(paciente);
        }).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    /**
     * Elimina un paciente (desactivacion logica)
     */
    public void eliminar(Long id) {
        pacienteRepository.findById(id).ifPresent(paciente -> {
            paciente.setEstado(false);
            pacienteRepository.save(paciente);
        });
    }

    /**
     * Obtiene el total de pacientes activos
     */
    public long contarActivos() {
        return pacienteRepository.countActivos();
    }
}
