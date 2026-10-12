# MS-Auditoria

Microservicio encargado de la auditoria de acciones (agregar/eliminar) y la trazabilidad de los pasos de los microservicios vinculados con RabbitMQ.

## Caracteristicas
- **Trazabilidad en tiempo real:** Escucha eventos de RabbitMQ (otilleria.exchange) para documentar el ciclo de vida completo de una orden:
  1. Creacion de la orden (ms-ordenes).
  2. Notificacion de correo simulado (ms-notificaciones).
  3. Generacion de ticket/guia de despacho (ms-despachos).
  4. Descuento de stock en catalogo (ms-productos).
- **Auditoria de acciones:** Endpoint POST /api/auditoria para registrar eventos de interfaz (agregar al carrito, limpiar carrito, etc.).
- **Consulta de logs:** Endpoint GET /api/auditoria para alimentar el panel de trazabilidad en el Frontend.
- Se registra en Netflix Eureka.
- Protegido via Microsoft Entra ID (Azure AD).

## Instrucciones de Uso
- Puerto por defecto: 8087
- Ejecutar con mvn spring-boot:run o a traves de docker-compose up.
