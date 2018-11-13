package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnOfficialCardRepository extends JpaRepository<UnOfficialCard,String>{
    UnOfficialCard getByUuid(String uuid);

    boolean existsByUuid(String uuid);
}
