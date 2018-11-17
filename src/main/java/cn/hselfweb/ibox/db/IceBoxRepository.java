package cn.hselfweb.ibox.db;


import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface IceBoxRepository extends Repository<IceBox,String> {
    IceBox getIceBoxByIceId(String macip);

    IceBox save(IceBox iceBox);
}
