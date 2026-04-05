package io.github.ojoilesanmi.dtoforge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DtoGeneratorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void generatesRecordSuccessfully() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"name\\": \\"John\\", \\"age\\": 30}",
                    "rootClassName": "UserDto",
                    "outputStyle": "RECORD"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("public record UserDto(")));
    }

    @Test
    void generatesClassSuccessfully() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"name\\": \\"John\\", \\"age\\": 30}",
                    "rootClassName": "UserDto",
                    "outputStyle": "CLASS"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("public class UserDto {")));
    }

    @Test
    void returnsBadRequestForInvalidJson() throws Exception {
        String requestBody = """
                {
                    "json": "not valid json",
                    "rootClassName": "TestDto",
                    "outputStyle": "RECORD"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsBadRequestForMissingFields() throws Exception {
        String requestBody = """
                {
                    "json": "{}",
                    "rootClassName": "",
                    "outputStyle": "RECORD"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsBadRequestForInvalidOutputStyle() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"name\\": \\"John\\"}",
                    "rootClassName": "TestDto",
                    "outputStyle": "FOO"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsBadRequestForInvalidOutputStylePattern() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"name\\": \\"John\\"}",
                    "rootClassName": "TestDto",
                    "outputStyle": "xml"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generatesWithJacksonAnnotations() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"user_name\\": \\"John\\"}",
                    "rootClassName": "UserDto",
                    "outputStyle": "RECORD",
                    "useJacksonAnnotations": true
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("@JsonProperty(\"user_name\")")));
    }

    @Test
    void generatesNestedTypes() throws Exception {
        String requestBody = """
                {
                    "json": "{\\"name\\": \\"John\\", \\"address\\": {\\"city\\": \\"London\\", \\"country\\": \\"UK\\"}}",
                    "rootClassName": "UserDto",
                    "outputStyle": "RECORD"
                }
                """;

        mockMvc.perform(post("/api/v1/dto-generator/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Address")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("city")));
    }
}
