package com.springboot.mapping.teacher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository repository;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${teacher.default.course}")
    private String defaultCourse;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }
    public List<Teacher> findAll() {
        logger.debug("Fetching all teachers");
        return repository.findAll();
//        return repository.findAllByCourse(defaultCourse);
    }

    public List<Teacher> findAllByCourse(String course) {
        logger.debug("Searching teacher with course: {}", course);
        return repository.findAllByCourse(course);
    }

    public List<Teacher> findByCourseContaining(String course) {
        if (course == null) {
            course = defaultCourse;
        }

        logger.debug("Searching teacher with course containing: {}", course);
        return repository.findByCourseContaining(course);
    }


    public Teacher findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Teacher create(Teacher teacher) {

        logger.info("Teacher with id {} is added. ", teacher.getId());

        return repository.save(teacher);
    }

    public Teacher update(long id, Teacher newTeacherData) {
        Optional<Teacher> existingTeacher = repository.findById(id);

        if (existingTeacher.isPresent()) {
            Teacher teacherToUpdate = existingTeacher.get();
            teacherToUpdate.setCourse(newTeacherData.getCourse());
            teacherToUpdate.setSalary(newTeacherData.getSalary());
            // You can update other properties as needed

            return repository.save(teacherToUpdate);
        }

        return null; // Teacher with given id not found
    }

    public boolean delete(long id) {
        Optional<Teacher> teacherToDelete = repository.findById(id);

        if (teacherToDelete.isPresent()) {
            repository.delete(teacherToDelete.get());
            return true;
        }

        return false; // Teacher with given id not found
    }

}
