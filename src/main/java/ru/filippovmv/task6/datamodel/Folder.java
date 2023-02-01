package ru.filippovmv.task6.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    private Set<String> folders;
    private List<FileH> files;
}
