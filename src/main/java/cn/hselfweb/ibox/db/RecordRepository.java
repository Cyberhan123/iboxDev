package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Long> {
    List<Record> findAllByIceIdAndOpFlag(String macip,Long opFlag);
}
