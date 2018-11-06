package cn.hselfweb.ibox.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IceBoxRepository extends JpaRepository<IceBox,String> {
    IceBox getIceBoxByIceId(String macip);

}
