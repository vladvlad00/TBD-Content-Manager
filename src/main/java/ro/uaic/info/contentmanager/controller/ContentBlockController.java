package ro.uaic.info.contentmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.contentmanager.entity.ContentBlock;
import ro.uaic.info.contentmanager.repository.ContentBlockRepository;
import ro.uaic.info.contentmanager.repository.CourseRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/content_block")
public class ContentBlockController {
    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/")
    public ResponseEntity<ContentBlock> createContentBlock(@RequestBody Map<String,String> contentBlockJson)
    {
        Integer contentBlockId;
        Integer courseId;
        String type;
        String content;
        Integer position;

        try
        {
            contentBlockId = contentBlockJson.get("contentBlockId") != null ?
                    Integer.parseInt(contentBlockJson.get("contentBlockId")) : null;
            courseId = Integer.parseInt(contentBlockJson.get("courseId"));
            type = contentBlockJson.get("type");
            content = contentBlockJson.get("content");
            position = contentBlockJson.get("position") != null ?
                    Integer.parseInt(contentBlockJson.get("position")) : null;
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (type == null || content == null)
            return ResponseEntity.badRequest().build();

        if (contentBlockId != null && contentBlockRepository.findById(contentBlockId).isPresent())
            return ResponseEntity.badRequest().build();

        var courseOpt = courseRepository.findById(courseId);

        if (courseOpt.isEmpty())
            return ResponseEntity.notFound().build();

        var courseObj = courseOpt.get();
        if (position == null)
            position = courseObj.getCourseContentBlocks().size();

        if (position < 0 || position > courseObj.getCourseContentBlocks().size())
            return ResponseEntity.badRequest().build();

        var contentBlock = new ContentBlock(type, content, courseObj);
        courseObj.getCourseContentBlocks().add(position, contentBlock);

        ContentBlock createdBlock = contentBlockRepository.save(contentBlock);
        courseRepository.save(courseObj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdBlock.getId()).toUri();
        return ResponseEntity.created(uri).body(createdBlock);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<ContentBlock>> listAllContentBlocks() {
        Iterable<ContentBlock> foundContentBlocks = contentBlockRepository.findAll();
        return ResponseEntity.ok(foundContentBlocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentBlock> listContentBlock(@PathVariable Integer id) {
        Optional<ContentBlock> foundContentBlock = contentBlockRepository.findById(id);
        if (foundContentBlock.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundContentBlock.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentBlock> updateContentBlock(@RequestBody ContentBlock contentBlock, @PathVariable Integer id) {
        if (contentBlock.getId() == null || !contentBlock.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        if (contentBlockRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ContentBlock updatedContentBlock = contentBlockRepository.save(contentBlock);

        return ResponseEntity.ok(updatedContentBlock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ContentBlock> deleteContentBlock(@PathVariable Integer id) {
        if (contentBlockRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        contentBlockRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
