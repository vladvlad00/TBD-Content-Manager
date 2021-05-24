package ro.uaic.info.contentmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.contentmanager.entity.ContentBlock;
import ro.uaic.info.contentmanager.entity.Course;
import ro.uaic.info.contentmanager.repository.ContentBlockRepository;
import ro.uaic.info.contentmanager.repository.CourseRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @PostMapping("/")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        if(course.getId()!=null&&courseRepository.findById(course.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Course createdCourse = courseRepository.save(course);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdCourse.getId()).toUri();
        return ResponseEntity.created(uri).body(createdCourse);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Course>> listAllCourses()
    {
        Iterable<Course> foundCourses =courseRepository.findAll();
        return ResponseEntity.ok(foundCourses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> listCourse(@PathVariable Integer id)
    {
        Optional<Course> foundCourse = courseRepository.findById(id);
        if (foundCourse.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundCourse.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@RequestBody Course course, @PathVariable Integer id)
    {
        if (course.getId() == null || !course.getId().equals(id))
            return ResponseEntity.badRequest().build();

        if (courseRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Course updatedCourse = courseRepository.save(course);

        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Integer id)
    {
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Course courseObj = courseOpt.get();

        for(ContentBlock content : courseObj.getCourseContentBlocks())
            contentBlockRepository.deleteById(content.getId());

        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
