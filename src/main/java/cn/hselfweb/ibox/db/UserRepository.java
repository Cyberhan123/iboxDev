package cn.hselfweb.ibox.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getAllByFid(Long fid);
    User save(User user);
    User getByTelAndPassword(String tel,String password);
    List<User> getAllByTel(String tel);
    User getByTel(String tel);
}
