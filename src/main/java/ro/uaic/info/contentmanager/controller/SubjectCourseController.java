package ro.uaic.info.contentmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.contentmanager.entity.Course;
import ro.uaic.info.contentmanager.entity.Subject;
import ro.uaic.info.contentmanager.repository.CourseRepository;
import ro.uaic.info.contentmanager.repository.SubjectRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/subject_course")
public class SubjectCourseController {
    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    CourseRepository courseRepository;

    @PostMapping("/")
    public ResponseEntity<Map<String, Integer>> createSubjectCourse(@RequestBody Map<String,Integer> subjectCourse){
        Integer subjectId=subjectCourse.get("subjectId");
        Integer courseId=subjectCourse.get("courseId");

        if(subjectId == null || courseId == null){
            return ResponseEntity.badRequest().build();
        }

        Optional<Subject> subjectOpt=subjectRepository.findById(subjectId);
        Optional<Course> courseOpt=courseRepository.findById(courseId);

        if(subjectOpt.isEmpty() || courseOpt.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Subject subject=subjectOpt.get();
        Course course=courseOpt.get();

        if(subject.getSubjectCourses().contains(course)&&
        course.getSubject().equals(subject))
            return ResponseEntity.badRequest().build();

        subject.getSubjectCourses().add(course);
        course.setSubject(subject);

        subjectRepository.save(subject);
        courseRepository.save(course);

        URI uri= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{subjectId}/{courseId}").buildAndExpand(subjectId,courseId).toUri();
        return ResponseEntity.created(uri).body(subjectCourse);
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<Iterable<Course>> listBySubjectId(@PathVariable Integer id){
        Optional<Subject> foundCourse=subjectRepository.findById(id);
        if(foundCourse.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foundCourse.get().getSubjectCourses());
    }

    @DeleteMapping("/subject/{subjectId}/course/{courseId}")
    public ResponseEntity<Course> deleteSubjectCourse(@PathVariable Integer subjectId,@PathVariable Integer courseId){
        Optional<Subject> subjectOpt=subjectRepository.findById(subjectId);
        Optional<Course> courseOpt=courseRepository.findById(courseId);

        if(subjectOpt.isEmpty()||courseOpt.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Subject subject=subjectOpt.get();
        Course course=courseOpt.get();

        if(!subject.getSubjectCourses().contains(course)&&
         course.getSubject().equals(subject)){
            return ResponseEntity.badRequest().build();
        }
    }
}
