package com.dh.DentalClinicMVC.controller;

import com.dh.DentalClinicMVC.entity.Dentist;
import com.dh.DentalClinicMVC.exception.ResourceNotFoundException;
import com.dh.DentalClinicMVC.service.IDentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dentists")
public class DentistController {

    public IDentistService dentistService;

    @Autowired
    public DentistController(IDentistService dentistService) {
        this.dentistService = dentistService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dentist> findById(@PathVariable Long id) {
        Optional<Dentist> dentist = dentistService.findById(id);
        if ( dentist.isPresent() ) {
            return ResponseEntity.ok(dentist.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Dentist> save(@RequestBody Dentist dentist) {
        return ResponseEntity.ok(dentistService.save(dentist));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Dentist dentist) {
        ResponseEntity<String> response;
        Optional<Dentist> dentistToLookFor = dentistService.findById(dentist.getId());
        if ( dentistToLookFor.isPresent() ) {
            dentistService.update(dentist);
            response = ResponseEntity.ok("Odontólogo actualizado con éxito");
        } else {
            response = ResponseEntity.ok().body("No se pudo actualizar un odontólogo que no existe");
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws ResourceNotFoundException {
        dentistService.delete(id);
        return ResponseEntity.ok("Se eliminó el odontólogo con id " + id);
    }

    @GetMapping
    public List<Dentist> findAll() {
        return dentistService.findAll();
    }

    @GetMapping("/registration/{registration}")
    public ResponseEntity<Dentist> findByRegistration(@PathVariable Integer registration) throws Exception {

        Optional<Dentist> dentist = dentistService.findByRegistration(registration);
        if ( dentist.isPresent() ) {
            return ResponseEntity.ok(dentist.get());
        } else {
            throw new Exception("No se encontró el odontólohgo  con la matricula " + registration);
        }
    }

}
