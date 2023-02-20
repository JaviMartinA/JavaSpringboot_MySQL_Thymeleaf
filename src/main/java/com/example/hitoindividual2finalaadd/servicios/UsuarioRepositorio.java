package com.example.hitoindividual2finalaadd.servicios;

import com.example.hitoindividual2finalaadd.jpa.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    List<Usuario> findByNombre(String nombre);
}
