package pe.edu.cibertec.service;


import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Cita;
import pe.edu.cibertec.repository.CitaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class CitaService {
	
	private final CitaRepository citaRepository;

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> obtenerCitasPaciente(Long idPaciente) {
        return citaRepository.findByPaciente(idPaciente);
    }

    public List<Cita> obtenerCitasVeterinario(Long idVeterinario) {
        return citaRepository.findByVeterinario(idVeterinario);
    }

    public List<Cita> obtenerCitasProgramadasVeterinario(Long idVeterinario) {
        return citaRepository.findCitasProgramadas(idVeterinario);
    }

    public List<Cita> obtenerCitasEnFecha(LocalDateTime fecha) {
        return citaRepository.findByFecha(fecha);
    }

    public List<Cita> obtenerCitasEnRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return citaRepository.findByRangoFechas(fechaInicio, fechaFin);
    }

    public List<Cita> obtenerCitasPorEstado(String estado) {
        return citaRepository.findByEstado(estado);
    }


    public List<Cita> obtenerCitasCompletadas() {
        return citaRepository.findCitasCompletadas();
    }

    public Cita crear(Cita cita) {
        // Validaciones
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son requeridas");
        }
        if (cita.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es requerido");
        }
        if (cita.getVeterinario() == null) {
            throw new IllegalArgumentException("El veterinario es requerido");
        }
        if (cita.getServicio() == null) {
            throw new IllegalArgumentException("El servicio es requerido");
        }

        // Validara que la fecha no sea en el pasado
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser en el pasado");
        }

        // Validara disponibilidad del veterinario - regreso mas tarde aqui
        List<Cita> citasVeterinario = citaRepository.findByVeterinario(cita.getVeterinario().getIdVeterinario());
        boolean disponible = citasVeterinario.stream()
            .noneMatch(c -> c.getFechaHora().equals(cita.getFechaHora()) && !c.getEstado().equals("CANCELADA"));
        
        if (!disponible) {
            throw new IllegalArgumentException("El veterinario no está disponible en esa hora");
        }

        return citaRepository.save(cita);
    }

    public Cita actualizar(Long id, Cita citaActualizada) {
        return citaRepository.findById(id).map(cita -> {
            if (citaActualizada.getFechaHora() != null) {
                cita.setFechaHora(citaActualizada.getFechaHora());
            }
            if (citaActualizada.getMotivo() != null) {
                cita.setMotivo(citaActualizada.getMotivo());
            }
            if (citaActualizada.getNotas() != null) {
                cita.setNotas(citaActualizada.getNotas());
            }
            if (citaActualizada.getDiagnostico() != null) {
                cita.setDiagnostico(citaActualizada.getDiagnostico());
            }
            if (citaActualizada.getTratamiento() != null) {
                cita.setTratamiento(citaActualizada.getTratamiento());
            }
            if (citaActualizada.getEstado() != null) {
                cita.setEstado(citaActualizada.getEstado());
            }
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }


    public Cita cambiarEstado(Long id, String nuevoEstado) {
        return citaRepository.findById(id).map(cita -> {
            // Validara que el nuevo estado sea valido
            if (!nuevoEstado.matches("PROGRAMADA|EN_PROCESO|COMPLETADA|CANCELADA")) {
                throw new IllegalArgumentException("Estado invalido");
            }
            cita.setEstado(nuevoEstado);
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }


    public void eliminar(Long id) {
        citaRepository.findById(id).ifPresent(cita -> {
            cita.setEstado("CANCELADA");
            citaRepository.save(cita);
        });
    }

    public long contarCitasProgramadas() {
        return citaRepository.countCitasProgramadas();
    }
}
