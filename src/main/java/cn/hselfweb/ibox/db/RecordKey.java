package cn.hselfweb.ibox.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordKey implements Serializable {
    private Long uuid;
    private String iceId;
    private Long fid;
}
