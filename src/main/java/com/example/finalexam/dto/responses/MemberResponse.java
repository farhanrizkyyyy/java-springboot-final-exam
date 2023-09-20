package com.example.finalexam.dto.responses;

import com.example.finalexam.models.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    public MemberResponse(Member member) {
        this.code = member.getCode();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.address = member.getAddress();
    }
}
