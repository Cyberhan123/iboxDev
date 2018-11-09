package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record,RecordKey> {
    //List<Record> findByIceIdAndOpFlag(String macip,Long opFlag);
    List<Record> findAllByIceId(String macip);
    List<Record> findByUuidOrderByOpDateDesc(String uuid);

     Record save(Record record);
}
