package com.perfulandia.reporte.controller;

import com.perfulandia.reporte.dto.ReporteDTO;
import com.perfulandia.reporte.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/reportes")  // Asegúrate de que la ruta esté bien configurada
public class ReporteController {

    @Autowired
    private ReporteService service;

    // Crear un nuevo reporte
    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto) {
        ReporteDTO creado = service.crear(dto);
        creado.add(linkTo(methodOn(ReporteController.class).obtener(creado.getId())).withSelfRel());
        return ResponseEntity.ok(creado);
    }

    // Listar todos los reportes
    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listar() {
        List<ReporteDTO> lista = service.listar();
        lista.forEach(reporte -> 
            reporte.add(linkTo(methodOn(ReporteController.class).obtener(reporte.getId())).withSelfRel())
        );
        return ResponseEntity.ok(lista);
    }

    // Obtener un reporte por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtener(@PathVariable Integer id) {
        ReporteDTO dto = service.obtenerPorId(id);
        dto.add(linkTo(methodOn(ReporteController.class).obtener(id)).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    // Actualizar un reporte por ID
    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizar(@PathVariable Integer id, @RequestBody ReporteDTO dto) {
        ReporteDTO actualizado = service.actualizar(id, dto);
        actualizado.add(linkTo(methodOn(ReporteController.class).obtener(id)).withSelfRel());
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar un reporte por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
