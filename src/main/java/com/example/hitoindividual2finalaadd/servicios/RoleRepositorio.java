package com.example.hitoindividual2finalaadd.servicios;

import com.example.hitoindividual2finalaadd.jpa.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepositorio extends JpaRepository<Rol, Integer> {

    @Modifying
    @Transactional
    @Query("delete from Rol r where r.usuario.nif=:nif")

    void deleteByNif(@Param("nif") String nif);

}
