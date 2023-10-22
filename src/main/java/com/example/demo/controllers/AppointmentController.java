package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * AppointmentController
 */

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    /**
     * @return List<Appointment>
     */
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * @param id
     * @return Appointment
     */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param newAppointment
     * @return List<Appointment>
     */
    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment newAppointment) {
        if (isInvalidTimeRange(newAppointment)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (hasOverlap(newAppointment)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        appointmentRepository.save(newAppointment);
        List<Appointment> appointments = appointmentRepository.findAll();

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * @param id
     * @return HttpStatus
     */
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * @return HttpStatus
     */
    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param appointment
     * @return boolean
     */
    private boolean isInvalidTimeRange(Appointment appointment) {
        return appointment.getStartsAt().equals(appointment.getFinishesAt());
    }

    /**
     * @param newAppointment
     * @return boolean
     */
    private boolean hasOverlap(Appointment newAppointment) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        for (Appointment existingAppointment : existingAppointments) {
            if (newAppointment.overlaps(existingAppointment)) {
                return true;
            }
        }
        return false;
    }

}
