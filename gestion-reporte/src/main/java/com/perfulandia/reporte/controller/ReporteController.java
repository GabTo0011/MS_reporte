package com.perfulandia.reporte.controller;

import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.service.ReporteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@Api(value = "API de Reportes", description = "Operaciones para gestionar reportes")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @ApiOperation(value = "Crear un nuevo reporte")
    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto) {
        ReporteDTO creado = service.crear(dto);
        return ResponseEntity.ok(creado);
    }

    @ApiOperation(value = "Listar todos los reportes")
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @ApiOperation(value = "Obtener un reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @ApiOperation(value = "Actualizar un reporte por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizar(@PathVariable Integer id, @RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @ApiOperation(value = "Eliminar un reporte por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // HATEOAS - Obtener reporte con enlaces relacionados
    @ApiOperation(value = "Obtener reporte con enlaces HATEOAS")
    @GetMapping("/hateoas/{id}")
    public ReporteDTO obtenerHATEOAS(@PathVariable Integer id) {
        ReporteDTO dto = service.obtenerPorId(id);

        // Enlaces URL de la misma API
        dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(ReporteController.class).listar()).withRel("todos"));
        dto.add(linkTo(methodOn(ReporteController.class).eliminar(id)).withRel("eliminar"));

        // Link HATEOAS para API Gateway "A mano"
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withSelfRel());
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    // HATEOAS - Listar todos los reportes con enlaces relacionados
    @ApiOperation(value = "Listar todos los reportes con enlaces HATEOAS")
    @GetMapping("/hateoas")
    public List<ReporteDTO> obtenerTodosHATEOAS() {
        List<ReporteDTO> lista = service.listar();

        for (ReporteDTO dto : lista) {
            // Enlace URL de la misma API
            dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(dto.getId())).withSelfRel());

            // Link HATEOAS para API Gateway "A mano"
            dto.add(Link.of("http://localhost:8888/api/proxy/reportes").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}
