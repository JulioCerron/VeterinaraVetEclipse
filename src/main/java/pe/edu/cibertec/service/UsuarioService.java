package pe.edu.cibertec.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.entity.Usuario;
import pe.edu.cibertec.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerActivos() {
        return usuarioRepository.findAllActivos();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<Usuario> obtenerPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Optional<Usuario> autenticar(String correo, String contrasena) {
        return usuarioRepository.findByCorreo(correo)
            .filter(usuario -> usuario.getEstado())
            .filter(usuario -> passwordEncoder.matches(contrasena, usuario.getContrasena())); 
    }

    public Usuario crear(Usuario usuario) {
        // Validaciones
        if (usuario.getCorreo() == null) {
            throw new IllegalArgumentException("El correo es requerido");
        }
        if (usuario.getContrasena() == null) {
            throw new IllegalArgumentException("La contraseña es requerida");
       }
        if (usuario.getNombre() == null) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (usuario.getApellido() == null) {
          throw new IllegalArgumentException("El apellido es requerido");
        }

        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        
        if (usuario.getCodigo() == null) {
            usuario.setCodigo("U-" + System.currentTimeMillis()); 
        }

       if (usuario.getRol() == null) {
            usuario.setRol(Usuario.Rol.USUARIO);
       }

       return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (usuarioActualizado.getNombre() != null) {
                usuario.setNombre(usuarioActualizado.getNombre());
           }
            if (usuarioActualizado.getApellido() != null) {
                usuario.setApellido(usuarioActualizado.getApellido());
            }
            if (usuarioActualizado.getCorreo() != null) {
               usuario.setCorreo(usuarioActualizado.getCorreo());
            }
           if (usuarioActualizado.getRol() != null) {
               usuario.setRol(usuarioActualizado.getRol());
            }
            if (usuarioActualizado.getEstado() != null) {
               usuario.setEstado(usuarioActualizado.getEstado());
            }
            return usuarioRepository.save(usuario);
       }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

  public void eliminar(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
           usuario.setEstado(false);
           usuarioRepository.save(usuario);
      });
    }


    public void eliminarPermanente(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        if (!usuario.getEstado()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());

        return User.builder()
        		.username(usuario.getCorreo())
        	    .password(usuario.getContrasena())
        	    .authorities(Collections.singletonList(authority))
        	    .build();
    }
    
}
