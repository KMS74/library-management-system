package com.example.restdemo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restdemo.dto.BorrowRequest;
import com.example.restdemo.exception.BadRequestException;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.model.Book;
import com.example.restdemo.model.BorrowingRecord;
import com.example.restdemo.model.Member;
import com.example.restdemo.repository.BookRepository;
import com.example.restdemo.repository.BorrowingRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final MemberService memberService;

    public List<BorrowingRecord> getAllRecords() {
        return borrowingRecordRepository.findAll();
    }

    public BorrowingRecord getRecordById(Long id) {
        return borrowingRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found with id: " + id));
    }

    public List<BorrowingRecord> getRecordsByMemberId(Long memberId) {
        return borrowingRecordRepository.findByMemberId(memberId);
    }

    public List<BorrowingRecord> getRecordsByBookId(Long bookId) {
        return borrowingRecordRepository.findByBookId(bookId);
    }

    public List<BorrowingRecord> getActiveBorrowings() {
        return borrowingRecordRepository.findByReturnDateIsNull();
    }

    @Transactional
    public BorrowingRecord borrowBook(BorrowRequest request) {
        Book book = bookService.getBookById(request.getBookId());
        Member member = memberService.getMemberById(request.getMemberId());

        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("No available copies to borrow for book: " + book.getTitle());
        }

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(request.getDueDate())
                .returnDate(null)
                .build();

        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long recordId) {
        BorrowingRecord record = getRecordById(recordId);

        if (record.getReturnDate() != null) {
            throw new BadRequestException("Book has already been returned on: " + record.getReturnDate());
        }

        record.setReturnDate(LocalDate.now());

        // Increment available copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long id) {
        BorrowingRecord record = getRecordById(id);
        borrowingRecordRepository.delete(record);
    }
}
