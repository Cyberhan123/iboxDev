package cn.hselfweb.ibox.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class Validation {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "identity")
    private String identity;

    @Column(name = "tel")
    private String tel;

    @Column(name = "duedate")
    private Date duedate;
}
