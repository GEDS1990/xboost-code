package com.mckinsey.ckc.sf.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mckinsey.ckc.sf.restful.service.IStudentService;

@RestController
@EnableAutoConfiguration
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @RequestMapping("/students")
    Object queryStudents() {
        return studentService.queryStudents();
    }
}