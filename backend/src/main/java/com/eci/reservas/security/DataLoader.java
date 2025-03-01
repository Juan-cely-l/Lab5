package com.eci.reservas.security;

import com.eci.reservas.model.Usuario;
import com.eci.reservas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("admin123"), List.of("ADMIN"));
            Usuario user = new Usuario("usuario", passwordEncoder.encode("user123"), List.of("USER"));

            usuarioRepository.saveAll(List.of(admin, user));
            System.out.println("Usuarios de prueba creados!");
        }
    }
}
