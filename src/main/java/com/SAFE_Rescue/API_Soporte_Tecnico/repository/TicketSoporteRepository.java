package com.SAFE_Rescue.API_Soporte_Tecnico.repository; // Asegúrate que el paquete sea el correcto para este microservicio

import com.SAFE_Rescue.API_Soporte_Tecnico.modelo.TicketSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Integer> {

    // Métodos de búsqueda personalizados (derived queries)

    // Buscar tickets por el ID del emisor
    List<TicketSoporte> findByEmisorTicketId(Integer emisorTicketId);

    // Buscar tickets por el ID del técnico asignado (puede ser null)
    List<TicketSoporte> findByTecnicoAsignadoId(Integer tecnicoAsignadoId);

    // Buscar tickets por estado
    List<TicketSoporte> findByEstado(String estado);

    // Buscar tickets por categoría (case-insensitive para flexibilidad)
    List<TicketSoporte> findByCategoriaContainingIgnoreCase(String categoria);

    // Buscar tickets por prioridad
    List<TicketSoporte> findByPrioridad(String prioridad);

    // Buscar tickets creados después de una fecha/hora específica
    List<TicketSoporte> findByFechaHoraCreacionTicketAfter(Date fecha);

    // Buscar tickets resueltos después de una fecha/hora específica
    List<TicketSoporte> findByFechaHoraResolucionAfter(Date fecha);

    // Buscar tickets por asunto que contenga una palabra clave (case-insensitive)
    List<TicketSoporte> findByAsuntoContainingIgnoreCase(String asunto);

    // Buscar tickets por estado Y categoría
    List<TicketSoporte> findByEstadoAndCategoria(String estado, String categoria);

    // Buscar tickets por emisor y estado
    List<TicketSoporte> findByEmisorTicketIdAndEstado(Integer emisorTicketId, String estado);

    // Puedes añadir más métodos según las necesidades de búsqueda que surjan.
    // Por ejemplo:
    // List<TicketSoporte> findByPrioridadOrderByFechaHoraCreacionTicketDesc(String prioridad);
    // List<TicketSoporte> findByEstadoNot(String estado);
}