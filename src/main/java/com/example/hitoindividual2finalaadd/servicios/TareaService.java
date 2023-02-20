package com.example.hitoindividual2finalaadd.servicios;


import com.example.hitoindividual2finalaadd.jpa.Tarea;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Service
@ApplicationScope
public class TareaService {

    private TareaRepositorio tareas;

    public TareaService(TareaRepositorio tareas) {
        this.tareas = tareas;
    }

    public List<Tarea> listaTareas() {
        return tareas.findAll();
    }

    public Integer tareasFinalizadas() {
        return tareas.finalizadas();
    }

    public Integer cuentaTareas(Integer estado) {
        return tareas.findByEstado(estado);
    }

    public void deletetareaByNif(String nif) {
        tareas.deleteByNif(nif);
    }
    //guardar tarea
    public void guardar(Tarea t){tareas.save(t); }

    public Optional<Tarea> buscarTarea(int id){
        return tareas.findById(id);
    }

    public void borrar(Tarea t){
        tareas.delete(t);
    }

    public Double promedioTareas() {
        return tareas.promedio();
    }
}