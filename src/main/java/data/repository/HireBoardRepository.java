package data.repository;

import data.dto.HireBoardDto;
import data.entity.HireBoardEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpSession;

@Repository
public interface HireBoardRepository extends JpaRepository<HireBoardEntity, Integer> {
    Long countBy();
    // Page<HireBoardEntity> findAll(Pageable pageable);
    // public HireBoardEntity findByHbIdx(int hbIdx);


}



