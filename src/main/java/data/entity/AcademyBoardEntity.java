package data.entity;
import javax.persistence.*;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@EntityScan
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "academyboard")
public class AcademyBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ab_idx")
    private int abIdx;

    @Column(name = "m_idx")
    private short mIdx;

    @Column(name = "ab_subject", nullable = false, length = 200)
    private String abSubject;

    @Column(name = "ab_content", nullable = false, length = 10000)
    private String abContent;

    @Column(name = "ab_photo", length = 500)
    private String abPhoto;

    @Column(name = "ab_readcount", columnDefinition = "smallint default 0")
    private short abReadCount;

    @Column(name = "ab_like", columnDefinition = "smallint default 0")
    private short abLike;

    @Column(name = "ab_dislike", columnDefinition = "smallint default 0")
    private short abDislike;

    @Column(name = "ab_writeday", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime abWriteDay;

    @Column(name = "ai_idx")
    private short aiIdx;

    // Getter와 Setter

}
