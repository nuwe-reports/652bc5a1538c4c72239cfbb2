package com.example.demo;

import com.example.demo.entities.Appointment;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import org.junit.jupiter.api.BeforeEach;
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

/**
 * EntityUnitTest
 */
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @BeforeEach
    void setup() {
        d1 = createAndPersistDoctor("Gisselle", "Montenegro", 30, "gisselle@gmail.com");
        r1 = createAndPersistRoom("Room 1");
        p1 = createAndPersistPatient("Guillermo", "Santisteban", 30, "guillermosg28@gmail.com");
    }

    /**
     * Method under test: {@link Patient#Patient()}
     */
    @Test
    void testEntityPatient() {
        assertEquals("Guillermo", p1.getFirstName());
        assertEquals("Santisteban", p1.getLastName());
        assertEquals(30, p1.getAge());
        assertEquals("guillermosg28@gmail.com", p1.getEmail());
    }

    /**
     * Method under test: {@link Doctor#Doctor()}
     */
    @Test
    void testEntityDoctor() {
        assertEquals("Gisselle", d1.getFirstName());
        assertEquals("Montenegro", d1.getLastName());
        assertEquals(30, d1.getAge());
        assertEquals("gisselle@gmail.com", d1.getEmail());
    }

    /**
     * Method under test: {@link Room#Room()}
     */
    @Test
    void testEntityRoom() {
        assertEquals("Room 1", r1.getRoomName());
    }

    @Test
    void testEntityAppointment() {
        LocalDateTime startsAt = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:00 16/10/2023", formatter);
        LocalDateTime startsAt2 = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        LocalDateTime startsAt3 = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt3 = LocalDateTime.parse("20:00 16/10/2023", formatter);

        Room r2 = new Room("Room 2");

        a1 = createAndPersistAppointment(p1, d1, r1, startsAt, finishesAt);
        a2 = createAndPersistAppointment(p1, d1, r2, startsAt2, finishesAt2);
        a3 = createAndPersistAppointment(p1, d1, r1, startsAt3, finishesAt3);

        assertEquals(p1, a1.getPatient());
        assertEquals(d1, a1.getDoctor());
        assertEquals(r1, a1.getRoom());
        assertEquals(startsAt, a1.getStartsAt());
        assertEquals(finishesAt, a1.getFinishesAt());

        assertEquals(p1, a2.getPatient());
        assertEquals(d1, a2.getDoctor());
        assertEquals(r2, a2.getRoom());
        assertEquals(startsAt2, a2.getStartsAt());
        assertEquals(finishesAt2, a2.getFinishesAt());

        assertEquals(p1, a3.getPatient());
        assertEquals(d1, a3.getDoctor());
        assertEquals(r1, a3.getRoom());
        assertEquals(startsAt3, a3.getStartsAt());
        assertEquals(finishesAt3, a3.getFinishesAt());

        assertFalse(a1.overlaps(a2));
        assertTrue(a1.overlaps(a3));

    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps() {
        Doctor doctor2 = createAndPersistDoctor("Doctor 2", "Apellidos", 30, "doctor2@gmail.com");
        Patient patient2 = createAndPersistPatient("Paciente 2", "Apellido 2", 25, "paciente2@gmail.com");
        Room room2 = createAndPersistRoom("Sala 1");

        LocalDateTime startsAt = LocalDateTime.of(2023, 10, 16, 0, 0);
        LocalDateTime finishesAt = LocalDateTime.of(2023, 10, 16, 0, 30);

        a1 = createAndPersistAppointment(p1, d1, r1, startsAt, finishesAt);
        a2 = createAndPersistAppointment(patient2, doctor2, room2, startsAt, finishesAt);

        assertFalse(a1.overlaps(a2));
    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps2() {
        Room room2 = createAndPersistRoom("Sala 1");

        LocalDateTime startsAt = LocalDateTime.of(2023, 10, 16, 0, 0);
        LocalDateTime finishesAt = LocalDateTime.of(2023, 10, 16, 0, 30);

        a1 = createAndPersistAppointment(p1, d1, room2, startsAt, finishesAt);

        Doctor doctor2 = createAndPersistDoctor("Doctor 2", "Apellidos", 30, "doctor2@gmail.com");
        Patient patient2 = createAndPersistPatient("Paciente 2", "Apellido 2", 25, "paciente2@gmail.com");

        LocalDateTime startsAt2 = LocalDateTime.of(2023, 10, 16, 0, 0);
        LocalDateTime finishesAt2 = LocalDateTime.of(2023, 10, 16, 0, 30);

        a2 = createAndPersistAppointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        assertTrue(a1.overlaps(a2));
    }

    /**
     * Method under test: {@link Appointment#overlaps(Appointment)}
     */
    @Test
    void testOverlaps3() {
        LocalDateTime startsAt = LocalDate.now().atStartOfDay();
        LocalDateTime finishesAt = LocalDate.of(2023, 10, 16).atStartOfDay();

        a1 = createAndPersistAppointment(p1, d1, r1, startsAt, finishesAt);

        Doctor doctor2 = createAndPersistDoctor("Doctor 2", "Apellidos", 30, "doctor2@gmail.com");
        Patient patient2 = createAndPersistPatient("Paciente 2", "Apellido 2", 25, "paciente2@gmail.com");

        LocalDateTime startsAt2 = LocalDate.of(2023, 10, 16).atStartOfDay();
        LocalDateTime finishesAt2 = finishesAt;

        a2 = createAndPersistAppointment(patient2, doctor2, r1, startsAt2, finishesAt2);

        assertTrue(a1.overlaps(a2));
    }


    /**
     * Crea y persiste un objeto Doctor en la base de datos.
     *
     * @param firstName El nombre del doctor.
     * @param lastName  El apellido del doctor.
     * @param age       La edad del doctor.
     * @param email     El correo electrónico del doctor.
     * @return El objeto Doctor creado y persistido.
     */
    private Doctor createAndPersistDoctor(String firstName, String lastName, int age, String email) {
        Doctor doctor = new Doctor(firstName, lastName, age, email);
        return entityManager.persistAndFlush(doctor);
    }

    /**
     * Crea y persiste un objeto Patient en la base de datos.
     *
     * @param firstName El nombre del paciente.
     * @param lastName  El apellido del paciente.
     * @param age       La edad del paciente.
     * @param email     El correo electrónico del paciente.
     * @return El objeto Patient creado y persistido.
     */
    private Patient createAndPersistPatient(String firstName, String lastName, int age, String email) {
        Patient patient = new Patient(firstName, lastName, age, email);
        return entityManager.persistAndFlush(patient);
    }

    /**
     * Crea y persiste un objeto Room en la base de datos.
     *
     * @param roomName El nombre de la habitación.
     * @return El objeto Room creado y persistido.
     */
    private Room createAndPersistRoom(String roomName) {
        Room room = new Room(roomName);
        return entityManager.persistAndFlush(room);
    }

    /**
     * Crea y persiste un objeto Appointment en la base de datos.
     *
     * @param patient     El paciente para la cita.
     * @param doctor      El doctor para la cita.
     * @param room        La habitación para la cita.
     * @param startsAt    La fecha y hora de inicio de la cita.
     * @param finishesAt  La fecha y hora de finalización de la cita.
     * @return El objeto Appointment creado y persistido.
     */
    private Appointment createAndPersistAppointment(Patient patient, Doctor doctor, Room room, LocalDateTime startsAt, LocalDateTime finishesAt) {
        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        return entityManager.persistAndFlush(appointment);
    }

}
