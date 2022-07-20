package api.boardAPI.domain.board.domain;

import api.boardAPI.global.entity.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
