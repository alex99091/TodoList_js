package com.todo.backend.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.todo.backend.model.Todo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final Firestore db = FirestoreClient.getFirestore();

    // ✅ CREATE
    @PostMapping
    public Todo addTodo(@RequestBody Todo todo) throws Exception {
        DocumentReference docRef = db.collection("todos").document();
        todo.setId(docRef.getId());
        ApiFuture<WriteResult> result = docRef.set(todo);
        System.out.println("✅ Saved at: " + result.get().getUpdateTime());
        return todo;
    }

    // ✅ READ
    @GetMapping
    public List<Todo> getTodos() throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection("todos").get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        return docs.stream()
                .map(doc -> doc.toObject(Todo.class))
                .collect(Collectors.toList());
    }

    // ✅ UPDATE (완료 상태 토글 또는 수정)
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateTodo(@PathVariable String id, @RequestBody Todo updatedTodo) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("todos").document(id);
        ApiFuture<WriteResult> result = docRef.set(updatedTodo); // 전체 덮어쓰기
        return ResponseEntity.ok("Updated at: " + result.get().getUpdateTime());
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("todos").document(id);
        ApiFuture<WriteResult> result = docRef.delete();
        return ResponseEntity.ok("Deleted at: " + result.get().getUpdateTime());
    }
}
