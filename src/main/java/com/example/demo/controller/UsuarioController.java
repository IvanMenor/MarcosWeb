package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.service.ProductoService;
import com.example.demo.service.TrabajadorService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
private UsuarioService usuarioService;

@Autowired
private ProductoService productoService;

@Autowired
private TrabajadorService trabajadorService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "index"; // muestra templates/index.html
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute Usuario usuario) {
        // asegurar que el rol por defecto sea USER si no viene
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        usuarioService.guardarUsuario(usuario);
        return "redirect:/"; // redirige al inicio tras guardar
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String password, Model model, HttpSession session) {
        Usuario u = usuarioService.buscarPorCorreo(correo);
        if (u == null) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("loginError", "Usuario no encontrado");
            return "index";
        }
        if (!u.getPassword().equals(password)) {
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("loginError", "Credenciales inválidas");
            return "index";
        }
        // Guardamos usuario en sesión para control de acceso simple
        session.setAttribute("usuarioLogeado", u);
        // login exitoso
        if ("ADMIN".equalsIgnoreCase(u.getRol())) {
            return "redirect:/admin/usuarios";
        }
        return "redirect:/"; // usuario normal
    }

    // ---------- Rutas para administrador (CRUD básico) ----------
    @GetMapping("/admin")
    public String adminRoot() {
        // redirigimos al panel de usuarios por defecto
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios")
    public String adminUsuarios(Model model, HttpSession session) {
        Usuario ses = (Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/"; // no autorizado
        }

        List<Usuario> usuarios = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);

        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new Usuario());
        }
        return "admin-usuarios";
    }


    @PostMapping("/admin/guardar")
    public String guardarAdmin(@ModelAttribute Usuario usuario, HttpSession session) {
        Usuario ses = (Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/"; // no autorizado
        }

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("USER");
        }
        usuarioService.guardarUsuario(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        Usuario ses = (Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/"; // no autorizado
        }

        Usuario u = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", u == null ? new Usuario() : u);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "admin-usuarios";
    }

    @GetMapping("/admin/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        Usuario ses = (Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/"; // no autorizado
        }

        usuarioService.eliminarPorId(id);
        return "redirect:/admin/usuarios";
    }
}
