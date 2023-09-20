package com.example.finalexam.services;

import com.example.finalexam.dto.requests.MemberRequest;
import com.example.finalexam.dto.responses.MemberResponse;
import com.example.finalexam.models.Member;
import com.example.finalexam.repositories.MemberRepository;
import com.example.finalexam.utilities.Utility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<MemberResponse> getMembers() {
        ModelMapper mapper = new ModelMapper();
        List<Member> members = memberRepository.findAllByDeletedAtIsNullOrderByNameAsc();
        List<MemberResponse> responses = Arrays.asList(mapper.map(members, MemberResponse[].class));

        if (members.isEmpty()) responseMessage = "Member is empty";
        else responseMessage = "Fetch member success";

        return responses;
    }

    public MemberResponse getMemberByCode(String code) {
        Member result = memberRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (result != null) {
            responseMessage = "Fetch member success";
            return new MemberResponse(result);
        } else {
            responseMessage = "Cannot find member with code " + code.toUpperCase();
            return null;
        }

    }

    public MemberResponse createMember(MemberRequest request) {
        String memberName = Utility.capitalizeFirstLetter(request.getName().trim());
        boolean isPhoneValid = Utility.validatePhoneNumber(request.getPhone().trim());

        if (isPhoneValid) {
            Member newMember = new Member(generateCode(), memberName, request.getPhone(), request.getAddress());
            memberRepository.save(newMember);
            responseMessage = "Member successfully added";

            return new MemberResponse(newMember);
        } else responseMessage = "Phone must be a 10-13 digit number";

        return null;
    }

    public MemberResponse updateMember(String code, MemberRequest request) {
        Member target = memberRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());
        boolean isPhoneValid = Utility.validatePhoneNumber(request.getPhone().trim());

        if (target != null) {
            if (isPhoneValid) {
                String newMemberName = Utility.capitalizeFirstLetter(request.getName().trim());

                target.setName(newMemberName);
                target.setPhone(request.getPhone().trim());
                memberRepository.save(target);
                responseMessage = "Member successfully updated";

                return new MemberResponse(target);
            } else responseMessage = "Phone must be a 10-13 digit number";
        } else responseMessage = "Cannot find member with code " + code.toUpperCase();

        return null;
    }

    public MemberResponse deleteMember(String code) {
        Member target = memberRepository.findOneByCodeAndDeletedAtIsNull(code.toUpperCase());

        if (target != null) {
            target.setDeletedAt(new Date());
            memberRepository.save(target);
            responseMessage = "Member successfully deleted";
            return new MemberResponse(target);
        } else {
            responseMessage = "Cannot find member with code " + code.toUpperCase();
            return null;
        }
    }

    private String generateCode() {
        List<Member> members = memberRepository.findAll();

        if (members.isEmpty()) return "MM001";
        else {
            String memberIdAsString = String.valueOf(members.get(members.size() - 1).getId());
            int lastMemberId = Integer.parseInt(memberIdAsString);
            lastMemberId++;
            String codeDigit = String.format("%03d", lastMemberId);

            return "MM" + codeDigit;
        }
    }
}
