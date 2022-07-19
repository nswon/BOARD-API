package api.boardAPI.domain.board.domain;

import api.boardAPI.domain.comment.domain.Comment;
import api.boardAPI.domain.member.domain.Member;
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
@Table(name = "BOARD")
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 40, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Lob
    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoard(this);
    }

    public void addComment(Comment comment){
        //comment의 Post 설정은 comment에서 함
        commentList.add(comment);
    }


}
