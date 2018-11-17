package cn.hselfweb.ibox.service;


import cn.hselfweb.ibox.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SpringDataJpaUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Autowired
    public SpringDataJpaUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        cn.hselfweb.ibox.db.User user = this.repository.findByUserName(name);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles()));
    }
}
