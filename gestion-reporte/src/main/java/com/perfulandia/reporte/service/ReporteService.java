package com.perfulandia.reporte.service;

import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.model.Reporte;
import com.perfulandia.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    // Convertir entidad a DTO
    private ReporteDTO toDTO(Reporte reporte) {
        return new ReporteDTO(
                reporte.getId(),
                reporte.getTipoReporte(),
                reporte.getFechaGeneracion(),
                reporte.getDescripcion(),
                reporte.getJsonDatos()
        );
    }

    // Convertir DTO a entidad
    private Reporte toEntity(ReporteDTO dto) {
        return new Reporte(
                dto.getId(),
                dto.getTipoReporte(),
                dto.getFechaGeneracion(),
                dto.getDescripcion(),
                dto.getJsonDatos()
        );
    }

    // Crear un reporte
    public ReporteDTO crear(ReporteDTO dto) {
        Reporte reporte = toEntity(dto);
        return toDTO(reporteRepository.save(reporte));
    }

    // Listar todos los reportes
    public List<ReporteDTO> listar() {
        return reporteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener un reporte por su ID
    public ReporteDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        ReporteDTO dto = toDTO(reporte);

        // Agregar HATEOAS: Link para obtener el reporte
        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ReporteService.class).obtenerPorId(id)
        ).withSelfRel();
        dto.add(selfLink);

        // Agregar más enlaces HATEOAS
        dto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteService.class).listar()).withRel("reportes"));

        return dto;
    }

    // Actualizar un reporte
    public ReporteDTO actualizar(Integer id, ReporteDTO dto) {
        Reporte existente = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        existente.setTipoReporte(dto.getTipoReporte());
        existente.setFechaGeneracion(dto.getFechaGeneracion());
        existente.setDescripcion(dto.getDescripcion());
        existente.setJsonDatos(dto.getJsonDatos());

        return toDTO(reporteRepository.save(existente));
    }

    // Eliminar un reporte
    public void eliminar(Integer id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }
        reporteRepository.deleteById(id);
    }
}
