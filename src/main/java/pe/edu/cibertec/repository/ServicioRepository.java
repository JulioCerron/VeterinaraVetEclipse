package pe.edu.cibertec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.cibertec.entity.Cliente;
import pe.edu.cibertec.entity.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s FROM Servicio s WHERE s.estado = true ORDER BY s.nombre ASC")
    List<Servicio> findAllActivos();


    @Query("SELECT s FROM Servicio s WHERE s.precio BETWEEN :precioMin AND :precioMax AND s.estado = true")
    List<Servicio> findByRangoPrecio(Double precioMin, Double precioMax);

  
    @Query("SELECT COUNT(s) FROM Servicio s WHERE s.estado = true")
    long countActivos();
    
    
    
    @Query("SELECT s FROM Servicio s WHERE " +
            "LOWER(s.codigo) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "CAST(s.duracionEstimada AS string) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "CAST(s.precio AS string) LIKE LOWER(CONCAT('%', :t, '%')) OR " +
            "(LOWER(:t) = 'activo' AND s.estado = true) OR " +
            "(LOWER(:t) = 'inactivo' AND s.estado = false)")
     List<Servicio> buscarGlobal(@Param("t") String t);
}
