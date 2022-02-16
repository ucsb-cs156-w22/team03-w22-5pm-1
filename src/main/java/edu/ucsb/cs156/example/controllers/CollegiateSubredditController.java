package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.CollegiateSubreddit;
import edu.ucsb.cs156.example.models.CurrentUser;
import edu.ucsb.cs156.example.repositories.CollegiateSubredditRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "API Information for Collegiate Subreddits")
@RequestMapping("/api/collegiateSubreddits")
@RestController
@Slf4j
public class CollegiateSubredditController extends ApiController {

    @Autowired
    CollegiateSubredditRepository collegiateSubredditRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Returns list of all subreddits in the database")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<CollegiateSubreddit> allCollegiateSubreddits(){
        Iterable<CollegiateSubreddit> subreddits = collegiateSubredditRepository.findAll();
        return subreddits;
    }

    @ApiOperation(value = "Returns info about the subreddit with ID passed as a parameter")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> subredditByID(
            @ApiParam("id") @RequestParam long id) throws JsonProcessingException{
            
        CollegiateSubreddit csr;
        ResponseEntity<String> error;
        Optional<CollegiateSubreddit> optionalCsr = collegiateSubredditRepository.findById(id);
        if(optionalCsr.isEmpty()){
            error = ResponseEntity.badRequest().body(String.format("subreddit with id %d not found", id));
            return error;
        }
        else{
            csr = optionalCsr.get();
            String body = mapper.writeValueAsString(csr);
            return ResponseEntity.ok().body(body);
        }
    }

    @ApiOperation(value = "Update a subreddit by id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("")
    public ResponseEntity<String> putCollegiateSubredditById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid CollegiateSubreddit incomingCsr) throws JsonProcessingException {


        Optional<CollegiateSubreddit> optionalCsr = collegiateSubredditRepository.findById(id);
        if(optionalCsr.isEmpty()){
            return ResponseEntity
                .badRequest()
                .body(String.format("Subreddit with id %d not found", id));
        }
        else{
            incomingCsr.setId(id);
            collegiateSubredditRepository.save(incomingCsr);
            String body = mapper.writeValueAsString(incomingCsr);
            return ResponseEntity.ok().body(body);
        }
    }



    @ApiOperation(value = "Creates a new subreddit")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public CollegiateSubreddit postCollegiateSubreddit(
            @ApiParam("name") @RequestParam String name,
            @ApiParam("location") @RequestParam String location,
            @ApiParam("subreddit") @RequestParam String subreddit){
        

        CollegiateSubreddit collegiateSubreddit = new CollegiateSubreddit();
        collegiateSubreddit.setName(name);
        collegiateSubreddit.setLocation(location);
        collegiateSubreddit.setSubreddit(subreddit);

        CollegiateSubreddit savedCollegiateSubreddit = collegiateSubredditRepository.save(collegiateSubreddit);
        return savedCollegiateSubreddit;
    }

    @ApiOperation(value = "Delete a subreddit")
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteSubreddit(
            @ApiParam("id") @RequestParam long id){
        

        ResponseEntity<String> error;
        Optional<CollegiateSubreddit> optionalCsr = collegiateSubredditRepository.findById(id);
        if(optionalCsr.isEmpty()){
            error = ResponseEntity.badRequest().body(String.format("record %d not found", id));
            return error;
        }
        else{
            collegiateSubredditRepository.deleteById(id);
            return ResponseEntity.ok().body(String.format("record %d deleted", id));
        }
    }
}