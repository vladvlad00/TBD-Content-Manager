package ro.uaic.info.contentmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.contentmanager.entity.ContentBlock;
import ro.uaic.info.contentmanager.entity.Course;
import ro.uaic.info.contentmanager.entity.Subject;
import ro.uaic.info.contentmanager.repository.ContentBlockRepository;
import ro.uaic.info.contentmanager.repository.CourseRepository;
import ro.uaic.info.contentmanager.repository.SubjectRepository;


import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/subject")
public class SubjectController
{
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContentBlockRepository contentBlockRepository;


    @PostMapping("/")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject)
    {
        if (subject.getId() != null && subjectRepository.findById(subject.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Subject createdSubject = subjectRepository.save(subject);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdSubject.getId()).toUri();
        return ResponseEntity.created(uri).body(createdSubject);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Subject>> listAllSubjects()
    {
        Iterable<Subject> foundSubjects =subjectRepository.findAll();
        return ResponseEntity.ok(foundSubjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> listSubject(@PathVariable Integer id)
    {
        Optional<Subject> foundSubject = subjectRepository.findById(id);
        if (foundSubject.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundSubject.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@RequestBody Subject subject, @PathVariable Integer id)
    {
        if (subject.getId() == null || !subject.getId().equals(id))
            return ResponseEntity.badRequest().build();

        if (subjectRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Subject updatedSubject = subjectRepository.save(subject);

        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Integer id)
    {
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        if (subjectOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Subject subjectObj = subjectOpt.get();

        if(subjectObj.getSubjectCourses() != null)
        {
            for(Course course : subjectObj.getSubjectCourses()) {
                for (ContentBlock contentBlock : course.getCourseContentBlocks())
                    contentBlockRepository.deleteById(contentBlock.getId());
                courseRepository.deleteById(course.getId());
            }

        }

        subjectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}