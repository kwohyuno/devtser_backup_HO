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
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_idx")
    private Short idx;

    @Column(name = "m_email", length = 50)
    private String email;

    @Column(name = "m_pass", length = 300)
    private String password;

    @Column(name = "m_state", columnDefinition = "smallint default 0")
    private Short state;

    @Column(name = "m_tele", length = 30)
    private String tele;

    @Column(name = "ai_idx")
    private Short aiIdx;

    @Column(name = "m_name", length = 30)
    private String name;

    @Column(name = "m_nickname", length = 50)
    private String nickname;

    @Column(name = "m_photo", length = 500)
    private String photo;

    @Column(name = "m_point", columnDefinition = "int default 100")
    private Integer point;

    @Column(name = "m_filename", length = 200, nullable = false, columnDefinition = "varchar(200) default 'no'")
    private String filename;

    @Column(name = "m_new", columnDefinition = "smallint default 0")
    private Short newMember;

    @Column(name = "ai_name", length = 50)
    private String aiName;

    @Column(name = "salt", length = 100)
    private String salt;

    @Column(name = "m_type")
    private Short type;

    @Column(name = "m_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date;
}
