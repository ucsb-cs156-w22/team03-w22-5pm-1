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
    @Autowired
    EarthquakesCollection earthquakesCollection;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Get earthquakes a certain distance from UCSB's Storke Tower", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PostMapping("/retrieve")
    public ResponseEntity<String> postEarthquakeFeature(
        @ApiParam("distance in km, e.g. 100") @RequestParam String distance,
        @ApiParam("minimum magnitude, e.g. 2.5") @RequestParam String minMag
    ) throws JsonProcessingException {
        log.info("getEarthquakes: distance={} minMag={}", distance, minMag);
        String result = earthquakeQueryService.getJSON(distance, minMag);
        
        // Store result as Feature Collection Object
        FeatureCollection collection = mapper.readValue(result, FeatureCollection.class);

        // Get all features from Feature Collection Object
        List<Feature> features = collection.getFeatures();

        // Store all features in MongoDB collection
        List<Feature> storedFeatures = earthquakesCollection.saveAll(features);

        return ResponseEntity.ok().body(storedFeatures);
    }

}
