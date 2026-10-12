package com.botilleria.auditoria.controller;

import com.botilleria.auditoria.model.RegistroAuditoria;
import com.botilleria.auditoria.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<RegistroAuditoria>> listarRegistros() {
        return ResponseEntity.ok(auditoriaService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<RegistroAuditoria> crearRegistro(@RequestBody RegistroAuditoria registro) {
        RegistroAuditoria guardado = auditoriaService.registrar(registro);
        return ResponseEntity.ok(guardado);
    }
}
