package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.core.service.sys.FileManageService;
import com.hanqian.kepler.web.controller.BaseController;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 附件上传
 */
@Controller
@RequestMapping("/main/file")
public class FileController extends BaseController {

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private FileManageService fileManageService;

    /**
     * 上传
     * @param multipartFile 附件
     * @param fileName 自定义附件名
     * @return FileManage
     */
    @PostMapping(value = "upload")
    @ResponseBody
    public Object upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileName")String fileName) throws IOException {
        if(multipartFile!=null){
            String filename = StrUtil.isNotBlank(fileName) ? fileName : multipartFile.getOriginalFilename();
            InputStream is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            FileManage fileManage = fileManageService.saveFile(is, filename, contentType);
            if(fileManage!=null){
                Map<String, String> map = new HashMap<>();
                map.put("id", fileManage.getId());
                map.put("name", fileManage.getName());
                map.put("url", "/main/file/"+fileManage.getId());
                return AjaxResult.success("上传成功", map);
            }
        }
        return AjaxResult.error();
    }

    /**
     * 获取附件（穿过来的id有可能是keyId也有可能是gridId）
     */
    @RequestMapping(value = "{id}")
    @ResponseBody
    public void download(@PathVariable  String id) throws IOException {
        byte[] bs =new byte[1024];

        GridFSFile file = null;
        if(StrUtil.length(id) == 32){ //32位说明是mysqlId
            file = fileManageService.getFile(id);
        }else{ //否则则是grid
            file = fileManageService.getFileByGrid(id);
        }

        GridFsResource resource = gridFsTemplate.getResource(file);
        InputStream inputStream = resource.getInputStream();
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getFilename().getBytes("gb2312"), "ISO8859-1"));
        while (inputStream.read(bs)>0){
            outputStream.write(bs);
        }
        inputStream.close();
        outputStream.close();
    }

}
