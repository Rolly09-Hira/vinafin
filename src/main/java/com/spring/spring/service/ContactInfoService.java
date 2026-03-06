package com.spring.spring.service;

import com.spring.spring.dto.ContactInfoRequestDTO;
import com.spring.spring.entity.ContactInfo;
import com.spring.spring.repository.ContactInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    public List<ContactInfo> getAllContactInfos() {
        return contactInfoRepository.findByActifTrueOrderByOrdreAffichageAsc();
    }

    public List<ContactInfo> getByType(String type) {
        return contactInfoRepository.findByTypeContact(type);
    }

    public Optional<ContactInfo> getById(Long id) {
        return contactInfoRepository.findById(id);
    }

    public ContactInfo createContactInfo(ContactInfoRequestDTO dto) {
        if (contactInfoRepository.existsByTitre(dto.getTitre())) {
            throw new RuntimeException("Un contact avec ce titre existe déjà");
        }

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setTitre(dto.getTitre());
        contactInfo.setValeur(dto.getValeur());
        contactInfo.setIcone(dto.getIcone());
        contactInfo.setTypeContact(dto.getTypeContact());
        contactInfo.setLien(dto.getLien());
        contactInfo.setOrdreAffichage(dto.getOrdreAffichage());
        contactInfo.setActif(dto.getActif() != null ? dto.getActif() : true);

        return contactInfoRepository.save(contactInfo);
    }

    public ContactInfo updateContactInfo(Long id, ContactInfoRequestDTO dto) {
        ContactInfo contactInfo = contactInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact non trouvé"));

        // Vérifier si le titre n'est pas déjà utilisé par un autre contact
        if (!contactInfo.getTitre().equals(dto.getTitre()) &&
                contactInfoRepository.existsByTitre(dto.getTitre())) {
            throw new RuntimeException("Un contact avec ce titre existe déjà");
        }

        contactInfo.setTitre(dto.getTitre());
        contactInfo.setValeur(dto.getValeur());
        contactInfo.setIcone(dto.getIcone());
        contactInfo.setTypeContact(dto.getTypeContact());
        contactInfo.setLien(dto.getLien());
        contactInfo.setOrdreAffichage(dto.getOrdreAffichage());
        if (dto.getActif() != null) {
            contactInfo.setActif(dto.getActif());
        }

        return contactInfoRepository.save(contactInfo);
    }

    public void deleteContactInfo(Long id) {
        if (!contactInfoRepository.existsById(id)) {
            throw new RuntimeException("Contact non trouvé");
        }
        contactInfoRepository.deleteById(id);
    }

    public ContactInfo toggleActif(Long id) {
        ContactInfo contactInfo = contactInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact non trouvé"));

        contactInfo.setActif(!contactInfo.getActif());
        return contactInfoRepository.save(contactInfo);
    }

    public List<ContactInfo> getTelephones() {
        return contactInfoRepository.findByTypeContact("telephone");
    }

    public List<ContactInfo> getEmails() {
        return contactInfoRepository.findByTypeContact("email");
    }

    public List<ContactInfo> getReseauxSociaux() {
        return contactInfoRepository.findByTypeContact("reseau_social");
    }

    public List<ContactInfo> getAdresses() {
        return contactInfoRepository.findByTypeContact("adresse");
    }
}