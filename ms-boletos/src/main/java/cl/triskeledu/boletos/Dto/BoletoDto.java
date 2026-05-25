package cl.triskeledu.boletos.controller;

import cl.triskeledu.boletos.dto.BoletoRequestDTO;
import cl.triskeledu.boletos.dto.BoletoResponseDTO;


import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;



@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    

    @PostMapping
    public ResponseEntity<BoletoResponseDTO> crearBoleto(@Valid @RequestBody BoletoRequestDTO request) {
       
        
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletoResponseDTO> obtenerBoleto(@PathVariable Integer id) {
        
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletoResponseDTO> actualizarBoleto(@PathVariable Integer id, @Valid @RequestBody BoletoRequestDTO request) {
        
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBoleto(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}