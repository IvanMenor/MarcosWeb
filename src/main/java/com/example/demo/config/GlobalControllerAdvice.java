package com.example.demo.config;

import com.example.demo.model.Usuario;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Aporta un objeto 'usuario' vacío a todas las vistas para evitar errores de binding
    @ModelAttribute("usuario")
    public Usuario usuario() {
        return new Usuario();
    }
}
