package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IceBoxRepository extends JpaRepository<IceBox,String> {
    IceBox getIceBoxByIceId(String macip);

}
