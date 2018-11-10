package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Long, User> {
    List<User> getAllByFid(Long fid);
    User save(User user);
}
