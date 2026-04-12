package pe.edu.cibertec.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.entity.Veterinario;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long>  {
	

    List<Veterinario> findByNombreContainingIgnoreCase(String nombre);


    List<Veterinario> findByEspecialidad(String especialidad);

 
    Optional<Veterinario> findByNumeroColegiado(String numeroColegiado);

    @Query("SELECT v FROM Veterinario v WHERE v.estado = true ORDER BY v.nombre ASC")
    List<Veterinario> findAllActivos();
  
  
    @Query("SELECT v FROM Veterinario v WHERE v.especialidad = :especialidad AND v.estado = true")
    List<Veterinario> findByEspecialidadActivos(@Param("especialidad") String especialidad);

    @Query("SELECT COUNT(v) FROM Veterinario v WHERE v.estado = true")
    long countActivos();
    
    @Query("SELECT v FROM Veterinario v WHERE " +
    	       "LOWER(v.codigo) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(v.nombre) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(v.apellido) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(v.numeroColegiado) LIKE LOWER(CONCAT('%', :t, '%')) OR " + 
    	       "LOWER(v.especialidad) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(v.correo) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "v.telefono LIKE CONCAT('%', :t, '%') OR " +
    	       "(LOWER(:t) = 'activo' AND v.estado = true) OR " +
    	       "(LOWER(:t) = 'inactivo' AND v.estado = false)")
    	List<Veterinario> buscarGlobal(@Param("t") String t);
}
