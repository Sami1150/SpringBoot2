package com.springboot.mapping.teacher;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherService teacherService;
    @Test
    public void testFindAllTeachers() throws Exception {
        mockMvc.perform(get("/api/v1/teacher")
                        .with(testUser("reporter","REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.startsWith("{\"content\":[{\"id\":1,\"course\":\"Science\",\"salary\":50000},{\"id\":2,\"course\":\"Math\",\"salary\":60000},{\"id\":3,\"course\":\"History\",\"salary\":45000}]}"
                        )));
    }

    // Test Get Teacher by ID:
    @Test
    public void testFindById() throws Exception {
        // Create a sample teacher
        Teacher teacher = new Teacher(1, "Science", 50000);

        // Mock the service method
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherService.findById(999L)).thenReturn(null); // Non-existent teacher

        // Test the controller method for existing teacher
        mockMvc.perform(get("/api/v1/teacher/1")
                        .with(testUser("reporter","REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.course", equalTo("Science")))
                .andExpect(jsonPath("$.salary", equalTo(50000)))
                .andReturn();

        // Test the controller method for non-existent teacher
        mockMvc.perform(get("/api/v1/teacher/999")
                .with(testUser("reporter","REPORTER"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    // Test to Verify Post Method
    @Test
    public void testPost() throws Exception {
        Teacher newTeacher = new Teacher(4, "Math", 60000); // Creating a new teacher object

        // Mock the service method
        when(teacherService.create(any(Teacher.class))).thenReturn(newTeacher);

        // Test the controller method for creating a new teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/teacher/")
                        .with(testUser("reporter","REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"id\": 4, \"course\": \"Math\", \"salary\": 60000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.course", equalTo("Math")))
                .andExpect(jsonPath("$.salary", equalTo(60000)))
                .andReturn();
    }



    // Test to verify PUT or UPDATE Method
    @Test
    public void testUpdate() throws Exception {
        Teacher updatedTeacher = new Teacher(4, "Biology", 55000); // Creating an updated teacher object

        // Mock the service method
        when(teacherService.update(eq(4L), any(Teacher.class))).thenReturn(updatedTeacher);

        // Test the controller method for updating a teacher
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/teacher/4/")
                        .with(testUser("reporter", "REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"id\": 4, \"course\": \"Biology\", \"salary\": 55000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.course", equalTo("Biology")))
                .andExpect(jsonPath("$.salary", equalTo(55000)))
                .andReturn();
    }

    // Test to verify DELETE Method
    @Test
    public void testDelete() throws Exception {
        // Mock the service method to indicate successful deletion
        when(teacherService.delete(4L)).thenReturn(true);

        // Test the controller method for deleting a teacher
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/teacher/4")
                        .with(testUser("reporter", "REPORTER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent()) // HTTP 204 indicates successful deletion
                .andReturn();
    }


    private RequestPostProcessor testUser(String userName, String authoriy) {
        return SecurityMockMvcRequestPostProcessors.user(userName).authorities(new SimpleGrantedAuthority(authoriy));
    }
}
