package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnOfficialCardRepository extends JpaRepository<UnOfficialCard,Long>{
    UnOfficialCard getByUuid(String uuid);
}
