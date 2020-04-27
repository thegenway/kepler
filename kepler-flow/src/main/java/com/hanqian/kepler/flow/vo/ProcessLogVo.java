package com.hanqian.kepler.flow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessLogVo {

    private String keyId;

    private String flowDutyName;

    private String flowDutyId;

    private String flowComment;

    public ProcessLogVo(String keyId) {
        this.keyId = keyId;
    }
}
