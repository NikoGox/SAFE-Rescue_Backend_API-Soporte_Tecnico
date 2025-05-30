package com.SAFE_Rescue.API_Soporte_Tecnico.service;

import com.SAFE_Rescue.API_Soporte_Tecnico.modelo.TicketSoporte;
import com.SAFE_Rescue.API_Soporte_Tecnico.repository.TicketSoporteRepository;
import com.SAFE_Rescue.API_Soporte_Tecnico.controller.TicketSoporteController.CrearTicketSoporteRequest;
import com.SAFE_Rescue.API_Soporte_Tecnico.controller.TicketSoporteController.ActualizarTicketSoporteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketSoporteService {

    private final TicketSoporteRepository ticketSoporteRepository;

    @Autowired
    public TicketSoporteService(TicketSoporteRepository ticketSoporteRepository) {
        this.ticketSoporteRepository = ticketSoporteRepository;
    }

    // CREATE
    public TicketSoporte crearTicket(TicketSoporte ticketSoporte) {
        if (ticketSoporte.getFechaHoraCreacionTicket() == null) {
            ticketSoporte.setFechaHoraCreacionTicket(new Date());
        }
        validarTicketSoporte(ticketSoporte); // Asegúrate de que esta validación lance RuntimeException
        return ticketSoporteRepository.save(ticketSoporte);
    }

    public TicketSoporte crearTicketDesdeRequest(CrearTicketSoporteRequest request) {
        TicketSoporte ticketSoporte = new TicketSoporte();
        ticketSoporte.setEmisorTicketId(request.getEmisorTicketId());
        ticketSoporte.setTecnicoAsignadoId(request.getTecnicoAsignadoId());
        ticketSoporte.setAsunto(request.getAsunto());
        ticketSoporte.setDescripcion(request.getDescripcion());
        ticketSoporte.setCategoria(request.getCategoria());
        ticketSoporte.setPrioridad(request.getPrioridad());
        ticketSoporte.setEstado(request.getEstado());
        ticketSoporte.setFechaHoraCreacionTicket(new Date()); // Asignar fecha de creación

        validarTicketSoporte(ticketSoporte); // Asegúrate de que esta validación lance RuntimeException
        return ticketSoporteRepository.save(ticketSoporte);
    }

    // READ (obtener todos y por ID)
    public List<TicketSoporte> obtenerTodosLosTickets() {
        return ticketSoporteRepository.findAll();
    }

    public Optional<TicketSoporte> obtenerTicketPorId(Integer id) {
        return ticketSoporteRepository.findById(id);
        // Si quisieras que el servicio lanzara la excepción para que el controlador la capture:
        // return ticketSoporteRepository.findById(id)
        //         .orElseThrow(() -> new RuntimeException("Ticket de Soporte con ID " + id + " no encontrado."));
    }

    // UPDATE
    public Optional<TicketSoporte> actualizarTicket(Integer id, TicketSoporte ticketSoporteActualizado) {
        return ticketSoporteRepository.findById(id)
                .map(ticket -> {
                    ticket.setTecnicoAsignadoId(ticketSoporteActualizado.getTecnicoAsignadoId());
                    ticket.setAsunto(ticketSoporteActualizado.getAsunto());
                    ticket.setDescripcion(ticketSoporteActualizado.getDescripcion());
                    ticket.setCategoria(ticketSoporteActualizado.getCategoria());
                    ticket.setPrioridad(ticketSoporteActualizado.getPrioridad());
                    ticket.setEstado(ticketSoporteActualizado.getEstado());
                    ticket.setFechaHoraResolucion(ticketSoporteActualizado.getFechaHoraResolucion());
                    ticket.setSolucionDetalle(ticketSoporteActualizado.getSolucionDetalle());
                    validarTicketSoporte(ticket); // Asegúrate de que esta validación lance RuntimeException
                    return ticketSoporteRepository.save(ticket);
                });
    }

    public Optional<TicketSoporte> actualizarTicketDesdeRequest(Integer id, ActualizarTicketSoporteRequest request) {
        return ticketSoporteRepository.findById(id)
                .map(ticket -> {
                    // Actualiza solo los campos que vienen en el request
                    if (request.getTecnicoAsignadoId() != null) {
                        ticket.setTecnicoAsignadoId(request.getTecnicoAsignadoId());
                    }
                    if (request.getAsunto() != null) {
                        ticket.setAsunto(request.getAsunto());
                    }
                    if (request.getDescripcion() != null) {
                        ticket.setDescripcion(request.getDescripcion());
                    }
                    if (request.getCategoria() != null) {
                        ticket.setCategoria(request.getCategoria());
                    }
                    if (request.getPrioridad() != null) {
                        ticket.setPrioridad(request.getPrioridad());
                    }
                    if (request.getEstado() != null) {
                        ticket.setEstado(request.getEstado());
                    }
                    if (request.getFechaHoraResolucion() != null) {
                        ticket.setFechaHoraResolucion(request.getFechaHoraResolucion());
                    }
                    if (request.getSolucionDetalle() != null) {
                        ticket.setSolucionDetalle(request.getSolucionDetalle());
                    }

                    validarTicketSoporte(ticket); // Asegúrate de que esta validación lance RuntimeException
                    return ticketSoporteRepository.save(ticket);
                });
    }

    // DELETE
    public void eliminarTicket(Integer id) {
        if (!ticketSoporteRepository.existsById(id)) {
            // Esta es la RuntimeException que queremos que el controlador capture
            throw new RuntimeException("Ticket de Soporte con ID " + id + " no encontrado para eliminar.");
        }
        ticketSoporteRepository.deleteById(id);
    }

    // Métodos de búsqueda personalizados
    public List<TicketSoporte> buscarTicketsPorEmisor(Integer emisorId) {
        return ticketSoporteRepository.findByEmisorTicketId(emisorId);
    }

    public List<TicketSoporte> buscarTicketsPorTecnicoAsignado(Integer tecnicoId) {
        return ticketSoporteRepository.findByTecnicoAsignadoId(tecnicoId);
    }

    public List<TicketSoporte> buscarTicketsPorEstado(String estado) {
        return ticketSoporteRepository.findByEstado(estado);
    }

    public List<TicketSoporte> buscarTicketsPorCategoria(String categoria) {
        return ticketSoporteRepository.findByCategoriaContainingIgnoreCase(categoria);
    }

    public List<TicketSoporte> buscarTicketsPorPrioridad(String prioridad) {
        return ticketSoporteRepository.findByPrioridad(prioridad);
    }

    public List<TicketSoporte> buscarTicketsPorAsuntoConteniendo(String keyword) {
        return ticketSoporteRepository.findByAsuntoContainingIgnoreCase(keyword);
    }

    // Método de validación (asegúrate de que lanza RuntimeException para errores de validación)
    private void validarTicketSoporte(TicketSoporte ticketSoporte) {
        if (ticketSoporte.getAsunto() == null || ticketSoporte.getAsunto().trim().isEmpty()) {
            throw new RuntimeException("El asunto del ticket no puede estar vacío.");
        }
        if (ticketSoporte.getDescripcion() == null || ticketSoporte.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción del ticket no puede estar vacía.");
        }
        // Puedes añadir más validaciones aquí y lanzar RuntimeException con mensajes descriptivos.
        // Ejemplo:
        if (ticketSoporte.getAsunto().length() > 255) {
            throw new RuntimeException("El asunto es demasiado largo.");
        }
    }
}