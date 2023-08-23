package com.springboot.mapping.teacher;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void testFindAllTeachers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/teacher"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(
                        Matchers.startsWith("{\"content\":[{\"id\":1,\"course\":\"Science\",\"salary\":50000},{\"id\":2,\"course\":\"Math\",\"salary\":60000},{\"id\":3,\"course\":\"History\",\"salary\":45000}]}"
                        )));
    }
    @Test
    public void testFindAll() {
        // Mock the TeacherService
        TeacherService teacherService = mock(TeacherService.class);

        // Create sample data
        Teacher teacher1 = new Teacher(1, "Science", 50000);
        Teacher teacher2 = new Teacher(2, "Math", 60000);
        Teacher teacher3 = new Teacher(3, "History", 45000);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2, teacher3);

        // Mock the service method
        when(teacherService.findAll()).thenReturn(teachers);

        // Create the controller
        TeacherController teacherController = new TeacherController(teacherService);

        // Test the controller method
        ResponseEntity<Map<String, List<Teacher>>> response = teacherController.findAll();

        // Assert the response status code
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        // Assert the response body
        Map<String, List<Teacher>> responseBody = response.getBody();
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.get("content"), equalTo(teachers));
    }

    // Test Get Teacher by ID:
    @Test
    public void testFindById() {
        // Mock the TeacherService
        TeacherService teacherService = mock(TeacherService.class);

        // Create a sample teacher
        Teacher teacher = new Teacher(1, "Science", 50000);

        // Mock the service method
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherService.findById(999L)).thenReturn(null); // Non-existent teacher

        // Create the controller
        TeacherController teacherController = new TeacherController(teacherService);

        // Test the controller method for existing teacher
        ResponseEntity<Teacher> responseExisting = teacherController.findById(1L);
        assertThat(responseExisting.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseExisting.getBody(), equalTo(teacher));

        // Test the controller method for non-existent teacher
        ResponseEntity<Teacher> responseNonExistent = teacherController.findById(999L);
        assertThat(responseNonExistent.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }


    // Test to Verify Post Method
    @Test
    public void testPost() {
        // Mock the TeacherService and Repository
        TeacherService teacherService = mock(TeacherService.class);
        TeacherRepository teacherRepository = mock(TeacherRepository.class);

        // Create a sample teacher
        Teacher newTeacher = new Teacher(1, "Math", 60000);

        // Mock the service method
        when(teacherService.create(any(Teacher.class))).thenReturn(newTeacher);

        // Create the controller
        TeacherController teacherController = new TeacherController(teacherService);

        // Test the controller method
        ResponseEntity<Teacher> response = teacherController.create(newTeacher);

        // Assert the response status code and body
        verify(teacherService).create(newTeacher);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newTeacher, response.getBody());
    }


    // Test to verify PUT or UPDATE Method
    @Test
    public void testUpdate() {
        // Mock the TeacherService and Repository
        TeacherService teacherService = mock(TeacherService.class);
        TeacherRepository teacherRepository = mock(TeacherRepository.class);

        // Create a sample teacher
        Teacher existingTeacher = new Teacher(1, "Science", 50000);
        Teacher updatedTeacher = new Teacher(1, "Math", 60000);

        // Mock the service method
        when(teacherService.update(eq(1L), any(Teacher.class))).thenReturn(updatedTeacher);

        // Create the controller
        TeacherController teacherController = new TeacherController(teacherService);

        // Test the controller method
        ResponseEntity<Teacher> response = teacherController.update(1L, updatedTeacher);

        // Assert the response status code and body
        verify(teacherService).update(eq(1L), any(Teacher.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTeacher, response.getBody());
    }

    // Test to verify DELETE Method
    @Test
    public void testDelete() {
        // Mock the TeacherService and Repository
        TeacherService teacherService = mock(TeacherService.class);
        TeacherRepository teacherRepository = mock(TeacherRepository.class);

        // Create a sample teacher
        Teacher existingTeacher = new Teacher(1, "Science", 50000);

        // Mock the service method
        when(teacherService.delete(eq(1L))).thenReturn(true);

        // Create the controller
        TeacherController teacherController = new TeacherController(teacherService);

        // Test the controller method for successful deletion
        ResponseEntity<String> responseSuccess = teacherController.delete(1L);

        // Assert the response status code and body
        verify(teacherService).delete(eq(1L));
        assertEquals(HttpStatus.OK, responseSuccess.getStatusCode());
        assertEquals("Resource deleted successfully", responseSuccess.getBody());

        // Mock the service method for unsuccessful deletion
        when(teacherService.delete(eq(2L))).thenReturn(false);

        // Test the controller method for unsuccessful deletion
        ResponseEntity<String> responseFailure = teacherController.delete(2L);

        // Assert the response status code and body
        verify(teacherService).delete(eq(2L));
        assertEquals(HttpStatus.NOT_FOUND, responseFailure.getStatusCode());
        assertEquals("Resource not found or could not be deleted", responseFailure.getBody());
    }
}
