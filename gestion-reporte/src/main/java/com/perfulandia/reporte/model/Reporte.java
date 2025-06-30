package com.perfulandia.reporte.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer id;

    @Column(name = "tipo_reporte", length = 100)
    private String tipoReporte;

    @Column(name = "fecha_generacion")
    private LocalDate fechaGeneracion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "json_datos", columnDefinition = "TEXT")
    private String jsonDatos;
}
