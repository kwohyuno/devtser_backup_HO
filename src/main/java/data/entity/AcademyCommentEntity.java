package data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import data.dto.AcademyCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "academycomment")
@Builder
public class AcademyCommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "abc_idx")
    private int ABcidx;

    @Column(name="ab_idx")
    private int ABidx;

    @Column(name="m_idx")
    private int MIdx;

    @Column(name="abc_content")
    private String ABccontent;

    @Column(name="abc_like")
    private int ABclike;

    @Column(name="abc_depth")
    private int ABcdepth;

    @Column(name="abc_writeday",insertable = false)
    private Timestamp ABcwriteday;

    public static AcademyCommentEntity toAcademyCommentEntity(AcademyCommentDto dto){
        return AcademyCommentEntity.builder()
            .ABcidx(dto.getAbc_idx())
            .ABidx(dto.getAb_idx())
            .MIdx(dto.getM_idx())
            .ABccontent(dto.getAbc_content())
            .ABclike(dto.getAbc_like())
            .ABcdepth(dto.getAbc_depth())
            .ABcwriteday(dto.getAbc_writeday())
            .build();
    }
}
