package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.Earthquake.Feature;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "Earthquake info from USGS")
@RequestMapping("/api/earthquakes")
@RestController
@Slf4j
public class EarthquakeFeatureController extends ApiController{
    
}
