package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.models.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagDto {

    String name;
    User creator;

}
