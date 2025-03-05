package edu.eci.cvds.proyect.booking;


import edu.eci.cvds.proyect.booking.controller.UserController;
import edu.eci.cvds.proyect.booking.documents.UserRole;
import edu.eci.cvds.proyect.booking.documents.Users;
import edu.eci.cvds.proyect.booking.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSaveUser() throws Exception {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

        mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Andres silva"));
    }

    @Test
    public void testFindAllUsers() throws Exception {


        Users user1 = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        Users user2= new Users(2, "Juan lopez", "JuanLopez@gmail.com", UserRole.TEACHER, "1234567");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/Users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Andres silva"))
                .andExpect(jsonPath("$[1].name").value("Juan lopez"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Users user = new Users(1, "Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

        mockMvc.perform(put("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userRepository).deleteById(1);

        mockMvc.perform(delete("/Users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado"));
    }
}

