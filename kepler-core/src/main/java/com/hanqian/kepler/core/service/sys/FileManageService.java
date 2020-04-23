package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.common.base.service.BaseService;
import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.InputStream;
import java.util.List;

public interface FileManageService extends BaseService<FileManage, String> {

    /**
     * 保存附件
     */
    FileManage saveFile(InputStream inputStream, String fileName, String contentType);

    /**
     * 获取附件
     */
    GridFSFile getFile(String fileId);
    GridFSFile getFileByGrid(String grid);

    /**
     * 根据附件名称获取附件
     */
    List<FileManage> getFileManageByName(String name);

    /**
     * 真 · 删除附件
     */
    AjaxResult deleteFile(String fileId);

}
