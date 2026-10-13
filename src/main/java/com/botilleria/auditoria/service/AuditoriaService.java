package com.botilleria.auditoria.service;

import com.botilleria.auditoria.model.RegistroAuditoria;
import com.botilleria.auditoria.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AuditoriaService {

    @Autowired(required = false)
    private AuditoriaRepository auditoriaRepository;

    private final List<RegistroAuditoria> registrosMemoria = Collections.synchronizedList(new ArrayList<>());

    public AuditoriaService() {
        registrosMemoria.add(new RegistroAuditoria("SISTEMA", "INICIALIZACION", "Ecosistema de microservicios Pedidos360 conectado a RabbitMQ y Supabase", "INFO", Map.of("broker", "34.236.39.87:5672")));
        registrosMemoria.add(new RegistroAuditoria("MS-PRODUCTOS", "CATALOGO_DISPONIBLE", "Inventario de bodega cargado en Supabase", "INFO", Map.of("productos_activos", 5)));
    }

    public List<RegistroAuditoria> obtenerTodos() {
        if (auditoriaRepository != null) {
            try {
                List<RegistroAuditoria> lista = auditoriaRepository.findAll();
                if (!lista.isEmpty()) {
                    Collections.reverse(lista);
                    return lista;
                }
            } catch (Exception e) {
                System.err.println("Aviso conexion Supabase en auditoria: " + e.getMessage());
            }
        }
        List<RegistroAuditoria> copia = new ArrayList<>(registrosMemoria);
        Collections.reverse(copia);
        return copia;
    }

    public RegistroAuditoria registrar(RegistroAuditoria registro) {
        if (registro.getId() == null) {
            registro.setId(UUID.randomUUID().toString());
        }

        if (auditoriaRepository != null) {
            try {
                auditoriaRepository.save(registro);
                System.out.println("[AUDITORIA-SUPABASE] " + registro.getOrigen() + " -> " + registro.getAccion() + ": " + registro.getDescripcion());
                return registro;
            } catch (Exception e) {
                System.err.println("Aviso conexion Supabase en auditoria: " + e.getMessage());
            }
        }

        registrosMemoria.add(registro);
        System.out.println("[AUDITORIA-MEMORIA] " + registro.getOrigen() + " -> " + registro.getAccion() + ": " + registro.getDescripcion());
        return registro;
    }

    public RegistroAuditoria registrarPaso(String origen, String accion, String descripcion, String nivel, Map<String, Object> detalles) {
        RegistroAuditoria nuevo = new RegistroAuditoria(origen, accion, descripcion, nivel, detalles);
        return registrar(nuevo);
    }

    public void limpiar(Integer cantidad) {
        if (auditoriaRepository != null) {
            try {
                if (cantidad == null || cantidad <= 0) {
                    auditoriaRepository.deleteAll();
                } else {
                    List<RegistroAuditoria> todos = auditoriaRepository.findAll();
                    if (!todos.isEmpty()) {
                        int start = Math.max(0, todos.size() - cantidad);
                        List<RegistroAuditoria> aEliminar = new ArrayList<>(todos.subList(start, todos.size()));
                        auditoriaRepository.deleteAll(aEliminar);
                    }
                }
            } catch (Exception e) {
                System.err.println("Aviso al limpiar Supabase: " + e.getMessage());
            }
        }

        if (cantidad == null || cantidad <= 0) {
            registrosMemoria.clear();
        } else {
            int start = Math.max(0, registrosMemoria.size() - cantidad);
            while (registrosMemoria.size() > start) {
                registrosMemoria.remove(registrosMemoria.size() - 1);
            }
        }
    }
}
