package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficialCardRepository extends JpaRepository<OfficialCard, Long> {
    List<OfficialCard> getOfficialCardByUuidIs(String uuId);

    boolean existsByUuid(String uuid);

    OfficialCard save(OfficialCard officialCard);
}
