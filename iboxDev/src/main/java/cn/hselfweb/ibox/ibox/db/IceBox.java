package cn.hselfweb.ibox.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "icebox")
public class IceBox {
    @Id
    @Column(name = "ice_id")
    private String iceId;

    @Column(name = "fid")
    private Long fid;

    @Column(name = "ice_name")
    private String iceName;

}
