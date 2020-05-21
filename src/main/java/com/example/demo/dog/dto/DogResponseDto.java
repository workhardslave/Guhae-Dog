package com.example.demo.dog.dto;


import com.example.demo.member.vo.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DogResponseDto {
    private Long id;
    private Member member;
    private String name;
    private String age;
    private String gender;
    private String birth;
    private String value;


    public DogResponseDto(Dog entity) {
        this.id = entity.getId();
        this.member = entity.getMember();
        this.name = entity.getName();
        this.age = entity.getAge();
        this.gender = entity.getGender();
        this.birth = entity.getBirth();
        this.value = entity.getValue();
    }
}


