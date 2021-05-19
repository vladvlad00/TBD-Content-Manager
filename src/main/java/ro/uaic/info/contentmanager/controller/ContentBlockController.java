package ro.uaic.info.contentmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.contentmanager.entity.ContentBlock;
import ro.uaic.info.contentmanager.repository.ContentBlockRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/content_block")
public class ContentBlockController {
    @Autowired
    private ContentBlockRepository contentBlockRepository;

    @PostMapping("/")
    public ResponseEntity<ContentBlock> createContentBlock(@RequestBody ContentBlock contentBlock) {
        if (contentBlock.getId() != null && contentBlockRepository.findById(contentBlock.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        ContentBlock createdBlock = contentBlockRepository.save(contentBlock);

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
