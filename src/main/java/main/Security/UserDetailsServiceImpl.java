package main.Security;

import main.Model.UserModel;
import main.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //"userDetailsServiceImpl"
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    //System.out.println("\nmail = " + email);

    UserModel user = userRepository.findAllByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException("user " + email + " not found"));

    //System.out.println(" pass = " + user.getPassword() + "\n");
    //System.out.println(user.getRole().toString());

    return SecurityUser.fromUser(user);
  }
}
