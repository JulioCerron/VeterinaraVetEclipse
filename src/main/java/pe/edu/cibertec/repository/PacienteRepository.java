package pe.edu.cibertec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);

    List<Paciente> findByEspecie(String especie);

    List<Paciente> findByRaza(String raza);

    @Query("SELECT p FROM Paciente p WHERE p.cliente.idCliente = :idCliente")
    List<Paciente> findByClienteActivos(@Param("idCliente") Long idCliente);

    @Query("SELECT p FROM Paciente p WHERE p.estado = true ORDER BY p.nombre ASC")
    List<Paciente> findAllActivos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.estado = true")
    long countActivos();

    @Query("SELECT p FROM Paciente p WHERE p.alergias IS NOT NULL AND p.alergias <> '' AND p.estado = true")
    List<Paciente> findPacientesConAlergias();
}
