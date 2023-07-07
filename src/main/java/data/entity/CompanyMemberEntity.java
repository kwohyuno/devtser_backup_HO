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
@Table(name = "companymember")
public class CompanyMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cm_idx")
    private Short cmIdx;

    @Column(name = "cm_email", length = 50)
    private String cmEmail;

    @Column(name = "cm_pass", length = 300)
    private String cmPass;

    @Column(name = "cm_tele", length = 30)
    private String cmTele;

    @Column(name = "cm_addr", length = 100)
    private String cmAddr;

    @Column(name = "cm_compname", length = 50)
    private String cmCompname;

    @Column(name = "cm_filename", length = 200, nullable = false, columnDefinition = "varchar(200) default 'no'")
    private String cmFilename;

    @Column(name = "cm_new", columnDefinition = "smallint default 0")
    private Short cmNew;

    @Column(name = "cm_post", length = 30)
    private String cmPost;

    @Column(name = "cm_name", length = 30)
    private String cmName;

    @Column(name = "cm_cp", length = 30)
    private String cmCp;

    @Column(name = "salt", length = 100)
    private String salt;

    @Column(name = "cm_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime cmDate;

    @Column(name = "cm_reg", length = 50)
    private String cmReg;
}
