package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
    User save(User user);
}
