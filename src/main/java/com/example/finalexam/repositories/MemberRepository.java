package com.example.finalexam.repositories;

import com.example.finalexam.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByDeletedAtIsNullOrderByNameAsc();

    Member findOneByCodeAndDeletedAtIsNull(String code);

    Member findOneByIdAndDeletedAtIsNull(Long id);
}
