package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.ChangeTagDto;
import org.example.dto.CreateTagDto;
import org.example.dto.DeleteTagDto;
import org.example.dto.TagsRequestDto;
import org.example.exceptions.AccessException;
import org.example.exceptions.TagException;
import org.example.exceptions.UserException;
import org.example.models.Role;
import org.example.models.Tag;
import org.example.models.User;
import org.example.repositories.TagRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TagService {

    private TagRepository tagRepository;
    private UserRepository userRepository;

    public Tag addTag(CreateTagDto dto){
        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setCreatorId(dto.getCreator().getId());

        tagRepository.save(tag);

        return tag;

    }

    public Tag changeTag(Long id, ChangeTagDto dto) throws AccessException, TagException {

        Optional<User> user = userRepository.findById(dto.getUserId());
        if (user.isEmpty() || user.get().getRole() != Role.COMPANY){
            throw new AccessException("Company role needed to change this tag!");
        }

        Optional<Tag> isPresent = tagRepository.findById(id);

        if (isPresent.isEmpty()) throw new TagException("No tag with id " + id);

        Tag tag = isPresent.get();
        tag.setName(dto.getNewName());

        tagRepository.save(tag);

        return tag;
    }

    public Tag deleteTag(Long id, DeleteTagDto dto) throws AccessException, UserException {

        Optional<User> user = userRepository.findById(dto.getUserId());
        if (user.isEmpty() || user.get().getRole() != Role.COMPANY){
            throw new AccessException("Company role needed to change this tag!");
        }

        Optional<Tag> isPresent = tagRepository.findById(id);

        if (isPresent.isEmpty()) throw new UserException("No tag with id " + id);

        Tag tag = isPresent.get();
        tagRepository.delete(tag);

        return tag;

    }

    public ArrayList<Tag> getTagsForUser(TagsRequestDto dto) throws UserException {

        Optional<User> user = userRepository.findById(dto.getUserId());
        if (user.isEmpty()){
            throw new UserException("No such user with id " + dto.getUserId());
        }

        List<Tag> tags = tagRepository.findAll();
        ArrayList<Tag> tagsForUser = new ArrayList<>();

        for (Tag t : tags){
            Optional<User> owner = userRepository.findById(t.getCreatorId());
            // tags created by this user or hse staff
            if (Objects.equals(t.getCreatorId(), dto.getUserId()) || (owner.isPresent()
                    && owner.get().getRole() == Role.HSE_STAFF)){
                tagsForUser.add(t);
            }
        }

        return tagsForUser;

    }

    public Tag fingTag(Long id) throws TagException {
        Optional<Tag> isPresent = tagRepository.findById(id);
        if (isPresent.isEmpty()) throw new TagException("No tag with id " + id);

        return isPresent.get();
    }
}
