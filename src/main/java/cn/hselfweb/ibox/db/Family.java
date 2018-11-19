package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "family")
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "name")
    private String name;

}
