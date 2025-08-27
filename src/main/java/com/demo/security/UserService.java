package com.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2025-08-26
 */
@Service
public class UserService implements UserDetailsService  {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String password = passwordEncoder.encode("123456");
        logger.info("UserService loadUserByUsername password {}", password);
        User user = new User(username, password, true, true, true,true, AuthorityUtils.commaSeparatedStringToAuthorityList("role"));
        return user;
    }

    /**
     * 查询用户
     * @param username
     * @return
     */
    public UserDetails findByUsername(String username){
        if (username == "admin"){
            return User.withUsername("admin").password("123456").roles("admin_role").build();
        }
        return null;
    }

    /**
     * 创建用户
     * @param username
     * @param password
     */
    public void createUser(String username, String password){
        logger.info("UserService.createUser username {} password {}", username, passwordEncoder.encode(password));
    }


    public User findBySocialIdAndProvider(String socialId, String registrationId) {
        return null;
    }
}
