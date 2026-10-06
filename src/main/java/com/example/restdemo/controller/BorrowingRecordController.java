package com.example.restdemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restdemo.dto.BorrowRequest;
import com.example.restdemo.model.BorrowingRecord;
import com.example.restdemo.service.BorrowingRecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingRecordService;

    @GetMapping
    public List<BorrowingRecord> getAllRecords() {
        return borrowingRecordService.getAllRecords();
    }

    @GetMapping("/{id}")
    public BorrowingRecord getRecordById(@PathVariable Long id) {
        return borrowingRecordService.getRecordById(id);
    }

    @GetMapping("/active")
    public List<BorrowingRecord> getActiveBorrowings() {
        return borrowingRecordService.getActiveBorrowings();
    }

    @GetMapping("/member/{memberId}")
    public List<BorrowingRecord> getRecordsByMemberId(@PathVariable Long memberId) {
        return borrowingRecordService.getRecordsByMemberId(memberId);
    }

    @GetMapping("/book/{bookId}")
    public List<BorrowingRecord> getRecordsByBookId(@PathVariable Long bookId) {
        return borrowingRecordService.getRecordsByBookId(bookId);
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowingRecord> borrowBook(@Valid @RequestBody BorrowRequest request) {
        BorrowingRecord record = borrowingRecordService.borrowBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowingRecord> returnBook(@PathVariable Long id) {
        BorrowingRecord record = borrowingRecordService.returnBook(id);
        return ResponseEntity.ok(record);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        borrowingRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
