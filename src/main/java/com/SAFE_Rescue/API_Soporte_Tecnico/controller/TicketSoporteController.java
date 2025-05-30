package com.SAFE_Rescue.API_Soporte_Tecnico.controller;

import com.SAFE_Rescue.API_Soporte_Tecnico.modelo.TicketSoporte;
import com.SAFE_Rescue.API_Soporte_Tecnico.service.TicketSoporteService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// Importaciones estáticas de las clases DTO anidadas del controlador
import static com.SAFE_Rescue.API_Soporte_Tecnico.controller.TicketSoporteController.CrearTicketSoporteRequest;
import static com.SAFE_Rescue.API_Soporte_Tecnico.controller.TicketSoporteController.ActualizarTicketSoporteRequest;

@RestController
@RequestMapping("api-soporte-tecnico/v1/tickets-soporte")
public class TicketSoporteController {

    private final TicketSoporteService ticketSoporteService;

    @Autowired
    public TicketSoporteController(TicketSoporteService ticketSoporteService) {
        this.ticketSoporteService = ticketSoporteService;
    }

    // ---------------------------------------------------
    // Clases DTO internas y ESTATICAS para Peticiones (REQUESTS)
    // ---------------------------------------------------
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CrearTicketSoporteRequest {
        private Integer emisorTicketId;
        private Integer tecnicoAsignadoId;
        private String asunto;
        private String descripcion;
        private String categoria;
        private String prioridad;
        private String estado;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActualizarTicketSoporteRequest {
        private Integer tecnicoAsignadoId;
        private String asunto;
        private String descripcion;
        private String categoria;
        private String prioridad;
        private String estado;
        private Date fechaHoraResolucion;
        private String solucionDetalle;
    }

    // ---------------------------------------------------
    // Endpoints RESTful (CRUD) - ¡Con try-catch en el controlador para respuestas personalizadas!
    // ---------------------------------------------------

    @PostMapping
    public ResponseEntity<?> crearTicket(@RequestBody CrearTicketSoporteRequest request) {
        try {
            TicketSoporte nuevoTicket = ticketSoporteService.crearTicketDesdeRequest(request);
            return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Captura RuntimeException para errores de validación o lógica del servicio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al crear el ticket.");
        }
    }

    @GetMapping
    public ResponseEntity<List<TicketSoporte>> obtenerTodosLosTickets() {
        List<TicketSoporte> tickets = ticketSoporteService.obtenerTodosLosTickets();
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTicketPorId(@PathVariable Integer id) {
        try {
            // Ahora, el servicio lanza una RuntimeException si no encuentra el ticket
            TicketSoporte ticket = ticketSoporteService.obtenerTicketPorId(id)
                    .orElseThrow(() -> new RuntimeException("Ticket de Soporte con ID " + id + " no encontrado."));
            return new ResponseEntity<>(ticket, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Captura la RuntimeException específica de "no encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al obtener el ticket.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTicket(@PathVariable Integer id, @RequestBody ActualizarTicketSoporteRequest request) {
        try {
            // Ahora, el servicio lanza una RuntimeException si no encuentra el ticket
            TicketSoporte ticketActualizado = ticketSoporteService.actualizarTicketDesdeRequest(id, request)
                    .orElseThrow(() -> new RuntimeException("Ticket de Soporte con ID " + id + " no encontrado para actualizar."));
            return new ResponseEntity<>(ticketActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Captura RuntimeException para "no encontrado" o errores de validación/lógica
            // Puedes diferenciar si el mensaje contiene "no encontrado" para HttpStatus.NOT_FOUND
            // o si es otro tipo de RuntimeException para HttpStatus.BAD_REQUEST
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al actualizar el ticket.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTicket(@PathVariable Integer id) {
        try {
            ticketSoporteService.eliminarTicket(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content para éxito
        } catch (RuntimeException e) {
            // Captura la RuntimeException lanzada por el servicio
            // y devuelve 404 NOT_FOUND con el mensaje descriptivo
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al eliminar el ticket.");
        }
    }

    // ---------------------------------------------------
    // Endpoints de búsqueda personalizados (sin cambios aquí)
    // ---------------------------------------------------

    @GetMapping("/emisor/{emisorId}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorEmisor(@PathVariable Integer emisorId) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorEmisor(emisorId);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorTecnicoAsignado(@PathVariable Integer tecnicoId) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorTecnicoAsignado(tecnicoId);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorEstado(@PathVariable String estado) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorEstado(estado);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorCategoria(@PathVariable String categoria) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorCategoria(categoria);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorPrioridad(@PathVariable String prioridad) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorPrioridad(prioridad);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/asunto-keyword/{keyword}")
    public ResponseEntity<List<TicketSoporte>> buscarTicketsPorAsuntoConteniendo(@PathVariable String keyword) {
        List<TicketSoporte> tickets = ticketSoporteService.buscarTicketsPorAsuntoConteniendo(keyword);
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}