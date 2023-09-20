package com.example.finalexam.seeders;

import com.example.finalexam.models.Member;
import com.example.finalexam.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MemberSeeder {
    @Autowired
    private MemberRepository memberRepository;

    @PostConstruct
    public void seed() {
        if (memberRepository.findAll().isEmpty()) {
            List<Member> members = new ArrayList<>(Arrays.asList(
                    new Member("MM001", "Member 1", "123123123", "Bandung"),
                    new Member("MM002", "Member 2", "123123123", "Bandung"),
                    new Member("MM003", "Member 3", "123123123", "Bandung"),
                    new Member("MM004", "Member 4", "123123123", "Bandung"),
                    new Member("MM005", "Member 5", "123123123", "Bandung"),
                    new Member("MM006", "Member 6", "123123123", "Bandung"),
                    new Member("MM007", "Member 7", "123123123", "Bandung"),
                    new Member("MM008", "Member 8", "123123123", "Bandung"),
                    new Member("MM009", "Member 9", "123123123", "Bandung"),
                    new Member("MM010", "Member 10", "123123123", "Bandung")
            ));

            memberRepository.saveAll(members);
        }
    }
}
