package com.perfulandia.reporte;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.reporte.controller.ReporteController;
import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReporteController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearReporte() throws Exception {
        ReporteDTO dto = new ReporteDTO();
        dto.setId(1);
        dto.setTipoReporte("Resumen");
        dto.setFechaGeneracion(LocalDate.of(2024, 1, 1));
        dto.setDescripcion("Reporte de prueba");
        dto.setJsonDatos("{\"clave\": \"valor\"}");

        when(reporteService.crear(Mockito.any(ReporteDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.descripcion").value("Reporte de prueba"))
            .andExpect(jsonPath("$.tipoReporte").value("Resumen"));
    }

    @Test
    void testListarReportes() throws Exception {
        ReporteDTO dto1 = new ReporteDTO(1, "Resumen", LocalDate.of(2024, 1, 1), "Desc1", "{\"a\":1}");
        ReporteDTO dto2 = new ReporteDTO(2, "Detalle", LocalDate.of(2024, 1, 2), "Desc2", "{\"b\":2}");

        when(reporteService.listar()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/reportes")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }
}
