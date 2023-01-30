package ru.filippovmv.task1.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileH {
    private String path;
    private Integer Hash;
}
