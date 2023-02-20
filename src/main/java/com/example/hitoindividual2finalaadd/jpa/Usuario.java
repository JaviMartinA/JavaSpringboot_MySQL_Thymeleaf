package com.example.hitoindividual2finalaadd.jpa;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;

@Entity
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String nif;

    private byte activo;

    private String apellidos;

    private String nombre;

    private String pw;

    @OneToMany(mappedBy="usuario")
    private List<Rol> roles;

    @OneToMany(mappedBy="usuario")
    private List<Tarea> tareas;

    public Usuario() {
        this.nif= "";
        this.nombre= "";
        this.apellidos= "";
        this.pw= "";
        this.activo= 1;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public byte getActivo() {
        return activo;
    }

    public void setActivo(byte activo) {
        this.activo = activo;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Rol addRole(Rol role) {
        getRoles().add(role);
        role.setUsuario(this);

        return role;
    }

    public Rol removeRole(Rol role) {
        getRoles().remove(role);
        role.setUsuario(null);

        return role;
    }

    public List<Tarea> getTareas() {
        return this.tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Tarea addTarea(Tarea tarea) {
        getTareas().add(tarea);
        tarea.setUsuario(this);

        return tarea;
    }

    public Tarea removeTarea(Tarea tarea) {
        getTareas().remove(tarea);
        tarea.setUsuario(null);

        return tarea;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nif='" + nif + '\'' +
                ", activo=" + activo +
                ", apellidos='" + apellidos + '\'' +
                ", nombre='" + nombre + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }

}