package com.example.hitoindividual2finalaadd.control;




import com.example.hitoindividual2finalaadd.jpa.Rol;
import com.example.hitoindividual2finalaadd.jpa.Tarea;
import com.example.hitoindividual2finalaadd.jpa.Usuario;
import com.example.hitoindividual2finalaadd.servicios.RolService;
import com.example.hitoindividual2finalaadd.servicios.TareaService;
import com.example.hitoindividual2finalaadd.servicios.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UsuarioService usuarios;

    @Autowired
    RolService roles;

    @Autowired
    TareaService tareas;

    @RequestMapping("/")
    public ModelAndView peticionRaiz(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        mv.setViewName("index"); //También llamados endpoints
        return mv;
    }

    @RequestMapping("login")
    public ModelAndView peticionSesion(Authentication auth) {
        ModelAndView mv = new ModelAndView();
        if (auth == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", auth.getName());
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping("denegado") // URL: http://localhost:8081/login
    public ModelAndView peticionDenegado(Authentication aut) { // Nombre del método: peticionSesion
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("denegado"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user") // URL: http://localhost:8081/user
    public ModelAndView peticionUser(Authentication aut) { // Nombre del método: peticionUser
        ModelAndView mv = new ModelAndView(); // Objeto que contiene la vista y los datos
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        Optional<Usuario> userOptional = usuarios.buscarUsuario(aut.getName());
        Usuario user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }

        mv.setViewName("usuario"); // La rutra del archivo: src/main/resources/templates/user.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user/perfil")
    public ModelAndView peticionUPerfil(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> userOptional = usuarios.buscarUsuario(nif);

            mv.addObject("nif", userOptional.get().getNif());
            mv.addObject("nombre", userOptional.get().getNombre());
            mv.addObject("apellidos", userOptional.get().getApellidos());
            Usuario user = userOptional.get();
        }
        mv.setViewName("perfil");
        return mv;
    }
    @RequestMapping("/user/tareas/nueva")
    public ModelAndView peticionNuevaTarea(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
            Tarea tarea = new Tarea();
            mv.addObject("tarea", tarea);
        }
        mv.setViewName("nuevatarea");
        return mv;
    }
    @RequestMapping("/user/tareas/listado")
    public ModelAndView peticionListadoTareas(Authentication auth) {
        ModelAndView mv = new ModelAndView();
        if (auth == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", auth.getName());
            String nif = auth.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
        }
        mv.setViewName("listadotareas");
        return mv;
    }
    @RequestMapping("/admin")
    public ModelAndView peticionAdministrador(Authentication auth) {
        ModelAndView mv = new ModelAndView();
        if (auth == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", auth.getName());
        List<Usuario> listaUsuarios = usuarios.listaUsuarios();
        mv.addObject("listaUsuarios", listaUsuarios);
        mv.setViewName("administrador");
        return mv;
    }

    @RequestMapping("/admin/usuario/nuevo")
    public ModelAndView UsuariosNuevo(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        Usuario user = new Usuario();
        mv.addObject("usuario", user);
        mv.setViewName("nuevousuario");
        return mv;
    }
    @RequestMapping("/guardar")
    public ModelAndView guardarUsuario(Usuario usuario, Authentication aut) {
        ModelAndView mv = new ModelAndView();
        System.out.println(usuario);

        String sinCodificar = usuario.getPw();
        String codificado = encoder.encode(sinCodificar);
        usuario.setPw(codificado);

        if (aut == null) {
            mv.addObject("El nif " + usuario.getNif() + "ya existe");
        } else {
            usuarios.guardarUsuario(usuario);
            Rol role = new Rol();
            role.setUsuario(usuario);
            role.setRol("USUARIO");
            roles.guardarRol(role);
            mv.addObject("El usuario con el nif:" + usuario.getNif() + " y el nombre: " + usuario.getNombre() + "se ha guardado correctamente");
        }
        mv.setViewName("informa");
        return mv;
    }
    @RequestMapping("/admin/dashboard")
    public ModelAndView peticionDashboard(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        List<Usuario> listaUsuarios = usuarios.listaUsuarios();
        mv.addObject("listaUsuarios", listaUsuarios);
        List<Tarea> listaTareas = tareas.listaTareas();
        int numeroTareas = 0;
        for (Tarea tarea : listaTareas) {
            numeroTareas++;
        }
        mv.addObject("contadorTareas", numeroTareas);
        int tareasPendientes = tareas.cuentaTareas(2);
        mv.addObject("tareasPendientes", tareasPendientes);
        int tareasAnuladas = tareas.cuentaTareas(1);
        mv.addObject("tareasAnuladas", tareasAnuladas);
        int tareasCompletadas = tareas.tareasFinalizadas();
        mv.addObject("tareasCompletadas", tareasCompletadas);
        Double promedioTareas = tareas.promedioTareas();
        mv.addObject("promedioTareas", promedioTareas);

        mv.setViewName("dashboard");
        return mv;
    }
    @RequestMapping("/admin/usuario/mostar")
    public ModelAndView peticionUsuariosMostrar(Authentication auth) {
        ModelAndView mv = new ModelAndView();
        if (auth == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", auth.getName());
        mv.setViewName("mostrarusuarios");
        return mv;
    }
    @RequestMapping("/admin/usuario/editar")
    public ModelAndView peticioUsuariosEditar(Authentication aut, HttpServletRequest request) {
        String nif = request.getParameter("nif");
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        Usuario user = usuarioOpt.get();

        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.addObject("usuario", user);
        mv.setViewName("useredit");
        return mv;
    }
    @RequestMapping("/actualizar")
    public String peticionActualizar(Usuario u, Authentication aut) {

        usuarios.guardarUsuario(u);

        return "redirect:/admin";
    }
    @RequestMapping("/admin/usuario/borrarUsuario")
    public String peticionBorrarUsuario(HttpServletRequest request) {
        String nif = request.getParameter("nif");
        Usuario u = usuarios.buscarUsuario(nif).get();
        ModelAndView mv = new ModelAndView();
        if (u.getNif().equals("98765432A")) {
            return "redirect:/denegado";
        } else {
            if (!u.getRoles().isEmpty()) {
                List<Rol> lr = u.getRoles();
                lr.forEach(rol -> roles.eliminarRolById(rol.getId()));
            }
            if (!u.getTareas().isEmpty()) {
                List<Tarea> lt = u.getTareas();
                lt.forEach(tarea -> tareas.deletetareaByNif(tarea.getUsuarioNif()));
            }
            usuarios.eliminarUsuario(u.getNif());
            return "redirect:/admin";
        }
    }
    @RequestMapping("/guardar/tarea")
    public ModelAndView guardarTarea(Tarea tarea, Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            tarea.setUsuario(user);
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                String nifAut = aut.getName();
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                Usuario userAut = usuarioAutOpt.get();
                List<Rol> rol = userAut.getRoles();
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    mv.addObject("nif", nif);
                    mv.setViewName("redirect:/admin/usuario/tareas/listado");
                }
                else
                    mv.setViewName("redirect:/user/tareas/listado");

            } else {
                mv.addObject("sms", "No se ha actualizado la tarea correctamente");
                mv.setViewName("informa");
            }
        }
        return mv;
    }
    @RequestMapping("/user/tareas/editar")
    public ModelAndView peticionEditarTarea(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            mv.addObject("usuario", user);
            mv.addObject("tarea", tarea);
        }
        mv.setViewName("tareaedit");
        return mv;
    }
    @RequestMapping("/user/tareas/eliminar")
    public ModelAndView peticionEliminarTarea(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("u ser", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            tareas.borrar(tarea);
            mv.setViewName("redirect:/user/tareas/listado");
        }
        return mv;
    }
    @RequestMapping("/actualizar/tarea")
    public ModelAndView actualizarTarea(Tarea tarea, Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            tarea.setUsuario(user);
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                String nifAut = aut.getName();
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                Usuario userAut = usuarioAutOpt.get();
                List<Rol> rol = userAut.getRoles();
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    mv.addObject("nif", nif);
                    mv.setViewName("redirect:/admin/usuario/tareas/listado");
                }
                else
                    mv.setViewName("redirect:/user/tareas/listado");

            } else {
                mv.addObject("sms", "No se ha actualizado la tarea correctamente");
                mv.setViewName("informa");
            }
        }
        return mv;
    }
    @RequestMapping("/admin/usuario/tareas/listado")
    public ModelAndView peticionUsuariosTareasListado(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
        }
        mv.setViewName("alltareas");
        return mv;
    }
    @RequestMapping("/admin/usuario/tareas/nueva")
    public ModelAndView peticionUsuariosTareasNueva(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            Tarea t = new Tarea();
            t.setUsuario(user);
            mv.addObject("tarea", t);
            mv.addObject("usuario", user);
            mv.setViewName("nuevatarea");
        }
        return mv;
    }
    @RequestMapping("/admin/usuario/tareas/editar")
    public ModelAndView peticionUsuariosTareasEditar(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null){
            mv.addObject("user", "No se ha iniciado sesión");
        }
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            mv.addObject("usuario", user);
            mv.addObject("tarea", tarea);
            mv.setViewName("useredit");
        }
        return mv;
    }
}