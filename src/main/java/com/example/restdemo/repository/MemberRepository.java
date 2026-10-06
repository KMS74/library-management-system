package com.example.restdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restdemo.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
