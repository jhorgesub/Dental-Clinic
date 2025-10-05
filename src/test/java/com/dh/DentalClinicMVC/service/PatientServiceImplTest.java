package com.dh.DentalClinicMVC.service;

import com.dh.DentalClinicMVC.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatientServiceImplTest {

    @Autowired
    private IPatientService patientService;

    @Test
    void findById() {
        Long idPatient = 1L;

        //buscar el paciente
        Optional<Patient> patient = patientService.findById(idPatient);
        assertNotNull(patient);
    }
}