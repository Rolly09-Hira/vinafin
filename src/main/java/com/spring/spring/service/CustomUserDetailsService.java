package com.spring.spring.service;

import com.spring.spring.entity.Utilisateur;
import com.spring.spring.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + email));

        if (!utilisateur.getActif()) {
            throw new UsernameNotFoundException("Compte désactivé: " + email);
        }

        return new User(
                utilisateur.getEmail(),
                utilisateur.getMotDePasse(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole()))
        );
    }

    // Méthode pour charger l'utilisateur complet
    public Utilisateur loadUserComplete(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + email));
    }

    // Nouvelle méthode pour charger par ID
    public Utilisateur loadUserComplete(Long id) throws UsernameNotFoundException {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec ID: " + id));
    }
}