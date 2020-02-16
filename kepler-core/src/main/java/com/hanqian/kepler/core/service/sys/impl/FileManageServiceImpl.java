package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.core.dao.primary.base.BaseDao;
import com.hanqian.kepler.core.dao.primary.sys.FileManageDao;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.core.service.base.BaseServiceImpl;
import com.hanqian.kepler.core.service.sys.FileManageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileManageServiceImpl extends BaseServiceImpl<FileManage, String> implements FileManageService {

    @Autowired
    private FileManageDao fileManageDao;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public BaseDao<FileManage, String> getBaseDao() {
        return fileManageDao;
    }

    private String createGridFile(InputStream inputStream, String fileName, String contentType) {
        ObjectId objectId = gridFsTemplate.store(inputStream, fileName, contentType);
        return objectId.toString();
    }

    @Override
    public FileManage saveFile(InputStream inputStream, String fileName, String contentType) {
        String gridId = createGridFile(inputStream, fileName, contentType);
        if(StrUtil.isNotBlank(gridId)){
            GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(gridId)));
            FileManage fileManage = new FileManage();
            fileManage.setName(fileName);
            fileManage.setGridId(gridId);
            fileManage.setContentType(contentType);
            fileManage.setSize(fsFile!=null ? fsFile.getLength() : 0L);
            fileManage.setUrl("/main/file/"+gridId);
            save(fileManage);
            return fileManage;
        }
        return null;
    }

    @Override
    public GridFSFile getFile(String fileId) {
        FileManage fileManage = get(fileId);
        return fileManage!=null ? getFileByGrid(fileManage.getGridId()) : null;
    }

    @Override
    public GridFSFile getFileByGrid(String grid) {
        return StrUtil.isNotBlank(grid) ? gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(grid))) : null;
    }

    @Override
    public AjaxResult deleteFile(String fileId) {
        FileManage fileManage = get(fileId);
        if(fileManage!=null){
            Query query = Query.query(Criteria.where("_id").is(fileManage.getGridId()));
            gridFsTemplate.delete(query);

            fileManage.setState(BaseEnumManager.StateEnum.Delete);
            save(fileManage);

            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
