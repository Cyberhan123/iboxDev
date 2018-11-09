package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficialCardRepository extends JpaRepository<OfficialCard, Long> {
    List<OfficialCard> getOfficialCardByUuidIs(Long uuId);

    OfficialCard save(OfficialCard officialCard);
}
