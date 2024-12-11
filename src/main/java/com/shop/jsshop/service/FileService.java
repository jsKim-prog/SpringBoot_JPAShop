package com.shop.jsshop.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService { //파일처리 용 서비스
    //파일업로드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID(); //uuid생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 추출
        String savedFileName = uuid.toString()+extension; // 저장명 : uuid.확장자
        String fileUploadFullUrl = uploadPath+"/"+savedFileName; //이미지 불러오는 경로
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //byte 단위 출력에 경로 실어 내보내기
        fos.write(fileData); //받은 데이터 저장
        fos.close();
        return savedFileName;
    }
    
    //파일삭제
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); //파일객체 찾기
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일삭제 완료./파일명 : "+filePath);
        }else {
            log.info("파일삭제 실패./파일명 : "+filePath);
        }
    }
}
