package data.controller;
import data.dto.HireBoardDto;
import data.service.HireBoardService;
import naver.cloud.NcpObjectStorageService;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;



@RestController
@CrossOrigin
@RequestMapping("/hboard")
public class HireBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    HireBoardService hireBoardService;

    @Autowired
    private NcpObjectStorageService storageService;

    String photo;

    @Value("${aws.s3.bucketName}")
    private String bucketName;


    @PostMapping("/upload")
    public String photoUpload(MultipartFile upload)
    {
        if(photo!=null){
            storageService.deleteFile(bucketName,"hboard",photo);
        }
        photo=storageService.uploadFile(bucketName,"hboard",upload);
        return photo;
    }

    @PostMapping("/insert")
    public void insert(@RequestBody HireBoardDto dto){
        dto.setHb_photo(photo);
        photo=null;
        hireBoardService.insertHireBoard(escapeDto(dto));
    }

    // @PostMapping
    // public ResponseEntity<HireBoardDto> insert(@RequestBody HireBoardDto dto){
    //     dto.setHb_photo(photo);
    //     photo=null;
    //     return new ResponseEntity<HireBoardDto>(hireBoardService.insertHireBoard(escapeDto(dto)),HttpStatus.OK);
    // }


    // @GetMapping
    // public ResponseEntity<List<HireBoardDto>> getAllData(@RequestParam(defaultValue="1") int currentPage){
    //     return new ResponseEntity<List<HireBoardDto>>(hireBoardService.getAllData(), HttpStatus.OK);
    // }

    // @GetMapping("/list")
    // public Map<String,Object> list(int currentPage){
    //     return hireBoardService.list(currentPage);
    // }

    @GetMapping("/list")
    public Map<String,Object> list(@RequestParam(defaultValue = "1") int currentPage){
        //페이징처리
        int totalCount;//총갯수
        int perPage=3;//한페이지당 출력할 글갯수
        int perBlock=3;//출력할 페이지갯수
        int startNum;//db에서 가져올 시작번호
        int startPage;//출력할 시작페이지
        int endPage;//출력할 끝페이지
        int totalPage;//총 페이지수
        int no;//출력할 시작번호

        //총갯수
        totalCount=hireBoardService.getTotalCount();
        //총 페이지수
        totalPage=totalCount/perPage+(totalCount%perPage==0?0:1);
        //시작페이지
        startPage=(currentPage-1)/perBlock*perBlock+1;
        //끝페이지
        endPage=startPage+perBlock-1;
        if(endPage>totalPage)
            endPage=totalPage;

        //시작번호
        startNum=(currentPage-1)*perPage;
        //각페이지당 출력할 번호
        no=totalCount-(currentPage-1)*perPage;

        List<HireBoardDto> list=hireBoardService.getPagingList(startNum, perPage);

        //출력할 페이지번호들을 Vector에 담아서 보내기
        Vector<Integer> parr=new Vector<>();
        for(int i=startPage;i<=endPage;i++){
            parr.add(i);
        }

        //리액트로 필요한 변수들을 Map 에 담아서 보낸다
        Map<String,Object> smap=new HashMap<>();
        smap.put("totalCount",totalCount);
        smap.put("list",list);
        smap.put("parr",parr);
        smap.put("startPage",startPage);
        smap.put("endPage",endPage);
        smap.put("no",no);
        smap.put("totalPage",totalPage);

        System.out.println(smap);
        return  smap;
    }





    @GetMapping("/{idx}")
    public ResponseEntity<HireBoardDto> getDetailPage(@PathVariable Integer idx){
        return new ResponseEntity<HireBoardDto>(hireBoardService.findByHbIdx(idx),HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Void> deleteHireBoard(@PathVariable Integer idx){
        hireBoardService.deleteHireBoard(idx);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/form/{idx}")
    public ResponseEntity<HireBoardDto> updateHireBoardForm(@PathVariable Integer idx){
        return new ResponseEntity<HireBoardDto>(hireBoardService.findByHbIdx(idx),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody HireBoardDto dto, MultipartFile upload, int currentPage){
        hireBoardService.updateHireBoard(escapeDto(dto),upload,currentPage);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/increaseBkmk")
    public void increaseBkmk(Integer hb_idx, Integer m_idx){
        hireBoardService.addBkmk(hb_idx,m_idx);
    }

    public HireBoardDto escapeDto(HireBoardDto dto){
        dto.setHb_content(StringEscapeUtils.escapeHtml4(dto.getHb_content()));
        dto.setHb_subject(StringEscapeUtils.escapeHtml4(dto.getHb_subject()));

        return dto;
    }

}



