package com.perfulandia.reporte.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO extends RepresentationModel<ReporteDTO> {
    private Integer id;
    private String tipoReporte;
    private LocalDate fechaGeneracion;
    private String descripcion;
    private String jsonDatos;
}
