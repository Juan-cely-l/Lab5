package com.eci.reservas.service;

import com.eci.reservas.model.Usuario;
import com.eci.reservas.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario guardarUsuario(Usuario usuario) {
        if (!usuario.getPassword().startsWith("$2a$")) { // Verificar si ya está encriptada
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        Usuario savedUser = usuarioRepository.save(usuario);
        logger.info("Usuario {} registrado con éxito", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Intentando autenticar usuario: {}", username);
        logger.info("Buscando usuario en la base de datos: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Usuario {} no encontrado", username);
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        logger.info("Usuario {} autenticado con éxito", usuario.getUsername());

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getAuthorities()
        );
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
