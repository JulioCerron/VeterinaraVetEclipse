package pe.edu.cibertec.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.entity.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente ORDER BY c.fechaHora DESC")
    List<Cita> findByPaciente(@Param("idPaciente") Long idPaciente);


    @Query("SELECT c FROM Cita c WHERE c.veterinario.idVeterinario = :idVeterinario ORDER BY c.fechaHora DESC")
    List<Cita> findByVeterinario(@Param("idVeterinario") Long idVeterinario);


    @Query("SELECT c FROM Cita c WHERE DATE(c.fechaHora) = DATE(:fecha) ORDER BY c.fechaHora ASC")
    List<Cita> findByFecha(@Param("fecha") LocalDateTime fecha);

  
    List<Cita> findByEstado(String estado);

  
    @Query("SELECT c FROM Cita c WHERE c.veterinario.idVeterinario = :idVeterinario AND c.estado = 'PROGRAMADA' ORDER BY c.fechaHora ASC")
    List<Cita> findCitasProgramadas(@Param("idVeterinario") Long idVeterinario);

  
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :fechaInicio AND :fechaFin ORDER BY c.fechaHora ASC")
    List<Cita> findByRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);


    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = 'PROGRAMADA'")
    long countCitasProgramadas();

   
    @Query("SELECT c FROM Cita c WHERE c.estado = 'COMPLETADA' ORDER BY c.fechaHora DESC")
    List<Cita> findCitasCompletadas();
}
