package com.example.choi.teamhub.DTO;

public class TeamDto {
    private int num;
    private int makeProject_num;
    private String student_id;
    private String name;
    private String pw;
    private String student_name;

    public int getNum() { return num; }

    public String getStringNum() {
        String tmp = String.valueOf(num);
        return tmp;
    }

    public int getMakeProject_num() { return makeProject_num; }

    public String getStudent_id() { return student_id; }

    public String getName() { return name; }

    public String getPw() { return pw; }

    public String getStudent_name() { return student_name;
    }
    public void setNum(int num) { this.num = num; }

    public void setMakeProject_num(int makeProject_num) { this.makeProject_num = makeProject_num; }

    public void setStudent_id(String student_id) { this.student_id = student_id; }

    public void setName(String name) { this.name = name; }

    public void setPw(String pw) { this.pw = pw; }

    public void setStudent_name(String student_name) { this.student_name = student_name; }

}
