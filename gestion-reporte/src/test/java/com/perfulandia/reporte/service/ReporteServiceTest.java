package com.perfulandia.reporte.service;

import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.model.Reporte;
import com.perfulandia.reporte.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;
    private ReporteDTO dto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        LocalDate fecha = LocalDate.now();
        reporte = new Reporte(1, "ventas", fecha, "Reporte de ventas", "{\"total\": 1000}");
        dto = new ReporteDTO(1, "ventas", fecha, "Reporte de ventas", "{\"total\": 1000}");
    }

    @Test
    public void deberiaCrearReporteCorrectamente() {
        when(reporteRepository.save(any())).thenReturn(reporte);
        ReporteDTO resultado = reporteService.crear(dto);
        assertNotNull(resultado);
        assertEquals("ventas", resultado.getTipoReporte());
    }

    @Test
    public void deberiaListarReportes() {
        when(reporteRepository.findAll()).thenReturn(Arrays.asList(reporte));
        List<ReporteDTO> lista = reporteService.listar();
        assertEquals(1, lista.size());
    }

    @Test
    public void deberiaObtenerReportePorId() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));
        ReporteDTO encontrado = reporteService.obtenerPorId(1);
        assertNotNull(encontrado);
        assertEquals("ventas", encontrado.getTipoReporte());
    }

    @Test
    public void deberiaActualizarReporte() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any())).thenReturn(reporte);

        ReporteDTO actualizado = reporteService.actualizar(1, dto);
        assertEquals("ventas", actualizado.getTipoReporte());
    }

    @Test
    public void deberiaEliminarReporteSiExiste() {
        when(reporteRepository.existsById(1)).thenReturn(true);
        doNothing().when(reporteRepository).deleteById(1);

        assertDoesNotThrow(() -> reporteService.eliminar(1));
    }

    @Test
    public void deberiaLanzarExcepcionSiNoExisteReporteAlEliminar() {
        when(reporteRepository.existsById(2)).thenReturn(false);
        Exception e = assertThrows(RuntimeException.class, () -> reporteService.eliminar(2));
        assertEquals("Reporte no encontrado", e.getMessage());
    }
}
