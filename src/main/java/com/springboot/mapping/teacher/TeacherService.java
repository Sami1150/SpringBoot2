package com.springboot.mapping.teacher;

import org.springframework.stereotype.Service;
import com.springboot.mapping.teacher.TeacherRepository;
import java.util.List;

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
//        if (teacher.getId() != null) && (teacher.getId()!=repository.existsById(teacher.getId()))) {
//            return null;
//        }
        teacher.setId((int) System.currentTimeMillis());
        return repository.save(teacher);
    }
}
