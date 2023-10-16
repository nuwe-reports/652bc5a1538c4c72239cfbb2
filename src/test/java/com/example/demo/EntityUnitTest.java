package com.example.demo;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    /**
     * Method under test: {@link Patient#Patient()}
     */
    @Test
    void testPatientEntity() {
        p1 = new Patient("Guillermo", "Santisteban", 30, "guillermosg28@gmail.com");

        Patient savePatient = entityManager.persistAndFlush(p1);

        Patient findPatient = entityManager.find(Patient.class, savePatient.getId());

        assertEquals("Guillermo", findPatient.getFirstName());
        assertEquals("Santisteban", findPatient.getLastName());
        assertEquals(30, findPatient.getAge());
        assertEquals("guillermosg28@gmail.com", findPatient.getEmail());
    }

    /**
     * Method under test: {@link Doctor#Doctor()}
     */
    @Test
    void testDoctorEntity() {
        d1 = new Doctor("Gisselle", "Montenegro", 30, "gisselle@gmail.com");

        Doctor saveDoctor = entityManager.persistAndFlush(d1);

        Doctor findDoctor = entityManager.find(Doctor.class, saveDoctor.getId());

        assertEquals("Gisselle", findDoctor.getFirstName());
        assertEquals("Montenegro", findDoctor.getLastName());
        assertEquals(30, findDoctor.getAge());
        assertEquals("gisselle@gmail.com", findDoctor.getEmail());
    }

    /**
     * Method under test: {@link Room#Room()}
     */
    @Test
    void testRoomEntity() {
        r1 = new Room("Room 1");

        Room saveRoom = entityManager.persistAndFlush(r1);

        Room findRoom = entityManager.find(Room.class, saveRoom.getRoomName());

        assertEquals("Room 1", findRoom.getRoomName());
    }

    @Test
    void testAppointmentEntity() {
        p1 = new Patient("Guillermo", "Santisteban", 30, "guillermosg28@gmail.com");
        d1 = new Doctor("Gisselle", "Montenegro", 30, "gisselle@gmail.com");
        r1 = new Room("Room 1");
        Room r2 = new Room("Room 2");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:00 16/10/2023", formatter);
        LocalDateTime startsAt2 = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);

        LocalDateTime startsAt3 = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt3 = LocalDateTime.parse("20:00 16/10/2023", formatter);


        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);
        a2 = new Appointment(p1, d1, r2, startsAt2, finishesAt2);
        a3 = new Appointment(p1, d1, r1, startsAt3, finishesAt3);

        Appointment saveAppointment = entityManager.persistAndFlush(a1);
        Appointment saveAppointment2 = entityManager.persistAndFlush(a2);
        Appointment saveAppointment3 = entityManager.persistAndFlush(a3);

        Appointment findAppointment = entityManager.find(Appointment.class, saveAppointment.getId());
        Appointment findAppointment2 = entityManager.find(Appointment.class, saveAppointment2.getId());
        Appointment findAppointment3 = entityManager.find(Appointment.class, saveAppointment3.getId());

        assertEquals(p1, findAppointment.getPatient());
        assertEquals(d1, findAppointment.getDoctor());
        assertEquals(r1, findAppointment.getRoom());
        assertEquals(startsAt, findAppointment.getStartsAt());
        assertEquals(finishesAt, findAppointment.getFinishesAt());

        assertEquals(p1, findAppointment2.getPatient());
        assertEquals(d1, findAppointment2.getDoctor());
        assertEquals(r2, findAppointment2.getRoom());
        assertEquals(startsAt2, findAppointment2.getStartsAt());
        assertEquals(finishesAt2, findAppointment2.getFinishesAt());

        assertEquals(p1, findAppointment3.getPatient());
        assertEquals(d1, findAppointment3.getDoctor());
        assertEquals(r1, findAppointment3.getRoom());
        assertEquals(startsAt3, findAppointment3.getStartsAt());
        assertEquals(finishesAt3, findAppointment3.getFinishesAt());

        assertFalse(a1.overlaps(a2));

        assertTrue(a1.overlaps(a3));

    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps() {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        Patient patient = new Patient();
        patient.setAge(28);
        patient.setEmail("gisselle@gmail.com");
        patient.setFirstName("Gisselle");
        patient.setId(1L);
        patient.setLastName("Montenegro");

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setRoom(new Room());
        appointment.setStartsAt(LocalDate.of(2023, 10, 16).atStartOfDay());

        Doctor doctor2 = new Doctor();
        doctor2.setAge(30);
        doctor2.setEmail("doctor2@gmail.com");
        doctor2.setFirstName("Doctor 2");
        doctor2.setId(1L);
        doctor2.setLastName("Apellidos");

        Patient patient2 = new Patient();
        patient2.setAge(25);
        patient2.setEmail("paciente2@gmail.com");
        patient2.setFirstName("Paciente 2");
        patient2.setId(1L);
        patient2.setLastName("Apellido 2");

        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor2);
        appointment2.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment2.setId(1L);
        appointment2.setPatient(patient2);
        appointment2.setRoom(new Room("Sala 1"));
        appointment2.setStartsAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        assertFalse(appointment.overlaps(appointment2));
    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps2() {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        Patient patient = new Patient();
        patient.setAge(28);
        patient.setEmail("gisselle@gmail.com");
        patient.setFirstName("Gisselle");
        patient.setId(1L);
        patient.setLastName("Montenegro");

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setRoom(new Room("Sala 1"));
        appointment.setStartsAt(LocalDate.of(2023, 10, 16).atStartOfDay());

        Doctor doctor2 = new Doctor();
        doctor2.setAge(30);
        doctor2.setEmail("doctor2@gmail.com");
        doctor2.setFirstName("Doctor 2");
        doctor2.setId(1L);
        doctor2.setLastName("Apellidos");

        Patient patient2 = new Patient();
        patient2.setAge(25);
        patient2.setEmail("paciente2@gmail.com");
        patient2.setFirstName("Paciente 2");
        patient2.setId(1L);
        patient2.setLastName("Apellido 2");

        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor2);
        appointment2.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment2.setId(1L);
        appointment2.setPatient(patient2);
        appointment2.setRoom(new Room("Sala 1"));
        appointment2.setStartsAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        assertTrue(appointment.overlaps(appointment2));
    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps3() {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        Patient patient = new Patient();
        patient.setAge(28);
        patient.setEmail("gisselle@gmail.com");
        patient.setFirstName("Gisselle");
        patient.setId(1L);
        patient.setLastName("Montenegro");

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setRoom(new Room("Sala 1"));
        appointment.setStartsAt(LocalDate.now().atStartOfDay());

        Doctor doctor2 = new Doctor();
        doctor2.setAge(30);
        doctor2.setEmail("doctor2@gmail.com");
        doctor2.setFirstName("Doctor 2");
        doctor2.setId(1L);
        doctor2.setLastName("Apellidos");

        Patient patient2 = new Patient();
        patient2.setAge(25);
        patient2.setEmail("paciente2@gmail.com");
        patient2.setFirstName("Paciente 2");
        patient2.setId(1L);
        patient2.setLastName("Apellido 2");

        Appointment appointment2 = new Appointment();
        appointment2.setDoctor(doctor2);
        appointment2.setFinishesAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        appointment2.setId(1L);
        appointment2.setPatient(patient2);
        appointment2.setRoom(new Room("Sala 1"));
        appointment2.setStartsAt(LocalDate.of(2023, 10, 16).atStartOfDay());
        assertTrue(appointment.overlaps(appointment2));
    }
}
