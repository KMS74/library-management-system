package com.example.restdemo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restdemo.dto.BookDTO;
import com.example.restdemo.dto.BorrowRequest;
import com.example.restdemo.dto.MemberDTO;
import com.example.restdemo.exception.BadRequestException;
import com.example.restdemo.model.Book;
import com.example.restdemo.model.BorrowingRecord;
import com.example.restdemo.model.Member;
import com.example.restdemo.service.BookService;
import com.example.restdemo.service.BorrowingRecordService;
import com.example.restdemo.service.MemberService;

@SpringBootTest
class BorrowingFlowIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Test
    void testFullBorrowAndReturnFlow() {
        // 1. Create a Book with 1 available copy
        Book book = bookService.createBook(BookDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .publicationYear(2008)
                .genre("Software Engineering")
                .availableCopies(1)
                .build());
        assertNotNull(book.getId());
        assertEquals(1, book.getAvailableCopies());

        // 2. Create a Member
        Member member = memberService.createMember(MemberDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .startDate(LocalDate.now())
                .build());
        assertNotNull(member.getId());

        // 3. Borrow the book
        BorrowRequest request = BorrowRequest.builder()
                .bookId(book.getId())
                .memberId(member.getId())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        BorrowingRecord record = borrowingRecordService.borrowBook(request);
        assertNotNull(record.getId());
        assertNotNull(record.getBorrowDate());
        assertNull(record.getReturnDate());

        // Verify book copies decreased
        Book borrowedBook = bookService.getBookById(book.getId());
        assertEquals(0, borrowedBook.getAvailableCopies());

        // 4. Try borrowing when copies == 0 -> should throw BadRequestException
        assertThrows(BadRequestException.class, () -> borrowingRecordService.borrowBook(request));

        // 5. Return the book
        BorrowingRecord returnedRecord = borrowingRecordService.returnBook(record.getId());
        assertNotNull(returnedRecord.getReturnDate());

        // Verify book copies increased back to 1
        Book returnedBook = bookService.getBookById(book.getId());
        assertEquals(1, returnedBook.getAvailableCopies());

        // 6. Try returning the same record again -> should throw BadRequestException
        assertThrows(BadRequestException.class, () -> borrowingRecordService.returnBook(record.getId()));

        // 7. Verify queries
        List<BorrowingRecord> memberRecords = borrowingRecordService.getRecordsByMemberId(member.getId());
        assertEquals(1, memberRecords.size());
    }
}
