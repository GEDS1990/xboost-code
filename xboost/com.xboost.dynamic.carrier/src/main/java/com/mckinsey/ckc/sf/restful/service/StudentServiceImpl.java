package com.mckinsey.ckc.sf.restful.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mckinsey.ckc.sf.restful.data.Student;

@Service
public class StudentServiceImpl implements IStudentService {

    @Override
    public List<Student> queryStudents() {
        return Arrays.asList(new Student("1001", "yinjihuan"));
    }

}