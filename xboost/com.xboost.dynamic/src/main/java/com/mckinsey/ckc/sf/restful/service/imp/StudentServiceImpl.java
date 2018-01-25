package com.mckinsey.ckc.sf.restful.service.imp;

import java.util.Arrays;
import java.util.List;

import com.mckinsey.ckc.sf.restful.service.IStudentService;
import org.springframework.stereotype.Service;

import com.mckinsey.ckc.sf.restful.data.Student;

@Service
public class StudentServiceImpl implements IStudentService {

    @Override
    public List<Student> queryStudents() {
        return Arrays.asList(new Student("1001", "yinjihuan"));
    }

}