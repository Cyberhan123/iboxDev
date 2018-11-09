package cn.hselfweb.ibox.db;


import org.springframework.data.repository.Repository;

public interface IceBoxRepository extends Repository<IceBox,String> {
    IceBox getIceBoxByIceId(String macip);

    IceBox save(IceBox iceBox);
}
