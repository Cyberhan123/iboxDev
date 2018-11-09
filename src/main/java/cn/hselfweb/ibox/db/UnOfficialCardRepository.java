package cn.hselfweb.ibox.db;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UnOfficialCardRepository extends JpaRepository<Long,UnOfficialCard> {
    UnOfficialCard save(UnOfficialCard unOfficialCard);
}
