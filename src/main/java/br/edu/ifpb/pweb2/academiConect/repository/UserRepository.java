package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.academiConect.model.User;

//@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public List<User> findByEnabledTrue();
    
}
