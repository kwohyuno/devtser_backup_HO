package data.controller;
import data.dto.HireBoardDto;
import data.dto.HireBookmarkDto;
import data.entity.HireBoardEntity;
import data.entity.TestEntity;
import data.service.HireBoardService;
import data.service.TestService;
import lombok.extern.slf4j.Slf4j;
import naver.cloud.NcpObjectStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/hireboard")
public class HireBoardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    HireBoardService hireBoardService;

    @Autowired
    private NcpObjectStorageService storageService;


    @PostMapping
    public ResponseEntity<HireBoardDto> insert(@RequestBody HireBoardDto dto,List<MultipartFile> upload,HttpSession session){
        return new ResponseEntity<HireBoardDto>(hireBoardService.insertHireBoard(dto,upload,session),HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<HireBoardDto>> getAllData(){
        return new ResponseEntity<List<HireBoardDto>>(hireBoardService.getAllData(), HttpStatus.OK);
    }

    @GetMapping("/{idx}")
    public ResponseEntity<HireBoardDto> getDetailPage(@PathVariable Integer idx){
        return new ResponseEntity<HireBoardDto>(hireBoardService.findByHbIdx(idx),HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public void deleteHireBoard(@PathVariable Integer idx){
        hireBoardService.deleteHireBoard(idx);
    }

    @GetMapping("/form/{idx}")
    public ResponseEntity<HireBoardDto> updateHireBoardForm(@PathVariable Integer idx){
        return new ResponseEntity<HireBoardDto>(hireBoardService.findByHbIdx(idx),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody HireBoardDto dto, MultipartFile upload, int currentPage){
        hireBoardService.updateHireBoard(dto,upload,currentPage);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/listajax")
    @ResponseBody
    public Map<String,Object> list(int currentPage){
        return hireBoardService.list(currentPage);
    }


    @GetMapping("/{hb_idx}/increaseBkmk")
    @ResponseBody
    public void increaseBkmk(@PathVariable Integer hb_idx, Integer m_idx){
        hireBoardService.addBkmk(hb_idx,m_idx);
    }



}




