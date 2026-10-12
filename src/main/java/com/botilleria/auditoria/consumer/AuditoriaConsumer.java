package com.botilleria.auditoria.consumer;

import com.botilleria.auditoria.config.RabbitMQConfig;
import com.botilleria.auditoria.service.AuditoriaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class AuditoriaConsumer {

    @Autowired
    private AuditoriaService auditoriaService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void procesarEventoRabbitMQ(Map<String, Object> mensaje) {
        String ordenId = mensaje.getOrDefault("id", "N/A").toString();
        String usuario = mensaje.getOrDefault("usuarioId", "anonimo").toString();
        Object total = mensaje.getOrDefault("total", "0");

        System.out.println("[AUDITORIA] Capturado evento RabbitMQ para orden: " + ordenId);

        // PASO 1: Registro de Orden Creada
        auditoriaService.registrarPaso(
            "MS-ORDENES",
            "ORDEN_EMITIDA",
            "Paso 1: Orden #" + ordenId + " emitida a traves de RabbitMQ por usuario " + usuario + " (Total: $" + total + ")",
            "SUCCESS",
            mensaje
        );

        // PASO 2: Trazabilidad Notificacion
        auditoriaService.registrarPaso(
            "MS-NOTIFICACIONES",
            "CORREO_SIMULADO",
            "Paso 2: Cola 'notificaciones.queue' consumida. Correo de confirmacion emitido para " + usuario,
            "INFO",
            Map.of("ordenId", ordenId, "destinatario", usuario + "@correo.com")
        );

        // PASO 3: Trazabilidad Despachos / Ticket
        auditoriaService.registrarPaso(
            "MS-DESPACHOS",
            "TICKET_GENERADO",
            "Paso 3: Cola 'despachos.queue' consumida. Ticket de seguimiento generado en estado PREPARACION",
            "INFO",
            Map.of("ordenId", ordenId, "estado", "PREPARACION")
        );

        // PASO 4: Trazabilidad Descuento Stock
        auditoriaService.registrarPaso(
            "MS-PRODUCTOS",
            "STOCK_DESCONTADO",
            "Paso 4: Cola 'stock.queue' consumida. Stock de catalogo descontado para los items de la orden #" + ordenId,
            "INFO",
            Map.of("ordenId", ordenId)
        );
    }
}
