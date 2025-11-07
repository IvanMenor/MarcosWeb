package com.example.demo.controller;

import com.example.demo.model.Trabajador;
import com.example.demo.service.TrabajadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/trabajadores")
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Trabajador trabajador, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        trabajadorService.guardarTrabajador(trabajador);
        return "redirect:/admin/trabajadores";
    }

    @GetMapping("")
    public String listar(Model model, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        model.addAttribute("trabajadores", trabajadorService.listarTrabajadores());
        model.addAttribute("trabajador", new Trabajador());
        return "admin-trabajadores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        model.addAttribute("trabajador", trabajadorService.buscarPorId(id));
        model.addAttribute("trabajadores", trabajadorService.listarTrabajadores());
        return "admin-trabajadores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        trabajadorService.eliminarPorId(id);
        return "redirect:/admin/trabajadores";
    }
}
