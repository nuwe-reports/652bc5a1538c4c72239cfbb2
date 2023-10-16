
package com.example.demo;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Method under test: {@link DoctorController#createDoctor(Doctor)}
     */
    @Test
    void testCreateDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        when(doctorRepository.save(Mockito.<Doctor>any())).thenReturn(doctor);

        Doctor doctor2 = new Doctor();
        doctor2.setAge(28);
        doctor2.setEmail("gisselle@gmail.com");
        doctor2.setFirstName("Gisselle");
        doctor2.setLastName("Montenegro");
        doctor2.setId(2L);

        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)))
                .andExpect(status().isCreated());
    }

    /**
     * Method under test: {@link DoctorController#deleteAllDoctors()}
     */
    @Test
    void testDeleteAllDoctors() throws Exception {
        doNothing().when(doctorRepository).deleteAll();
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link DoctorController#deleteDoctor(long)}
     */
    @Test
    void testDeleteDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link DoctorController#deleteDoctor(long)}
     */
    @Test
    void testDeleteDoctorNotFound() throws Exception {
        long id = 46;

        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link DoctorController#getAllDoctors()}
     */
    @Test
    void testGetAllDoctorsNoContent() throws Exception {
        List<Doctor> doctors = new ArrayList<>();

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());
    }

    /**
     * Method under test: {@link DoctorController#getAllDoctors()}
     */
    @Test
    void testGetAllDoctors() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link DoctorController#getDoctorById(long)}
     */
    @Test
    void testGetDoctorById() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setAge(30);
        doctor.setEmail("guillermo@gmail.com");
        doctor.setFirstName("Guillermo");
        doctor.setId(1L);
        doctor.setLastName("Santisteban");

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link DoctorController#getDoctorById(long)}
     */
    @Test
    void testGetDoctorByIdNotFound() throws Exception {
        long id = 46;

        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Method under test: {@link PatientController#createPatient(Patient)}
     */
    @Test
    void testCreatePatient() throws Exception {
        Patient patient = new Patient();
        patient.setAge(30);
        patient.setEmail("guillermo@gmail.com");
        patient.setFirstName("Guillermo");
        patient.setId(1L);
        patient.setLastName("Santisteban");
        when(patientRepository.save(Mockito.<Patient>any())).thenReturn(patient);

        Patient patient2 = new Patient();
        patient2.setAge(28);
        patient2.setEmail("gisselle@gmail.com");
        patient2.setFirstName("Gisselle");
        patient2.setId(1L);
        patient2.setLastName("Montenegro");

        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient2)))
                .andExpect(status().isCreated());
    }

    /**
     * Method under test: {@link PatientController#deleteAllPatients()}
     */
    @Test
    void testDeleteAllPatients() throws Exception {
        doNothing().when(patientRepository).deleteAll();
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link PatientController#deletePatient(long)}
     */
    @Test
    void testDeletePatient() throws Exception {
        Patient patient = new Patient();
        patient.setAge(30);
        patient.setEmail("guillermo@gmail.com");
        patient.setFirstName("Guillermo");
        patient.setId(1L);
        patient.setLastName("Santisteban");

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link PatientController#deletePatient(long)}
     */
    @Test
    void testDeletePatientNotFound() throws Exception {
        long id = 46;

        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link PatientController#getAllPatients()}
     */
    @Test
    void testGetAllPatientsNoContent() throws Exception {
        List<Patient> patients = new ArrayList<>();

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    /**
     * Method under test: {@link PatientController#getAllPatients()}
     */
    @Test
    void testGetAllPatients() throws Exception {
        Patient patient = new Patient();
        patient.setAge(30);
        patient.setEmail("guillermo@gmail.com");
        patient.setFirstName("Guillermo");
        patient.setId(1L);
        patient.setLastName("Santisteban");

        List<Patient> patients = new ArrayList<>();
        patients.add(patient);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link PatientController#getPatientById(long)}
     */
    @Test
    void testGetPatientById() throws Exception {
        Patient patient = new Patient();
        patient.setAge(30);
        patient.setEmail("guillermo@gmail.com");
        patient.setFirstName("Guillermo");
        patient.setId(1L);
        patient.setLastName("Santisteban");

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link PatientController#getPatientById(long)}
     */
    @Test
    void testGetPatientByIdNotFound() throws Exception {
        long id = 46;

        mockMvc.perform(get("/api/patients/" + id))
                .andExpect(status().isNotFound());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Method under test: {@link RoomController#createRoom(Room)}
     */
    @Test
    void testCreateRoom() throws Exception {
        Room room = new Room("Sala 01");

        when(roomRepository.save(Mockito.<Room>any())).thenReturn(room);

        Room room2 = new Room("Sala 02");

        mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room2)))
                .andExpect(status().isCreated());
    }

    /**
     * Method under test: {@link RoomController#deleteAllRooms()}
     */
    @Test
    void testDeleteAllRooms() throws Exception {
        doNothing().when(roomRepository).deleteAll();
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link RoomController#deleteRoom(String)}
     */
    @Test
    void testDeleteRoom() throws Exception {
        Room room = new Room("Sala 01");

        Optional<Room> opt = Optional.of(room);

        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo("Sala 01");

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);

        mockMvc.perform(delete("/api/rooms/Sala 01"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link RoomController#deleteRoom(String)}
     */
    @Test
    void testDeleteRoomNotFound() throws Exception {
        String roomName = "Sala 01";

        mockMvc.perform(delete("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link RoomController#getAllRooms()}
     */
    @Test
    void testGetAllRoomsNoContent() throws Exception {
        List<Room> rooms = new ArrayList<>();

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent());
    }

    /**
     * Method under test: {@link RoomController#getAllRooms()}
     */
    @Test
    void testGetAllRooms() throws Exception {
        Room room = new Room("Sala 01");

        List<Room> rooms = new ArrayList<>();
        rooms.add(room);

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link RoomController#getRoomByRoomName(String)}
     */
    @Test
    void testGetRoomByRoomName() throws Exception {
        Room room = new Room("Sala 01");

        Optional<Room> opt = Optional.of(room);

        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo("Sala 01");

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);

        mockMvc.perform(get("/api/rooms/Sala 01"))
                .andExpect(status().isOk());
    }

    /**
     * Method under test: {@link RoomController#getRoomByRoomName(String)}
     */
    @Test
    void testGetRoomByRoomNameNotFound() throws Exception {
        String roomName = "Sala 01";

        mockMvc.perform(get("/api/rooms/" + roomName))
                .andExpect(status().isNotFound());
    }

}
