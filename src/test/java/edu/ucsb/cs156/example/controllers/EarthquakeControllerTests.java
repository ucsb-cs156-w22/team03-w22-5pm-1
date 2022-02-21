package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.collections.EarthquakesCollection;
import edu.ucsb.cs156.example.documents.Earthquake.EarthquakeMetadata;
import edu.ucsb.cs156.example.documents.Earthquake.Feature;
import edu.ucsb.cs156.example.documents.Earthquake.FeatureCollection;
import edu.ucsb.cs156.example.documents.Earthquake.FeatureGeometry;
import edu.ucsb.cs156.example.documents.Earthquake.FeatureProperties;

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
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EarthquakeFeatureController.class)
@Import(TestConfig.class)
public class EarthquakeControllerTests extends ControllerTestCase{
    
    @MockBean
    EarthquakesCollection earthquakesCollection;

    @MockBean
    EarthquakeQueryService earthquakeQueryService;

    @MockBean
    UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_retrieve_earthquake_features_returns_post() throws Exception {
            FeatureProperties fp = FeatureProperties.builder()
                            .mag(69d)
                            .place("Timbaktu")
                            .time(123456789d)
                            .url("no_url")
                            .detail("no_detail")
                            .felt(1)
                            .type("Properties")
                            .title("Earthquake?")
                            .status("it_happened")
                            .build();

            FeatureGeometry fg = FeatureGeometry.builder()
                            .type("Geometry")
                            .coordinates( new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            Feature f = Feature.builder()
                            ._id("")
                            .id("123456")
                            .type("Feature")
                            .properties(fp)
                            .geometry(fg)
                            .build();

            List<Feature> fpa = new ArrayList<>() {{add(f);}};

            EarthquakeMetadata md = EarthquakeMetadata.builder()
                            .generated(123d)
                            .url("")
                            .title("metadata")
                            .status(200)
                            .api("")
                            .count(1)
                            .build();

            FeatureCollection fc = FeatureCollection.builder()
                            .type("FeatureCollection")
                            .metadata(md)
                            .features(fpa)
                            .bbox(new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            String magnitude = "123";
            String dis = "123";

            when(earthquakeQueryService.getJSON(dis, magnitude)).thenReturn(mapper.writeValueAsString(fc));
            when(earthquakesCollection.saveAll(fpa)).thenReturn(fpa);

            // act
            MvcResult response = mockMvc.perform(post(String.format("/api/earthquakes/retrieve?distance=%s&minMag=%s", dis, magnitude))
                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            // assert

            verify(earthquakesCollection, times(1)).saveAll(fpa);
            verify(earthquakeQueryService, times(1)).getJSON(dis, magnitude);
            String expectedJson = mapper.writeValueAsString(fpa);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_retrieve_earthquake_features_returns_403_because_logged_in_as_user() throws Exception {
            FeatureProperties fp = FeatureProperties.builder()
                            .mag(69d)
                            .place("Timbaktu")
                            .time(123456789d)
                            .url("no_url")
                            .detail("no_detail")
                            .felt(1)
                            .type("Properties")
                            .title("Earthquake?")
                            .status("it_happened")
                            .build();

            FeatureGeometry fg = FeatureGeometry.builder()
                            .type("Geometry")
                            .coordinates( new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            Feature f = Feature.builder()
                            ._id("")
                            .id("123456")
                            .type("Feature")
                            .properties(fp)
                            .geometry(fg)
                            .build();

            List<Feature> fpa = new ArrayList<>() {{add(f);}};

            EarthquakeMetadata md = EarthquakeMetadata.builder()
                            .generated(123d)
                            .url("")
                            .title("metadata")
                            .status(200)
                            .api("")
                            .count(1)
                            .build();

            FeatureCollection fc = FeatureCollection.builder()
                            .type("FeatureCollection")
                            .metadata(md)
                            .features(fpa)
                            .bbox(new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            String magnitude = "123";
            String dis = "123";

            when(earthquakeQueryService.getJSON(dis, magnitude)).thenReturn(mapper.writeValueAsString(fc));
            when(earthquakesCollection.saveAll(fpa)).thenReturn(fpa);

            // act
            MvcResult response = mockMvc.perform(post(String.format("/api/earthquakes/retrieve?distance=%s&minMag=%s", dis, magnitude))
                            .with(csrf()))
                            .andExpect(status().is(403)).andReturn();
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_purge_earthquake_features_returns_void() throws Exception {

            FeatureProperties fp = FeatureProperties.builder()
                            .mag(69d)
                            .place("Timbaktu")
                            .time(123456789d)
                            .url("no_url")
                            .detail("no_detail")
                            .felt(1)
                            .type("Properties")
                            .title("Earthquake?")
                            .status("it_happened")
                            .build();

            FeatureGeometry fg = FeatureGeometry.builder()
                            .type("Geometry")
                            .coordinates( new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            Feature f = Feature.builder()
                            ._id("")
                            .id("123456")
                            .type("Feature")
                            .properties(fp)
                            .geometry(fg)
                            .build();

            List<Feature> fpa = new ArrayList<>() {{add(f);}};

            EarthquakeMetadata md = EarthquakeMetadata.builder()
                            .generated(123d)
                            .url("")
                            .title("metadata")
                            .status(200)
                            .api("")
                            .count(1)
                            .build();

            FeatureCollection fc = FeatureCollection.builder()
                            .type("FeatureCollection")
                            .metadata(md)
                            .features(fpa)
                            .bbox(new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            mockMvc.perform(post("/api/earthquakes/purge")
                            .with(csrf()))
                            .andExpect(status().isOk()).andReturn();

            verify(earthquakesCollection, times(1)).deleteAll();
    }   

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_purge_earthquake_features_returns_403_because_logged_in_as_user() throws Exception {

            FeatureProperties fp = FeatureProperties.builder()
                            .mag(69d)
                            .place("Timbaktu")
                            .time(123456789d)
                            .url("no_url")
                            .detail("no_detail")
                            .felt(1)
                            .type("Properties")
                            .title("Earthquake?")
                            .status("it_happened")
                            .build();

            FeatureGeometry fg = FeatureGeometry.builder()
                            .type("Geometry")
                            .coordinates( new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            Feature f = Feature.builder()
                            ._id("")
                            .id("123456")
                            .type("Feature")
                            .properties(fp)
                            .geometry(fg)
                            .build();

            List<Feature> fpa = new ArrayList<>() {{add(f);}};

            EarthquakeMetadata md = EarthquakeMetadata.builder()
                            .generated(123d)
                            .url("")
                            .title("metadata")
                            .status(200)
                            .api("")
                            .count(1)
                            .build();

            FeatureCollection fc = FeatureCollection.builder()
                            .type("FeatureCollection")
                            .metadata(md)
                            .features(fpa)
                            .bbox(new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            mockMvc.perform(post("/api/earthquakes/purge")
                            .with(csrf()))
                            .andExpect(status().is(403)).andReturn();
    }   

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void api_get_earthquake_features_returns_get() throws Exception {

            FeatureProperties fp = FeatureProperties.builder()
                            .mag(69d)
                            .place("Timbaktu")
                            .time(123456789d)
                            .url("no_url")
                            .detail("no_detail")
                            .felt(1)
                            .type("Properties")
                            .title("Earthquake?")
                            .status("it_happened")
                            .build();

            FeatureGeometry fg = FeatureGeometry.builder()
                            .type("Geometry")
                            .coordinates( new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            Feature f = Feature.builder()
                            ._id("")
                            .id("123456")
                            .type("Feature")
                            .properties(fp)
                            .geometry(fg)
                            .build();

            List<Feature> fpa = new ArrayList<>() {{add(f);}};

            EarthquakeMetadata md = EarthquakeMetadata.builder()
                            .generated(123d)
                            .url("")
                            .title("metadata")
                            .status(200)
                            .api("")
                            .count(1)
                            .build();

            FeatureCollection fc = FeatureCollection.builder()
                            .type("FeatureCollection")
                            .metadata(md)
                            .features(fpa)
                            .bbox(new ArrayList<Double>(){{
                                add(1d);
                                add(2d);
                                add(3d);
                                  }})
                            .build();

            when(earthquakesCollection.findAll()).thenReturn(fpa);

            MvcResult response = mockMvc.perform(get("/api/earthquakes/all"))
                            .andExpect(status().isOk()).andReturn();

            verify(earthquakesCollection, times(1)).findAll();
            String expectedJson = mapper.writeValueAsString(fpa);
            String responseString = response.getResponse().getContentAsString();
            assertEquals(expectedJson, responseString);
    }    
}
