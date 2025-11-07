package com.example.demo.service;

import com.example.demo.model.Trabajador;
import com.example.demo.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }

    public void guardarTrabajador(Trabajador trabajador) {
        trabajadorRepository.save(trabajador);
    }

    public Trabajador buscarPorId(Long id) {
        return trabajadorRepository.findById(id).orElse(null);
    }

    public void eliminarPorId(Long id) {
        trabajadorRepository.deleteById(id);
    }
}
