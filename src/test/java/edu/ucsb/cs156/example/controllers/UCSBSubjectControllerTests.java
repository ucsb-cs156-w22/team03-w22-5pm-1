package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.Todo;
import edu.ucsb.cs156.example.entities.UCSBSubject;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.repositories.TodoRepository;
import edu.ucsb.cs156.example.repositories.UCSBSubjectRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)
public class UCSBSubjectControllerTests extends ControllerTestCase {

    @MockBean
    UCSBSubjectRepository subjectRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/todos/admin/all

    //get all tests
    @Test
    public void api_ucsb_subject_all__logged_out__returns_403() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(403));
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_all__user_logged_in__returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_all__admin_logged_in_returns_200() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }


    //get with id tests
    @Test
    public void api_ucsb_subject_get__logged_out__returns_403() throws Exception {
        mockMvc.perform(
            get("/api/UCSBSubjects/?id=420"))        
        .andExpect(status().is(403)).andReturn();
    }

    
    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_get__user_logged_in__returns_subject_with_id() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        when(subjectRepository.findById(eq(Long.valueOf(420)))).thenReturn(Optional.of(expectedSubject));
        
        MvcResult response = mockMvc.perform(
            get("/api/UCSBSubjects/?id=420")  )     
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).findById(eq(Long.valueOf(420)));
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_get__user_logged_in__searches_for_subject_does_not_exist() throws Exception {
        when(subjectRepository.findById(eq(29L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=29"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(subjectRepository, times(1)).findById(eq(29L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 29 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_get__admin_logged_in__returns_subject_with_id() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        when(subjectRepository.findById(eq(Long.valueOf(420)))).thenReturn(Optional.of(expectedSubject));
        
        MvcResult response = mockMvc.perform(
            get("/api/UCSBSubjects/?id=420"))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).findById(eq(Long.valueOf(420)));
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject__admin_logged_in__searches_for_subject_does_not_exist() throws Exception {
        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=420"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(subjectRepository, times(1)).findById(eq(420L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 not found", responseString);
    }


    //post tests
    @Test
    public void api_ucsb_subject_post__logged_out__returns_403() throws Exception {
        mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().is(403)).andReturn();
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_post__user_logged_in() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .build();

        when(subjectRepository.save(eq(expectedSubject))).thenReturn(expectedSubject);
        
        MvcResult response = mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).save(expectedSubject);
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_post__admin_logged_in() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .build();

        when(subjectRepository.save(eq(expectedSubject))).thenReturn(expectedSubject);
        
        MvcResult response = mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).save(expectedSubject);
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }
  

    //put tests
    @Test
    public void api_ucsb_subject_put__logged_out__returns_403() throws Exception{

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(420L)
                .build();

        String requestBody = mapper.writeValueAsString(updatedSubject);

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=420")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().is(403)).andReturn();
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_put__user_logged_in() throws Exception{
        UCSBSubject oldSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject correctSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        String requestBody = mapper.writeValueAsString(updatedSubject);
        String expectedReturn = mapper.writeValueAsString(correctSubject);

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.of(oldSubject));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=420")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        verify(subjectRepository, times(1)).save(correctSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_put__user_logged_in__id_does_not_exist() throws Exception{

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject correctSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        String requestBody = mapper.writeValueAsString(updatedSubject);
        String expectedReturn = mapper.writeValueAsString(correctSubject);

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=420")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_put__admin_logged_in() throws Exception{
        UCSBSubject oldSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject correctSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        String requestBody = mapper.writeValueAsString(updatedSubject);
        String expectedReturn = mapper.writeValueAsString(correctSubject);

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.of(oldSubject));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=420")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        verify(subjectRepository, times(1)).save(correctSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_put__admin_logged_in__id_does_not_exist() throws Exception{

        UCSBSubject updatedSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        UCSBSubject correctSubject = UCSBSubject.builder()
                .subjectCode("42069")
                .subjectTranslation("ben dover")
                .deptCode("420")
                .collegeCode("69")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        String requestBody = mapper.writeValueAsString(updatedSubject);
        String expectedReturn = mapper.writeValueAsString(correctSubject);

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.empty());


        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBSubjects?id=420")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 not found", responseString);
    }


    //delete tests
    @Test
    public void api_ucsb_subject_delete__logged_out__returns_403() throws Exception{
        
        // act
        mockMvc.perform(delete("/api/UCSBSubjects?id=420")
                .with(csrf()))
                .andExpect(status().is(403)).andReturn();
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_delete__user_logged_in() throws Exception{
        UCSBSubject oldSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();


        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.of(oldSubject));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBSubjects?id=420")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        verify(subjectRepository, times(1)).deleteById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 deleted", responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_subject_delete__user_logged_in__id_does_not_exist() throws Exception{

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBSubjects?id=420")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 not found", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_delete__admin_logged_in() throws Exception{
        UCSBSubject oldSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mike oxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();


        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.of(oldSubject));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBSubjects?id=420")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        verify(subjectRepository, times(1)).deleteById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 deleted", responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_subject_delete__admin_logged_in__id_does_not_exist() throws Exception{

        when(subjectRepository.findById(eq(420L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBSubjects?id=420")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(subjectRepository, times(1)).findById(420L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 420 not found", responseString);
    }

}
