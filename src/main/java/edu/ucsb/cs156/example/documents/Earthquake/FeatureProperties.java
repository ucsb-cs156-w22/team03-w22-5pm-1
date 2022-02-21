package edu.ucsb.cs156.example.documents.Earthquake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureProperties { 
    private Double mag;
    private String place;
    private Double time;
    private String url;
    private String detail;
    private int felt;
    private String type;
    private String title;
    private String status;
}
