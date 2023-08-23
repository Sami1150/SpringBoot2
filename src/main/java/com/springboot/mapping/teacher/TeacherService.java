package com.springboot.mapping.teacher;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.springboot.mapping.teacher.Teacher;
import com.springboot.mapping.teacher.TeacherRepository;

@Service
public class TeacherService {
    private final TeacherRepository repository;

    public TeacherService(TeacherRepository repository) {
        this.repository = repository;
    }
    public List<Teacher> findAll() {

        return repository.findAll();
    }

    public Teacher findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Teacher create(Teacher teacher) {

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
