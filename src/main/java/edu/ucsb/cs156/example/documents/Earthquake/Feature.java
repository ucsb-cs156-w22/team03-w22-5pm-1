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
public class Feature {
    private String id;
    private String type;
    private FeatureProperties properties;
    private FeatureGeometry geometry;
}
