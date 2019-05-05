package com.example.choi.teamhub.DTO;

public class ProjectDto {
    private int num;
    private int professor_code;
    private String professor_name;
    private String name;
    private String pw;

    public String getProfessor_name() {
        return professor_name;
    }

    public int getNum() {
        return num;
    }

    public int getProfessor_code() {
        return professor_code;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public void setProfessor_code(int professor_code) {
        this.professor_code = professor_code;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setProfessor_name(String professor_name) {
        this.professor_name = professor_name;
    }
}

