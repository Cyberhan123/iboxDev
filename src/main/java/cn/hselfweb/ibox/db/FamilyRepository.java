package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(exported = false)
public interface FamilyRepository extends JpaRepository<Family,Long> {
    List<Family> findAllByUid(Long uid);
    Family save(Family family);
    Family deleteByFidAndUid(Long fid, Long uid);
    Family findByFidAndUid(Long fid, Long uid);
}
