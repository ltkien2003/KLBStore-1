package com.klbstore.model;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.klbstore.dao.NguoiDungDAO;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private NguoiDungDAO userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        NguoiDung user = userRepository.findBySdt(username);
        System.out.println(user);
        System.out.println(user.getMatKhau());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        Optional<NguoiDung> userOptional = userRepository.findById(id);
        
        NguoiDung user = userOptional.orElseThrow(
            () -> new UsernameNotFoundException("User not found with id: " + id)
        );
        
        return new CustomUserDetails(user);
    }
    


}
