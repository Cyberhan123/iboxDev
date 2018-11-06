package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @Column(name = "uid")
    private Long uid;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "tel")
    private String tel;

    @Column(name = "info")
    private String info;

    @Column(name = "head_url")
    private String headUrl;

    @Column(name = "fid")
    private Long fid;
}
