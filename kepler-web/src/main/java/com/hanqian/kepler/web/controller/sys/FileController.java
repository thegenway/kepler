package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.core.service.sys.FileManageService;
import com.hanqian.kepler.web.controller.BaseController;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Object upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "fileName", required = false)String fileName) throws IOException {
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

        if(file != null){
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

    /**
     * 删除附件
     */
    @PostMapping("delete")
    @ResponseBody
    public AjaxResult delete(String keyId){
        FileManage fileManage = fileManageService.get(keyId);
        fileManageService.delete(fileManage);
        return AjaxResult.success("删除成功");
    }

    /**
     * 附件列表（针对dropzone插件的）
     */
    @GetMapping("fileList")
    @ResponseBody
    public AjaxResult fileList(@RequestParam String keyId) {
        List<Map<String, Object>> dataRows = new ArrayList<>();
        Map<String, Object> map ;
        if(StrUtil.isNotBlank(keyId)){
            for(String fileId : StrUtil.split(keyId, ",")){
                FileManage fileManage = fileManageService.get(fileId);
                if(fileManage == null){
                    continue;
                }
                map = new HashMap<>();
                map.put("id",fileManage.getId());
                String fileName = fileManage.getName();
                map.put("name",fileName);
                map.put("size",fileManage.getSize());
                map.put("type",fileManage.getContentType());
                map.put("filePath", "../main/file/"+fileManage.getId());
                String fileUrl = request.getServerName()+":"+request.getLocalPort()+"/main/file/"+fileManage.getId()+"&fullfilename="+fileName;
//                String fileViewUrl = "http://"+request.getServerName() + ":8012/onlinePreview?url="+ Encodes.urlEncode(fileUrl);
                String fileViewUrl = "http://"+request.getServerName() + ":8012/onlinePreview?url="+ URLUtil.encode(fileUrl);
                map.put("fileViewUrl", fileViewUrl);
                dataRows.add(map);
            }
        }
        return AjaxResult.success("success", dataRows);
    }

    /**
     * 单独针对图片类型的预览
     */
    @GetMapping(value = "imgView")
    public String imgView(Model model, String fileId){
        model.addAttribute("fileId", fileId);
        return "main/common/file_img_view";
    }

    /**
     * 进入公共excel导入页面
     */
    @GetMapping(value = "excelImportView")
    public String excelImportView(){
        return "main/common/excel_import_view";
    }

    /**
     * 下载excel导入模板
     * @param name 下载文件的名称
     * @param headArr 表头，用逗号分隔
     */
    @GetMapping("importTemp")
    @ResponseBody
    public void importTemp(String name, String headArr) throws IOException {
        if(StrUtil.isBlank(name)) name="未知模板";
        List<String> row1 = CollUtil.newArrayList(StrUtil.split(headArr, ","));

        List<List<String>> rows = CollUtil.newArrayList();
        rows.add(row1);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(rows);
        for(int i=0;i<writer.getColumnCount();i++){
            writer.setColumnWidth(i, 16);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((name + ".xlsx").getBytes(), "iso-8859-1"));

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

}
