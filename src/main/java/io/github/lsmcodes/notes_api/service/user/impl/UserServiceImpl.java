package io.github.lsmcodes.notes_api.service.user.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;
import io.github.lsmcodes.notes_api.service.user.UserService;

/**
 * Implements {@link UserService} interface methods.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(UUID id) {
        return this.userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(UUID id) {
        this.userRepository.deleteById(id);
    }

}