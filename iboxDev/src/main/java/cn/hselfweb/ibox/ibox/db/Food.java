package cn.hselfweb.ibox.ibox.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "food")
public class Food {
    @Id
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "food_url")
    private String foodUrl;

    @Column(name = "type")
    private Long type;
}
