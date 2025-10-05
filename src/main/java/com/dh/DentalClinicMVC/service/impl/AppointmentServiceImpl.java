package com.dh.DentalClinicMVC.service.impl;

import com.dh.DentalClinicMVC.dto.AppointmentDTO;
import com.dh.DentalClinicMVC.entity.Appointment;
import com.dh.DentalClinicMVC.entity.Dentist;
import com.dh.DentalClinicMVC.entity.Patient;
import com.dh.DentalClinicMVC.exception.ResourceNotFoundException;
import com.dh.DentalClinicMVC.repository.IAppointmentRepository;
import com.dh.DentalClinicMVC.repository.IDentistRepository;
import com.dh.DentalClinicMVC.repository.IPatientRepository;
import com.dh.DentalClinicMVC.service.IAppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private IAppointmentRepository appointmentRepository;
    private IPatientRepository patientRepository;
    private IDentistRepository dentistRepository;

    public AppointmentServiceImpl(IAppointmentRepository appointmentRepository, IPatientRepository patientRepository, IDentistRepository dentistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
    }

    @Override
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        //mapear nuestras entidades como DTO a mano
        //instanciar una entidad de turno
        Appointment appointment = new Appointment();

        //instanciar paciente
        Patient patient = new Patient();
        patient.setId(appointmentDTO.getPatient_id());

        //instanciar odontólogo
        Dentist dentist = new Dentist();
        dentist.setId(appointmentDTO.getDentist_id());

        //seteamos el paciente y el odontólogo a nuestra entidad turno
        appointment.setPatient(patient);
        appointment.setDentist(dentist);

        //convertir el string del turnoDTO que es la fecha a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(appointmentDTO.getDate(), formatter);

        //setaeamos la fecha
        appointment.setDate(date);

        //persistir en la base datos
        appointmentRepository.save(appointment);

        //DTO a devolver
        AppointmentDTO appointmentDTOToReturn = new AppointmentDTO();

        //le seteamos los datos de la entidad que persistimos
        appointmentDTOToReturn.setId(appointment.getId());
        appointmentDTOToReturn.setDate(appointment.getDate().toString());
        appointmentDTOToReturn.setDentist_id(appointment.getDentist().getId());
        appointmentDTOToReturn.setPatient_id(appointment.getPatient().getId());

        return appointmentDTOToReturn;
    }

    @Override
    public Optional<AppointmentDTO> findById(Long id) throws ResourceNotFoundException {
        //vamos a buscar la entidad por id a la BD
        Optional<Appointment> appointmentToLookFor = appointmentRepository.findById(id);

        //instanciamos el dto
        Optional<AppointmentDTO> appointmentDTO = null;

        if (appointmentToLookFor.isPresent()) {
            //recuperar la entidad que se encontró y guardarla en la variable appointmen
            Appointment appointment = appointmentToLookFor.get();

            //trabajar sobre la información que tenemos que devolver: dto
            //vamos a crear una intancia de turno dto para devolver
            AppointmentDTO appointmentDTOToReturn = new AppointmentDTO();
            appointmentDTOToReturn.setId(appointment.getId());
            appointmentDTOToReturn.setPatient_id(appointment.getPatient().getId());
            appointmentDTOToReturn.setDentist_id(appointment.getDentist().getId());
            appointmentDTOToReturn.setDate(appointment.getDate().toString());

            appointmentDTO = Optional.of(appointmentDTOToReturn);

        } else {

            throw new ResourceNotFoundException("No se encontró el turno con el id " + id);
        }
        return appointmentDTO;
    }

    @Override
    public AppointmentDTO update(AppointmentDTO appointmentDTO) throws Exception {

        if (appointmentRepository.findById(appointmentDTO.getId()).isPresent()) {
            Optional<Appointment> appointment = appointmentRepository.findById(appointmentDTO.getId());

            //instanciar paciente
            Patient patient = new Patient();
            patient.setId(appointmentDTO.getPatient_id());

            //instanciar odontólogo
            Dentist dentist = new Dentist();
            dentist.setId(appointmentDTO.getDentist_id());

            //seteamos el paciente y el odontólogo a nuestra entidad turno
            appointment.get().setPatient(patient);
            appointment.get().setDentist(dentist);

            //convertir el string del turnoDTO que es la fecha a LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(appointmentDTO.getDate(), formatter);

            //setaeamos la fecha
            appointment.get().setDate(date);

            //persistir en la base datos
            appointmentRepository.save(appointment.get());

            //DTO a devolver
            AppointmentDTO appointmentDTOToReturn = new AppointmentDTO();
            appointmentDTOToReturn.setId(appointmentDTO.getId());
            appointmentDTOToReturn.setPatient_id(appointmentDTO.getPatient_id());
            appointmentDTOToReturn.setDentist_id((appointmentDTO.getDentist_id()));
            appointmentDTOToReturn.setDate(appointmentDTO.getDate());

            return appointmentDTOToReturn;

        } else {
            throw new Exception("No se pudo actualizar el turno");
        }

    }

    @Override
    public Optional<AppointmentDTO> delete(Long id) throws ResourceNotFoundException {
        Optional<Appointment> appointmentToLookFor = appointmentRepository.findById(id);
        Optional<AppointmentDTO> appointmentDTO;

        if (appointmentToLookFor.isPresent()) {
            Appointment appointment = appointmentToLookFor.get();
            AppointmentDTO appointmentDTOToReturn = new AppointmentDTO();
            appointmentDTOToReturn.setId(appointment.getId());
            appointmentDTOToReturn.setPatient_id(appointment.getPatient().getId());
            appointmentDTOToReturn.setDentist_id(appointment.getDentist().getId());
            appointmentDTOToReturn.setDate(appointment.getDate().toString());

            appointmentDTO =  Optional.of(appointmentDTOToReturn);
            return appointmentDTO;

        } else {
            throw new ResourceNotFoundException("No se encontró el turno con el id " + id);
        }

    }

    @Override
    public List<AppointmentDTO> findAll() {
        //vamos a traernos las entidades de la BD y guardarlas en una lista
        List<Appointment> appointments = appointmentRepository.findAll();

        //vamos a crear una lista vacia de turnos DTOs
        List<AppointmentDTO> appointmentDTOs = new ArrayList<>();

        //recorremos la lista de turnos para guardarlas en la lista de DTOs
        for (Appointment appointment : appointments) {
            appointmentDTOs.add(new AppointmentDTO(appointment.getId(),
                    appointment.getDentist().getId(),
                    appointment.getPatient().getId(),
                    appointment.getDate().toString()));
        }
        return appointmentDTOs;
    }
}
