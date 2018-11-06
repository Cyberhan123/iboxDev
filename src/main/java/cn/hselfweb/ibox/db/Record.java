package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(RecordKey.class)
@Getter
@Setter
@Table(name = "record")
public class Record {

    @Id
    @Column(name = "uuid")
    private Long uuid;


    @Id
    @Column(name = "ice_id")
    private Long iceId;

    @Id
    @Column(name = "fid")
    private Long fid;

    @Column(name = "opflag")
    private Long opFlag;//操作方式

    @Column(name = "opdate")
    private Date opDate;//操作时间

    @Column(name = "tareweight")
    private String tareWeight;//皮重

    @Column(name = "foodweight")
    private String foodWeight;//食物重

    public Long getUuid() {
        return this.uuid;
    }
}
