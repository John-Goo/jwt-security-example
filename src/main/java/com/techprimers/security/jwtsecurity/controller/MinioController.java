package com.techprimers.security.jwtsecurity.controller;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.alibaba.fastjson.JSON;
import com.techprimers.security.jwtsecurity.model.JwtUser;
import com.techprimers.security.jwtsecurity.util.FileUtil;
import com.techprimers.security.jwtsecurity.vo.Res;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author John Goo
 * @version 1.0
 * @ClassName: MinioController
 * @Desc: TODO
 * @history v1.0
 */
@RestController
public class MinioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioController.class);

    @Value("${minio.endpoint}")
    private  String ENDPOINT;
    @Value("${minio.bucketName}")
    private  String BUCKETNAME;
    @Value("${minio.accessKey}")
    private  String ACCESSKEY;
    @Value("${minio.secretKey}")
    private  String SECRETKEY;







    @GetMapping
    public void downloadFiles(@RequestParam("filename") String filename, HttpServletResponse httpResponse) {

        try {

            MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
            InputStream object = minioClient.getObject(BUCKETNAME, filename);
            byte buf[] = new byte[1024];
            int length = 0;
            httpResponse.reset();
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            httpResponse.setContentType("application/octet-stream");
            httpResponse.setCharacterEncoding("utf-8");
            OutputStream outputStream = httpResponse.getOutputStream();
            while ((length = object.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
        } catch (Exception ex) {
            LOGGER.info("导出失败：", ex.getMessage());
        }
    }

    /** /file/company/public?p=f1/f2/test.png&token=kl0088-0001111111-88999
     *  /file/${BucketName}/public  ====> uploadPublic(),uploadPrivacy()
     *  p=${企业编号}/${1级功能}/test.png
     *
     * **/


    @PostMapping("/upload")
    public Res upload(@RequestParam(name = "file", required = false) MultipartFile[] file) throws InvalidPortException, InvalidEndpointException {
        String prefixPath = "f1/f2/";
        MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        Res res = new Res();
        res.setCode(500);
        if (file == null || file.length == 0) {
            res.setMessage("上传文件不能为空");
            return res;
        }
        List<String> orgfileNameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String orgfileName = multipartFile.getOriginalFilename();
            String storeObj = prefixPath + orgfileName;
            String fileType = multipartFile.getContentType();
            String filePath = storeObj+"&fileType="+fileType;
            System.out.println(">>>>>文件类型："+fileType);
            String accessPath = "http://localhost:8080/view/demo?p="+filePath;
            orgfileNameList.add(accessPath);
            InputStream in = null;
            try {
                in = multipartFile.getInputStream();
                minioClient.putObject(BUCKETNAME, storeObj, in, new PutObjectOptions(in.available(), -1));
            } catch (Exception e) {
                LOGGER.error("{}-->",e);
                res.setMessage("上传失败");
                return res;
            }finally {
                if(in !=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bucketName", BUCKETNAME);
        data.put("fileName", orgfileNameList);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);
        return res;
    }

    public static final  String TEMPLATE_FILEPATH = "/file/${orgType}/public?p=${filePath}";
    @PostMapping("/upload/file")
    public String writeStream(HttpServletRequest request) throws InvalidPortException, InvalidEndpointException, IOException {
        String token =request.getHeader("token");
        String userId =request.getHeader("userId");
        String orgType =request.getHeader("orgType");
        String originFileName = request.getHeader("originFileName");
        //企业编号/activity/
       // String dir = p1.orgCode+"/"+p1.moduleDir;
        String fileDir =request.getHeader("moduleDir");
      ;
        MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        Res res = new Res();
        res.setCode(500);
        InputStream  in =  request.getInputStream();
        if (in == null ) {
            res.setMessage("上传文件不能为空");
            return JSON.toJSONString(res);
        }
           String name = FileUtil.generateUUIDfileName(originFileName);
           String filePath = fileDir +name;
            try {
                minioClient.putObject(orgType, filePath, in, new PutObjectOptions(in.available(), -1));
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                res.setMessage("上传失败");
                return JSON.toJSONString(res);
            }finally {
                if(in !=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        Map<String, Object> data = new HashMap<String, Object>();
        String fileAccessUrl = TEMPLATE_FILEPATH;
        fileAccessUrl = fileAccessUrl.replace("${filePath}",filePath);
        fileAccessUrl = fileAccessUrl.replace("${orgType}",orgType);
            //file/company/public?p=f1/f2/test.png
        data.put("filePath", fileAccessUrl);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);
        System.out.println("返回结果====>"+res.toString());
        return JSON.toJSONString(res);
    }



    @PostMapping("/upload1/file")
    public Res writeStream1(HttpServletRequest request) throws InvalidPortException, InvalidEndpointException, IOException, ServletException {
        String prefixPath = "f33/44/";
        MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        Res res = new Res();
        res.setCode(500);
        Collection<Part> parts =  request.getParts();
        Iterator<Part> it =  parts.iterator();
        while (it.hasNext()){

            Part part =  it.next();
            InputStream in = part.getInputStream();
            String orgfileName = prefixPath + new Date().getTime()+".png";
            try {
                minioClient.putObject(BUCKETNAME, orgfileName, in, new PutObjectOptions(in.available(), -1));
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                res.setMessage("上传失败");
                return res;
            }finally {
                if(in !=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bucketName", BUCKETNAME);
        //data.put("fileName", orgfileName);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);
        return res;
    }

    @PostMapping("/upload2/file")
    public Res writeStream2(HttpServletRequest request) throws InvalidPortException, InvalidEndpointException, IOException, ServletException {
        Map<String, Object> data = new HashMap<String, Object>();

        String token =request.getHeader("token");
        String userId =request.getHeader("userId");
        String orgType =request.getHeader("orgType");
        String originFileName = request.getHeader("originFileName");
        //企业编号/activity/
        // String dir = p1.orgCode+"/"+p1.moduleDir;
        String fileDir =request.getHeader("moduleDir");

      // String prefixPath = "f33/44/";
        MinioClient minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        Res res = new Res();
        res.setCode(500);
        String filePath = null;
        Collection<Part> parts =  request.getParts();
        Iterator<Part> it =  parts.iterator();
        while (it.hasNext()){
            Part part =  it.next();
            InputStream in = part.getInputStream();
            String name = FileUtil.generateUUIDfileName(originFileName);
            filePath = fileDir +name;
            //String orgfileName = prefixPath + new Date().getTime()+".png";
            try {
                ByteArrayInputStream bis = null;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                minioClient.putObject(orgType, filePath, bis, new PutObjectOptions(bis.available(), -1));

            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                res.setMessage("上传失败");
                return res;
            }finally {
                if(in !=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String fileAccessUrl = TEMPLATE_FILEPATH;
        fileAccessUrl = fileAccessUrl.replace("${filePath}",filePath);
        fileAccessUrl = fileAccessUrl.replace("${orgType}",orgType);
        //file/company/public?p=f1/f2/test.png
        data.put("filePath", fileAccessUrl);
        res.setCode(200);
        res.setMessage("上传成功");
        res.setData(data);
        return res;
    }


    @RequestMapping("/view1/{bucketName}")
    public void view(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable("bucketName") String bucketName)  {
        String token = request.getParameter("token");
        String filePath= request.getParameter("p");
        String fileType= request.getParameter("fileType");
        LOGGER.info(" ========>     执行了"+filePath+",time:"+new Date().getTime());
        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            // ObjectStat stat = minioClient.statObject(bucketName, filePath);
            response.setContentType(fileType);
            String fileName=FileUtil.extractFileName(filePath);
            // response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            in = minioClient.getObject(bucketName, filePath);
            byte[] data = new byte[in.available()];
            in.read(data);
            String diskfilename = "final.mp4";
            response.setContentType("video/mp4");
           // response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
           // System.out.println("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + Integer.valueOf(data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();
            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            in.close();
        } catch (Exception e) {
            LOGGER.error("下载发生了异常！",e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }


    @GetMapping("/view/{bucketName}")
    public void viewq(HttpServletResponse response, HttpServletRequest request,
                     @PathVariable("bucketName") String bucketName) throws FileNotFoundException {
        String token = request.getParameter("token");
        String filePath= request.getParameter("p");
        String fileType= request.getParameter("fileType");
        LOGGER.info(" ========>     执行了"+filePath+",time:"+new Date().getTime());



        MinioClient minioClient = null;
        try {
            minioClient = new MinioClient(ENDPOINT, ACCESSKEY, SECRETKEY);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            // ObjectStat stat = minioClient.statObject(bucketName, filePath);
            response.setContentType(fileType);
            String fileName=FileUtil.extractFileName(filePath);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
             //in = new FileInputStream(new File("F:\\training-video\\Lambda\\test\\MyVideo_1.mp4"));
            in = minioClient.getObject(bucketName, filePath);
            LOGGER.info(" >>>本次下载文件大小："+in.available());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            LOGGER.error("下载发生了异常！",e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    @RequestMapping("/show")
    public void show(HttpServletResponse response){
            JwtUser jwtUser = new JwtUser();
            jwtUser.setId(10008);
            jwtUser.setRole("admin");
            jwtUser.setUserName("谷海江");
            FileUtil.print(response,jwtUser);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = URLEncoder.encode("f1/f2/MyVideo_1.mp4&fileType=video/mp4", "UTF-8");
        System.out.println(str);
        String deStr = URLDecoder.decode(str,"UTF-8");
        System.out.println(deStr);
    }
}
