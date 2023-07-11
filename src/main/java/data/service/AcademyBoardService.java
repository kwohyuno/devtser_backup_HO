package data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import data.dto.AcademyBoardDto;
import data.entity.AcademyBoardEntity;
import data.entity.AcademylikeEntity;
import data.repository.AcademyBoardRepository;
import data.repository.AcademylikeRepository;
import naver.cloud.NcpObjectStorageService;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AcademyBoardService {
    private final Logger logger = LoggerFactory.getLogger(HireBoardService.class);

    @Autowired 
    private AcademyBoardRepository academyBoardRepository;

    @Autowired
    private AcademylikeRepository academylikeRepository;

    @Autowired
    private NcpObjectStorageService storageService;
    
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public AcademyBoardDto insertAcademyBoard(AcademyBoardDto dto,List<MultipartFile> upload){  
        String ab_photo="";
        if(upload.get(0).getOriginalFilename().equals("")){
            ab_photo="no";
        } else{
            int i=0;
            for(MultipartFile mfile : upload) {
                ab_photo += (storageService.uploadFile(bucketName, "devster/hireboard", mfile) + ",");
            }
        }
        ab_photo=ab_photo.substring(0,ab_photo.length()-1);
        dto.setAb_photo(ab_photo);

        try {
            AcademyBoardEntity entity = AcademyBoardEntity.toAcademyBoardEntity(dto);
            academyBoardRepository.save(entity);

            return dto;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Error occurred while inserting hireboard",e);
            throw e;
        }
    }


    public List<AcademyBoardDto> getAllData(){
        try{
            List<AcademyBoardDto> list = new ArrayList<>();
            for(AcademyBoardEntity entity : academyBoardRepository.findAll()){
                list.add(AcademyBoardDto.toAcademyBoardDto(entity));
            }
            return list;
        } catch(Exception e){
            logger.error("Error occurred while getting all hireboard data", e);
            throw e;
        }
    }    


    public AcademyBoardDto findByAbIdx(int idx){
        try {
            AcademyBoardEntity entity = academyBoardRepository.findById((Integer)idx)
                .orElseThrow(() -> new EntityNotFoundException("해당 idx는 존재하지 않습니다." + idx));
            return AcademyBoardDto.toAcademyBoardDto(entity);    
        } catch (EntityNotFoundException e) {
            logger.error("Error occurred while getting a entity", e);
            throw e;
        }
    } 



    public void deleteAcademyBoard(Integer idx){
        try {
            System.out.println(idx);
            academyBoardRepository.deleteById(idx);
        } catch (Exception e) {
            logger.error("Error occurred while deleting a entity",e);
        }
    }



    public void updateAcademyBoard(AcademyBoardDto dto,MultipartFile upload,int currentPage){

        String filename="";
        AcademyBoardEntity entity = academyBoardRepository.findById(dto.getAb_idx())
                .orElseThrow(() -> new EntityNotFoundException("해당 idx의 게시물이 존재하지 않습니다:" +dto.getAb_idx()));
        if(!upload.getOriginalFilename().equals("")) {
            filename= entity.getABphoto();
            storageService.deleteFile(bucketName,"devster/hireboard",filename);
            filename=storageService.uploadFile(bucketName, "devster/hireboard", upload);
        }
        try {
            entity.setABphoto(filename);
            academyBoardRepository.save(entity);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("Error occurred while inserting hireboard",e);
            throw e;
        }
    }

    @Transactional
    private AcademylikeEntity findOrCreateABoardLike(int ABidx,int MIdx){
            System.out.println(academylikeRepository.findByABidxAndMIdx(ABidx,MIdx));
            System.out.println(new AcademylikeEntity(ABidx,MIdx));
            return academylikeRepository.findByABidxAndMIdx(ABidx,MIdx)
                .orElse(new AcademylikeEntity(ABidx,MIdx));

    }    

    public void like(int ABidx, int MIdx){
        try {           
            AcademylikeEntity academylikeEntity = findOrCreateABoardLike(ABidx,MIdx);
            
            if (academylikeEntity.getLikestatus() == 1) {
                //throw new IllegalArgumentException("이미 좋아요가 눌려 있습니다");
                academylikeEntity.setLikestatus(0);
                academylikeRepository.save(academylikeEntity);
    
                AcademyBoardEntity academyBoardEntity = academyBoardRepository.findById(ABidx)
                    .orElseThrow(()-> new IllegalArgumentException("해당하는 리뷰 보드를 찾지 못했습니다(ab_like-1)"));
                academyBoardEntity.setABlike(academyBoardEntity.getABlike()-1);
                academyBoardRepository.save(academyBoardEntity);
    
            } else if (academylikeEntity.getLikestatus() == 2) {
                throw new IllegalArgumentException("이미 싫어요가 눌려 있습니다");
            } else {
                academylikeEntity.setLikestatus(1);
                academylikeRepository.save(academylikeEntity);
    
                // AcademyBoardEntity의 ab_like 필드 업데이트
                AcademyBoardEntity academyBoardEntity = academyBoardRepository.findById(ABidx)
                        .orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰 보드를 찾지 못했습니다(ab_like+1): " + ABidx));
                academyBoardEntity.setABlike(academyBoardEntity.getABlike() + 1);
                academyBoardRepository.save(academyBoardEntity);
            }
        } catch (IllegalArgumentException e) {
            logger.error("review like Error(Ill)", e);
        } catch (Exception e) {
         logger.error("review like Error(Exce)", e);
        }
    }
     

    public void dislike(int ABidx, int MIdx) {
        try {
            AcademylikeEntity academylikeEntity = findOrCreateABoardLike(ABidx, MIdx);
            
            if (academylikeEntity.getLikestatus() == 1) {
                throw new IllegalArgumentException("이미 좋아요가 눌려 있습니다");
            } else if (academylikeEntity.getLikestatus() == 2) {
                //throw new IllegalArgumentException("이미 싫어요가 눌려 있습니다");
                academylikeEntity.setLikestatus(0);
                academylikeRepository.save(academylikeEntity);
    
                AcademyBoardEntity academyBoardEntity = academyBoardRepository.findById(ABidx)
                    .orElseThrow(()-> new IllegalArgumentException("해당하는 리뷰 보드를 찾지 못했습니다(ab_dislike-1)"));
                academyBoardEntity.setABdislike(academyBoardEntity.getABdislike()-1);
                academyBoardRepository.save(academyBoardEntity);
    
            } else {
                academylikeEntity.setLikestatus(2);
                academylikeRepository.save(academylikeEntity);
    
                // AcademyBoardEntity의 ab_like 필드 업데이트
                AcademyBoardEntity academyBoardEntity = academyBoardRepository.findById(ABidx)
                        .orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰 보드를 찾지 못했습니다(rb_dislike+1): " + ABidx));
                academyBoardEntity.setABdislike(academyBoardEntity.getABdislike() + 1);
                academyBoardRepository.save(academyBoardEntity);
            }
        } catch (IllegalArgumentException e) {
            logger.error("review like Error(Ill)", e);
        } catch (Exception e) {
         logger.error("review like Error(Exce)", e);
        }
    }

}







