package edu.ucsb.cs156.example.documents.Earthquake;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureCollection {
    private String type;
    private EarthquakeMetadata metadata;
    private List<Feature> features;
    private List<Double> bbox;
}
