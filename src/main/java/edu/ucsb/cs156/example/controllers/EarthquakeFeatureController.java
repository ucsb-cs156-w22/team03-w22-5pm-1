package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.Earthquake.Feature;
import edu.ucsb.cs156.example.documents.Earthquake.FeatureCollection;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "Get Earthquake info from USGS")
@RequestMapping("/api/earthquakes")
@RestController
@Slf4j
public class EarthquakeFeatureController extends ApiController{
    @Autowired
    EarthquakeQueryService earthquakeQueryService;

    @Autowired
    EarthquakesCollection earthquakesCollection;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Post earthquakes a certain distance from UCSB's Storke Tower to the MongoDB", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/retrieve")
    public List<Feature> postEarthquakeFeature(
        @ApiParam("distance in km, e.g. 100") @RequestParam String distance,
        @ApiParam("minimum magnitude, e.g. 2.5") @RequestParam String minMag
    ) throws JsonProcessingException {
        log.info("getEarthquakes: distance={} minMag={}", distance, minMag);
        String result = earthquakeQueryService.getJSON(distance, minMag);
        
        FeatureCollection collection = mapper.readValue(result, FeatureCollection.class);

        List<Feature> features = collection.getFeatures();

        List<Feature> storedFeatures = earthquakesCollection.saveAll(features);

        return storedFeatures;
    }

    
    @ApiOperation(value = "Purge all Earthquake feature objects in the MongoDB collection", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/purge")
    public void purgeEarthquakeFeatures() throws JsonProcessingException {      
        earthquakesCollection.deleteAll();
    }

    @ApiOperation(value = "Get all Earthquake feature objects in the MongoDB collection", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<Feature> getEarthquakeFeatures() throws JsonProcessingException {          
        return earthquakesCollection.findAll();
    }

}
