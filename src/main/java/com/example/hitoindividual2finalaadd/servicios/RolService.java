package com.example.hitoindividual2finalaadd.servicios;

import com.example.hitoindividual2finalaadd.jpa.Rol;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class RolService {
    private RoleRepositorio roles;

    public RolService(RoleRepositorio roles) {
        this.roles = roles;
    }

    public void guardarRol(Rol rol) {
        roles.save(rol);
    }

    public void eliminarRolById(int id) {
        roles.deleteById(id);
    }

    public void eliminarRolByNif(String nif) {
        roles.deleteByNif(nif);
    }
}
