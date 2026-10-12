package com.botilleria.auditoria.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "auditoria")
public class RegistroAuditoria {

    @Id
    private String id;

    private String timestamp;
    private String origen;
    private String accion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String nivel;

    @Transient
    private Map<String, Object> detalles;

    public RegistroAuditoria() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public RegistroAuditoria(String origen, String accion, String descripcion, String nivel, Map<String, Object> detalles) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.origen = origen;
        this.accion = accion;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.detalles = detalles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public Map<String, Object> getDetalles() { return detalles; }
    public void setDetalles(Map<String, Object> detalles) { this.detalles = detalles; }
}
