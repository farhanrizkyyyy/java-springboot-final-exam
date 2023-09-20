package com.example.finalexam.controllers;

import com.example.finalexam.dto.requests.MemberRequest;
import com.example.finalexam.dto.responses.MemberResponse;
import com.example.finalexam.models.ApiResponse;
import com.example.finalexam.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> getMember(@RequestParam(required = false, name = "code") String code) {
        ApiResponse response;
        Object body;
        HttpStatus httpStatus;

        if (code == null) {
            body = memberService.getMembers();
            httpStatus = HttpStatus.OK;
        } else {
            body = memberService.getMemberByCode(code);
            if (body == null) httpStatus = HttpStatus.NOT_FOUND;
            else httpStatus = HttpStatus.OK;
        }

        response = new ApiResponse(memberService.getResponseMessage(), body);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createMember(@RequestBody MemberRequest request) {
        MemberResponse newMember = memberService.createMember(request);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), newMember);
        HttpStatus httpStatus;

        if (newMember == null) httpStatus = HttpStatus.BAD_REQUEST;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse> updateMember(@PathVariable String code, @RequestBody MemberRequest request) {
        MemberResponse target = memberService.updateMember(code, request);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), target);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse> deleteMember(@PathVariable String code) {
        MemberResponse target = memberService.deleteMember(code);
        ApiResponse response = new ApiResponse(memberService.getResponseMessage(), null);
        HttpStatus httpStatus;

        if (target == null) httpStatus = HttpStatus.NOT_FOUND;
        else httpStatus = HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(response);
    }
}