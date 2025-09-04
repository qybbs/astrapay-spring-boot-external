package com.astrapay.controller;

import com.astrapay.constant.ConstantVariable;
import com.astrapay.dto.request.NoteRequest;
import com.astrapay.dto.response.BaseResponse;
import com.astrapay.dto.response.NoteResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + "/simple-notes" + uri;
    }

    @Test
    public void testCompleteNoteWorkflow() throws Exception {

        String initialTitle = "Catatan Integration Test";
        String initialContent = "Ini adalah catatan tes untuk integration testing";
        String updatedTitle = "Catatan Integration Test terbaru";
        String updatedContent = "Catatan ini telah diubah integration testing";

        NoteRequest createRequest = new NoteRequest(initialTitle, initialContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoteRequest> createEntity = new HttpEntity<>(createRequest, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                createEntity,
                String.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        BaseResponse<NoteResponse> createBaseResponse = objectMapper.readValue(
                createResponse.getBody(),
                new TypeReference<>() {
                }
        );

        assertTrue(createBaseResponse.isSuccess());
        assertEquals("Note created successfully", createBaseResponse.getMessage());
        assertNotNull(createBaseResponse.getData());
        assertEquals(initialTitle, createBaseResponse.getData().getTitle());
        assertEquals(initialContent, createBaseResponse.getData().getContent());
        assertNotNull(createBaseResponse.getData().getId());

        String noteId = createBaseResponse.getData().getId();

        ResponseEntity<String> getAllResponse = restTemplate.getForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                String.class
        );

        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertNotNull(getAllResponse.getBody());

        BaseResponse<List<NoteResponse>> getAllBaseResponse = objectMapper.readValue(
                getAllResponse.getBody(),
                new TypeReference<>() {
                }
        );

        assertTrue(getAllBaseResponse.isSuccess());
        assertEquals("Notes retrieved successfully", getAllBaseResponse.getMessage());
        assertNotNull(getAllBaseResponse.getData());
        assertFalse(getAllBaseResponse.getData().isEmpty());

        boolean noteFound = getAllBaseResponse.getData().stream()
                .anyMatch(note -> noteId.equals(note.getId()));
        assertTrue(noteFound, "Created note should be found in the list");

        NoteRequest updateRequest = new NoteRequest(updatedTitle, updatedContent);
        HttpEntity<NoteRequest> updateEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> updateResponse = restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + noteId),
                HttpMethod.PUT,
                updateEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());

        BaseResponse<NoteResponse> updateBaseResponse = objectMapper.readValue(
                updateResponse.getBody(),
                new TypeReference<>() {
                }
        );

        assertTrue(updateBaseResponse.isSuccess());
        assertEquals("Note updated successfully", updateBaseResponse.getMessage());
        assertEquals(noteId, updateBaseResponse.getData().getId());
        assertEquals(updatedTitle, updateBaseResponse.getData().getTitle());
        assertEquals(updatedContent, updateBaseResponse.getData().getContent());

        ResponseEntity<String> getAfterUpdateResponse = restTemplate.getForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                String.class
        );

        assertEquals(HttpStatus.OK, getAfterUpdateResponse.getStatusCode());

        BaseResponse<List<NoteResponse>> getAfterUpdateBaseResponse = objectMapper.readValue(
                getAfterUpdateResponse.getBody(),
                new TypeReference<>() {
                }
        );

        NoteResponse updatedNote = getAfterUpdateBaseResponse.getData().stream()
                .filter(note -> noteId.equals(note.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(updatedNote);
        assertEquals(updatedTitle, updatedNote.getTitle());
        assertEquals(updatedContent, updatedNote.getContent());

        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + noteId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertNotNull(deleteResponse.getBody());

        BaseResponse<Void> deleteBaseResponse = objectMapper.readValue(
                deleteResponse.getBody(),
                new TypeReference<>() {
                }
        );

        assertTrue(deleteBaseResponse.isSuccess());
        assertEquals("Note deleted successfully", deleteBaseResponse.getMessage());

        ResponseEntity<String> getAfterDeleteResponse = restTemplate.getForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                String.class
        );

        assertEquals(HttpStatus.OK, getAfterDeleteResponse.getStatusCode());

        BaseResponse<List<NoteResponse>> getAfterDeleteBaseResponse = objectMapper.readValue(
                getAfterDeleteResponse.getBody(),
                new TypeReference<>() {
                }
        );

        boolean noteStillExists = getAfterDeleteBaseResponse.getData().stream()
                .anyMatch(note -> noteId.equals(note.getId()));
        assertFalse(noteStillExists, "Deleted note should not be found in the list");
    }

    @Test
    public void testCreateNoteWithValidationError() {

        NoteRequest invalidRequest = new NoteRequest("", "Konten");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoteRequest> entity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                entity,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        assertTrue(response.getBody().contains(HttpStatus.BAD_REQUEST.getReasonPhrase()) ||
                response.getBody().contains("must not be empty"));
    }

    @Test
    public void testUpdateNonExistentNote() {

        String nonExistentId = "non-existent-id-12345";
        NoteRequest updateRequest = new NoteRequest("Judul", "Konten");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NoteRequest> entity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + nonExistentId),
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        String notFound = HttpStatus.NOT_FOUND.getReasonPhrase();

        assertTrue(response.getBody().contains(notFound) ||
                response.getBody().contains(notFound.toLowerCase()));
    }

    @Test
    public void testDeleteNonExistentNote() {

        String nonExistentId = "non-existent-id-67890";

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + nonExistentId),
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        String notFound = HttpStatus.NOT_FOUND.getReasonPhrase();

        assertTrue(response.getBody().contains(notFound) ||
                response.getBody().contains(notFound.toLowerCase()));
    }

    @Test
    public void testGetAllNotesWhenEmpty() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        BaseResponse<List<NoteResponse>> baseResponse = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {
                }
        );

        assertTrue(baseResponse.isSuccess());
        assertEquals("Notes retrieved successfully", baseResponse.getMessage());
        assertNotNull(baseResponse.getData());
    }

    @Test
    public void testConcurrentNoteOperations() throws Exception {

        NoteRequest request1 = new NoteRequest("Catatan 1", "Konten 1");
        NoteRequest request2 = new NoteRequest("Catatan 2", "Konten 2");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NoteRequest> entity1 = new HttpEntity<>(request1, headers);
        HttpEntity<NoteRequest> entity2 = new HttpEntity<>(request2, headers);

        ResponseEntity<String> response1 = restTemplate.postForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT), entity1, String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT), entity2, String.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());

        BaseResponse<NoteResponse> baseResponse1 = objectMapper.readValue(
                response1.getBody(), new TypeReference<>() {
                });
        BaseResponse<NoteResponse> baseResponse2 = objectMapper.readValue(
                response2.getBody(), new TypeReference<>() {
                });

        assertNotEquals(baseResponse1.getData().getId(), baseResponse2.getData().getId());
        
        restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + baseResponse1.getData().getId()),
                HttpMethod.DELETE, null, String.class);
        restTemplate.exchange(
                createURLWithPort(ConstantVariable.NOTE_ENDPOINT + "/" + baseResponse2.getData().getId()),
                HttpMethod.DELETE, null, String.class);
    }
}
