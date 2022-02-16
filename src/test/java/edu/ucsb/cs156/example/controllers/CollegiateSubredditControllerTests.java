package edu.ucsb.cs156.example.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.CollegiateSubreddit;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.repositories.CollegiateSubredditRepository;
import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {
    
    @MockBean
    CollegiateSubredditRepository collegiateSubredditRepository;

    @MockBean
    UserRepository userRepository;

    @WithMockUser(roles = { "USER" })
    @Test
    public void getAllSubreddits() throws Exception {
        CollegiateSubreddit csr = CollegiateSubreddit.builder().name("TestName").location("TestLoc").subreddit("TestSub").id(1L).build();

        ArrayList<CollegiateSubreddit> expectedSubreddits = new ArrayList<>();
        expectedSubreddits.add(csr);

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedSubreddits);

        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/all")).andExpect(status().isOk()).andReturn();


        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void getSubredditById() throws Exception {
        long ID = 1L;
        CollegiateSubreddit expectedCsr = CollegiateSubreddit.builder().name("SawCon").location("Su Ghana").subreddit("SawConvention").id(ID).build();

        when(collegiateSubredditRepository.findById(ID)).thenReturn(Optional.of(expectedCsr));

        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/?id=" + ID))
            .andExpect(status().isOk()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(ID);
        String expectedJson = mapper.writeValueAsString(expectedCsr);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void getSubredditByIdBadRequest() throws Exception {
        long ID = 1L;

        when(collegiateSubredditRepository.findById(eq(ID))).thenReturn(Optional.empty());

        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/?id=" + ID))
            .andExpect(status().isBadRequest()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(eq(ID));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("subreddit with id 1 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void postSubreddit() throws Exception {

        CollegiateSubreddit expectedSubreddit = CollegiateSubreddit.builder()
            .name("TestName")
            .location("TestLoc")
            .subreddit("TestSub")
            .id(0L)
            .build();

        when(collegiateSubredditRepository.save(eq(expectedSubreddit))).thenReturn(expectedSubreddit);

        MvcResult response = mockMvc.perform(
            post("/api/collegiateSubreddits/post?name=TestName&location=TestLoc&subreddit=TestSub")
                .with(csrf()))
                .andExpect(status().isOk()).andReturn();
        
        verify(collegiateSubredditRepository, times(1)).save(expectedSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);

    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void putSubreddit() throws Exception {
        long ID = 1L;
        CollegiateSubreddit oldPost = CollegiateSubreddit.builder()
            .name("OldTestName")
            .location("OldTestLoc")
            .subreddit("OldTestSub")
            .id(ID)
            .build();

        CollegiateSubreddit newPost = CollegiateSubreddit.builder()
            .name("TestName")
            .location("TestLoc")
            .subreddit("TestSub")
            .id(ID)
            .build();
        
        CollegiateSubreddit correctPost = CollegiateSubreddit.builder()
            .name("TestName")
            .location("TestLoc")
            .subreddit("TestSub")
            .id(ID)
            .build();

        String requestBody = mapper.writeValueAsString(newPost);
        String expectedReturn = mapper.writeValueAsString(correctPost);

        when(collegiateSubredditRepository.findById(eq(ID))).thenReturn(Optional.of(oldPost));

        MvcResult response = mockMvc.perform(
            put("/api/collegiateSubreddits/?id=" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(requestBody)
                .with(csrf()))
            .andExpect(status().isOk()).andReturn();
        
        // assert
        verify(collegiateSubredditRepository, times(1)).findById(ID);
        verify(collegiateSubredditRepository, times(1)).save(correctPost);

        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void deleteSubreddit() throws Exception {
        long ID = 1L;
        CollegiateSubreddit csr = CollegiateSubreddit.builder().name("Emma Chizit").location("Outback").subreddit("AustralianMemez").id(ID).build();

        when(collegiateSubredditRepository.findById(eq(ID))).thenReturn(Optional.of(csr));

        MvcResult response = mockMvc.perform(
            delete("/api/collegiateSubreddits/?id=" + ID)
                .with(csrf()))
            .andExpect(status().isOk()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(ID);
        verify(collegiateSubredditRepository, times(1)).deleteById(ID);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("record " + ID + " deleted", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void putSubredditDoesntExist() throws Exception {
        // arrange
        CollegiateSubreddit correctPost = CollegiateSubreddit.builder()
            .name("TestName")
            .location("TestLoc")
            .subreddit("TestSub")
            .id(0L)
            .build();

        String requestBody = mapper.writeValueAsString(correctPost);

        when(collegiateSubredditRepository.findById(eq(0L))).thenReturn(Optional.empty());

        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddits/?id=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(0L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subreddit with id " + 0L + " not found", responseString);
    }


    @WithMockUser(roles = { "USER" })
    @Test
    public void deleteSubredditBadRequest() throws Exception {
        long ID = 1L;
        when(collegiateSubredditRepository.findById(eq(ID))).thenReturn(Optional.empty());

        MvcResult response = mockMvc.perform(
            delete("/api/collegiateSubreddits/?id=" + ID)
                .with(csrf()))
            .andExpect(status().isBadRequest()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(ID);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("record " + ID + " not found", responseString);
    }
}
