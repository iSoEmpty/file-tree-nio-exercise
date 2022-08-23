# File Tree (NIO approach)

The purpose of this exercise is to train you to manage files and directories using Java NIO.

Duration: _1.5 hour_

## Description

You must implement a method to build a directory representation. You can also create other methods for convenience that will be called from the 'tree' method.

Please implement methods in the [FileTree](src/main/java/com/epam/autotasks/FileTree.java) class.

- `public Optional<String> tree(final Path path)` - takes a path to a file/directory as an input parameter and builds a String representation of its hierarchy.

1. If a given path *does not exist*, return an empty Optional.
2. If a given path *refers to a file*, return a String with its name and size like this:


    `some-file.txt 128 bytes`

3. If a given path *refers to a directory*, return a String with its name, total size and full hierarchy:

`

    some-dir 100 bytes
    ├─ some-inner-dir 50 bytes
    │  ├─ some-file.txt 20 bytes    
    │  └─ some-other-file.txt 30 bytes
    └─ some-one-more-file.txt 50 bytes
`

- Use pseudo-graphic characters to format the output.
- Compute the size of a directory as a sum of all its contents.
- Sort the contents in the following way:
    - Directories should go first.
    - Directories and files are sorted in lexicographic order (case-insensitive).
