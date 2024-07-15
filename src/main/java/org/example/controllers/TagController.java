package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChangeTagDto;
import org.example.dto.CreateTagDto;
import org.example.dto.DeleteTagDto;
import org.example.dto.TagsRequestDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.TagException;
import org.example.exceptions.UserException;
import org.example.models.Tag;
import org.example.services.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/smartpro.hse.ru/epp/create/tags")
@RequiredArgsConstructor
public class TagController {

    private TagService tagService;

    @PostMapping("/add")
    private ResponseEntity<Tag> addTag(@RequestBody CreateTagDto dto){
        return new ResponseEntity<>(tagService.addTag(dto),
                HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Tag> editAdvertisement(@PathVariable Long id, @RequestBody ChangeTagDto dto) throws AccessException, TagException {
        Tag tag = tagService.changeTag(id, dto);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Tag> findTag(@PathVariable Long id) throws TagException {
        Tag tag = tagService.fingTag(id);
        return ResponseEntity.ok(tag);
    }


    @DeleteMapping("delete/{id}")
    private ResponseEntity<Tag> deleteTag(@PathVariable Long id, @RequestBody DeleteTagDto dto) throws AccessException, UserException {
        Tag tag = tagService.deleteTag(id, dto);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/all")
    private List<Tag> findTagsForUser(@RequestBody TagsRequestDto dto) throws UserException {
        return tagService.getTagsForUser(dto);
    }



}
