package com.todo.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.backend.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FirebaseApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void todoPostTest() {
        // given
        Todo todo = new Todo();
        todo.setContent("🔥 테스트용 할 일");
        todo.setDone(false);
        todo.setDate(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Todo> request = new HttpEntity<>(todo, headers);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("/api/todos", request, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("✅ 응답: " + response.getBody());
    }

    @Test
    void todoGetTest() {
        // when
        ResponseEntity<Todo[]> response = restTemplate.getForEntity("/api/todos", Todo[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);

        System.out.println("✅ 조회된 할 일 개수: " + response.getBody().length);
        for (Todo todo : response.getBody()) {
            System.out.println("📄 " + todo);
        }
    }

    @Test
    void todoDeleteTest() {
        // 먼저 새 할 일 등록
        Todo todo = new Todo();
        todo.setContent("🗑 삭제 테스트용");
        todo.setDone(false);
        todo.setDate(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Todo> request = new HttpEntity<>(todo, headers);

        ResponseEntity<Todo> createRes = restTemplate.postForEntity("/api/todos", request, Todo.class);
        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        String id = createRes.getBody().getId();
        assertThat(id).isNotBlank();

        // 삭제 요청
        restTemplate.delete("/api/todos/" + id);
        System.out.println("🗑 삭제 완료: " + id);
    }

    @Test
    void todoUpdateTest() {
        // 새 할 일 등록
        Todo todo = new Todo();
        todo.setContent("✅ 수정 테스트용");
        todo.setDone(false);
        todo.setDate(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Todo> createRequest = new HttpEntity<>(todo, headers);

        ResponseEntity<Todo> createRes = restTemplate.postForEntity("/api/todos", createRequest, Todo.class);
        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        Todo created = createRes.getBody();
        created.setDone(true); // ✅ 상태 변경

        HttpEntity<Todo> updateRequest = new HttpEntity<>(created, headers);
        ResponseEntity<String> updateRes = restTemplate.exchange("/api/todos/" + created.getId(), HttpMethod.PATCH, updateRequest, String.class);

        assertThat(updateRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("✅ 수정 완료: " + updateRes.getBody());
    }


}
