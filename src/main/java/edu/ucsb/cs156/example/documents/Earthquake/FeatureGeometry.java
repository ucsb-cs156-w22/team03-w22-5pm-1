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
public class FeatureGeometry {
    private String type;
    private List<Double> coordinates;
}
