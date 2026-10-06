package com.example.restdemo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.restdemo.dto.MemberDTO;
import com.example.restdemo.exception.ResourceNotFoundException;
import com.example.restdemo.model.Member;
import com.example.restdemo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @Transactional
    public Member createMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phoneNumber(memberDTO.getPhoneNumber())
                .startDate(memberDTO.getStartDate())
                .endDate(memberDTO.getEndDate())
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long id, MemberDTO memberDTO) {
        Member existingMember = getMemberById(id);
        existingMember.setName(memberDTO.getName());
        existingMember.setEmail(memberDTO.getEmail());
        existingMember.setPhoneNumber(memberDTO.getPhoneNumber());
        existingMember.setStartDate(memberDTO.getStartDate());
        existingMember.setEndDate(memberDTO.getEndDate());
        return memberRepository.save(existingMember);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}
