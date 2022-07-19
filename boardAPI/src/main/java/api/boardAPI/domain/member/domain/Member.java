package api.boardAPI.domain.member.domain;

import api.boardAPI.domain.board.domain.Board;
import api.boardAPI.domain.comment.domain.Comment;
import api.boardAPI.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 50, nullable = false)
    private int age;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void addBoard(Board board) {
        boardList.add(board);
    }

    public void addComment(Comment comment){
        commentList.add(comment);
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateNickName(String nickname){
        this.nickname = nickname;
    }

    public void updateAge(int age){
        this.age = age;
    }
}
