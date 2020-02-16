package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.FileManage;
import com.hanqian.kepler.core.service.base.BaseService;
import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.InputStream;

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
     * 真 · 删除附件
     */
    AjaxResult deleteFile(String fileId);

}
