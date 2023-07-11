package data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import data.entity.AcademyCommentEntity;

@Repository
public interface AcademyCommentRepository extends JpaRepository<AcademyCommentEntity, Integer>{
    int countBy();
}
