package pe.edu.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.entity.Usuario;

import java.util.List;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByRol(Usuario.Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.estado = true")
    List<Usuario> findAllActivos();

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.estado = true")
    List<Usuario> findActivosByRol(@Param("rol") Usuario.Rol rol);

    boolean existsByCorreo(String correo);
	
}
