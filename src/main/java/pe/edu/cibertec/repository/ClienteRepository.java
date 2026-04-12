package pe.edu.cibertec.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    Optional<Cliente> findByCodigo(String codigo);
    
    Optional<Cliente> findByCorreo(String correo);


    Optional<Cliente> findByTelefono(String telefono);


    @Query("SELECT c FROM Cliente c WHERE c.estado = true ORDER BY c.nombre ASC")
    List<Cliente> findAllActivos();


    List<Cliente> findByCiudadIgnoreCase(String ciudad);

  
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.estado = true")
    long countActivos();
    
    @Query("SELECT c FROM Cliente c WHERE " +
    	       "LOWER(c.codigo) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(c.ciudad) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(c.direccion) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "LOWER(c.correo) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
    	       "CAST(c.telefono AS string) LIKE CONCAT('%', :t, '%') OR " +
    	       "(LOWER(:t) = 'activo' AND c.estado = true) OR " +
    	       "(LOWER(:t) = 'inactivo' AND c.estado = false)")
    	List<Cliente> buscarGlobal(@Param("t") String t);
}
