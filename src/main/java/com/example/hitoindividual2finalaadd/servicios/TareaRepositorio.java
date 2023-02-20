package com.example.hitoindividual2finalaadd.servicios;

import com.example.hitoindividual2finalaadd.jpa.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TareaRepositorio extends JpaRepository<Tarea, Integer> {
    @Query("select count(t) as pendientes from Tarea t where t.estado =1")
    public Integer pendientes();

    @Query("select count(t) as enproceso from Tarea t where t.estado =2")
    public Integer enproceso();

    @Query("select count(t) as finalizadas from Tarea t where t.estado =3")
    public Integer finalizadas();

    @Query("select count(t) as cuenta from Tarea t where t.estado =?1")
    public Integer findByEstado(Integer estado);

    @Modifying
    @Transactional
    @Query("delete from Tarea r where r.usuario.nif=:nif")

    void deleteByNif(@Param("nif") String nif);
    @Query("select t from Tarea t")
    List<Tarea> findAll();
    @Query("SELECT AVG(t.cuenta) FROM (SELECT COUNT(t) AS cuenta FROM Tarea t GROUP BY t.usuario) t")
    Double promedio();
}
