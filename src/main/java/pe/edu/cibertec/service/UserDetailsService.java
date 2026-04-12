//package pe.edu.cibertec.service;
//
//import java.util.Collections;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import pe.edu.cibertec.entity.Usuario;
//import pe.edu.cibertec.repository.UsuarioRepository;
//
//public class UserDetailsService {
//	private final UsuarioRepository usuarioRepository;
//	
//	@Override
//    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
//        Usuario usuario = usuarioRepository.findByCorreo(correo)
//            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
//
//        if (!usuario.getEstado()) {
//            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
//        }
//
//        // Convertir rol 
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name());
//
//        return User.builder()
//            .username(usuario.getCorreo())
//            .password(usuario.getContrasena())
//            .authorities(Collections.singletonList(authority))
//            .accountNonExpired(true)
//            .accountNonLocked(true)
//            .credentialsNonExpired(true)
//            .enabled(usuario.getEstado())
//            .build();
//    }
//}
