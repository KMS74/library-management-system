package com.example.restdemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restdemo.model.BorrowingRecord;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByMemberId(Long memberId);

    List<BorrowingRecord> findByBookId(Long bookId);

    List<BorrowingRecord> findByReturnDateIsNull();

}
