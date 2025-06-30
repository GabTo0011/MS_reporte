package com.perfulandia.reporte.service;

import com.perfulandia.reporte.model.Reporte;
import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    private ReporteDTO toDTO(Reporte reporte) {
        return new ReporteDTO(
                reporte.getId(),
                reporte.getTipoReporte(),
                reporte.getFechaGeneracion(),
                reporte.getDescripcion(),
                reporte.getJsonDatos()
        );
    }

    private Reporte toEntity(ReporteDTO dto) {
        Reporte reporte = new Reporte();
        reporte.setId(dto.getId());
        reporte.setTipoReporte(dto.getTipoReporte());
        reporte.setFechaGeneracion(dto.getFechaGeneracion());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setJsonDatos(dto.getJsonDatos());
        return reporte;
    }

    public ReporteDTO crear(ReporteDTO dto) {
        Reporte reporte = toEntity(dto);
        return toDTO(reporteRepository.save(reporte));
    }

    public List<ReporteDTO> listar() {
        return reporteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReporteDTO obtenerPorId(Integer id) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return toDTO(reporte);
    }

    public ReporteDTO actualizar(Integer id, ReporteDTO dto) {
        Reporte existente = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        existente.setTipoReporte(dto.getTipoReporte());
        existente.setFechaGeneracion(dto.getFechaGeneracion());
        existente.setDescripcion(dto.getDescripcion());
        existente.setJsonDatos(dto.getJsonDatos());

        return toDTO(reporteRepository.save(existente));
    }

    public void eliminar(Integer id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }
        reporteRepository.deleteById(id);
    }
}
