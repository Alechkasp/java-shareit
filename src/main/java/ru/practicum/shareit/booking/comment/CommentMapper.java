package ru.practicum.shareit.booking.comment;

public class CommentMapper {

    public static Comment toComment(CreateCommentDto commentDto) {
        return Comment.builder()
                .text(commentDto.getText())
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}