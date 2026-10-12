package com.botilleria.auditoria.service;

import com.botilleria.auditoria.model.RegistroAuditoria;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuditoriaService {
    private final List<RegistroAuditoria> registros = Collections.synchronizedList(new ArrayList<>());

    public AuditoriaService() {
        // Semillas iniciales para mostrar el flujo en el frontend
        registros.add(new RegistroAuditoria("SISTEMA", "INICIALIZACION", "Ecosistema de microservicios Pedidos360 iniciado y conectado a RabbitMQ", "INFO", Map.of("broker", "34.236.39.87:5672")));
        registros.add(new RegistroAuditoria("MS-PRODUCTOS", "CATALOGO_DISPONIBLE", "Inventario de bodega cargado en Eureka", "INFO", Map.of("productos_activos", 5)));
    }

    public List<RegistroAuditoria> obtenerTodos() {
        // Retornar lista ordenada del mas reciente al mas antiguo
        List<RegistroAuditoria> copia = new ArrayList<>(registros);
        Collections.reverse(copia);
        return copia;
    }

    public RegistroAuditoria registrar(RegistroAuditoria registro) {
        if (registro.getId() == null) {
            registro.setId(UUID.randomUUID().toString());
        }
        registros.add(registro);
        System.out.println("[AUDITORIA] " + registro.getOrigen() + " -> " + registro.getAccion() + ": " + registro.getDescripcion());
        return registro;
    }

    public RegistroAuditoria registrarPaso(String origen, String accion, String descripcion, String nivel, Map<String, Object> detalles) {
        RegistroAuditoria nuevo = new RegistroAuditoria(origen, accion, descripcion, nivel, detalles);
        return registrar(nuevo);
    }
}
