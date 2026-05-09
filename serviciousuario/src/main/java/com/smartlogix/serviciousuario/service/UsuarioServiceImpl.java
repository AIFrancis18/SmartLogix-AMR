package com.smartlogix.serviciousuario.service;

import com.smartlogix.serviciousuario.model.Usuario;
import com.smartlogix.serviciousuario.repository.UsuarioRepository;
import com.smartlogix.serviciousuario.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioServiceImpl(UsuarioRepository repository,
                              BCryptPasswordEncoder passwordEncoder,
                              JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 🔥 REGEX CORREO
    private final Pattern correoRegex =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final List<String> dominiosPermitidos =
            List.of("gmail.com", "hotmail.com", "duoc.cl");

    @Override
    public Usuario guardar(Usuario usuario) {

        // 🔥 VALIDAR CORREO
        if (usuario.getCorreo() == null || !correoRegex.matcher(usuario.getCorreo()).matches()) {
            throw new RuntimeException("Formato de correo inválido");
        }

        String dominio = usuario.getCorreo().split("@")[1];

        if (!dominiosPermitidos.contains(dominio)) {
            throw new RuntimeException("Solo se permiten correos Gmail, Hotmail o Duoc");
        }

        // 🔥 VALIDAR ROL
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            throw new RuntimeException("El rol es obligatorio");
        }

        if (usuario.getId() != null && repository.existsById(usuario.getId())) {

            // 🔥 UPDATE
            Usuario existente = repository.findById(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 🔥 SI VIENE CONTRASEÑA → VALIDAR Y CAMBIAR
            if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {

                if (usuario.getContrasena().length() < 4 || usuario.getContrasena().length() > 12) {
                    throw new RuntimeException("Contraseña entre 4 y 12 caracteres");
                }

                existente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }

            // 🔥 ACTUALIZAR DATOS
            existente.setNombre(usuario.getNombre());
            existente.setCorreo(usuario.getCorreo());
            existente.setRol(usuario.getRol());

            return repository.save(existente);

        } else {

            // 🔥 CREATE

            if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
                throw new RuntimeException("La contraseña es obligatoria");
            }

            if (usuario.getContrasena().length() < 4) {
                throw new RuntimeException("La contraseña debe tener mínimo 4 caracteres");
            }

            if (usuario.getContrasena().length() > 12) {
                throw new RuntimeException("La contraseña no puede superar 12 caracteres");
            }

            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

            return repository.save(usuario);
        }
    }

    @Override
    public List<Usuario> listar() {
        return repository.findAll();
    }

    // 🔥 ELIMINAR
    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        repository.deleteById(id);
    }

    @Override
    public String login(String correo, String contrasena) {

        // 🔥 VALIDAR CORREO
        if (correo == null || !correoRegex.matcher(correo).matches()) {
            throw new RuntimeException("Formato de correo inválido");
        }

        String dominio = correo.split("@")[1];

        if (!dominiosPermitidos.contains(dominio)) {
            throw new RuntimeException("Correo no permitido");
        }

        // 🔥 VALIDAR CONTRASEÑA
        if (contrasena == null || contrasena.length() < 4 || contrasena.length() > 12) {
            throw new RuntimeException("Contraseña inválida");
        }

        Usuario usuario = repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 🔥 TOKEN CON ROL
        return jwtUtil.generarToken(usuario.getCorreo(), usuario.getRol());
    }
}