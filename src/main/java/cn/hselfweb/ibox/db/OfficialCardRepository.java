package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
//@RepositoryRestResource(exported = false)
public interface OfficialCardRepository extends JpaRepository<OfficialCard, Long> {
    List<OfficialCard> getOfficialCardByUuidIs(String uuId);

    boolean existsByUuid(String uuid);

    OfficialCard save(OfficialCard officialCard);
}
