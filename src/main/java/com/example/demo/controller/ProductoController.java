package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        productoService.guardarProducto(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("")
    public String listar(Model model, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("producto", new Producto());
        return "admin-productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        model.addAttribute("producto", productoService.buscarPorId(id));
        model.addAttribute("productos", productoService.listarProductos());
        return "admin-productos"; // nueva vista para productos
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        com.example.demo.model.Usuario ses = (com.example.demo.model.Usuario) session.getAttribute("usuarioLogeado");
        if (ses == null || !"ADMIN".equalsIgnoreCase(ses.getRol())) {
            return "redirect:/";
        }
        productoService.eliminarPorId(id);
        return "redirect:/admin/productos";
    }
}
