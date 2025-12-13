package bj.ent.test_camunda.service;

import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonneService {

    @Autowired
    private PersonneRepository personneRepository;

    public List<Personne> getAll() {
        return personneRepository.findAll();
    }

    public Personne getById(Long id) {
        return personneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personne non trouvée avec l'ID: " + id));
    }

    @Transactional
    public Personne save(Personne personne) {
        return personneRepository.save(personne);
    }

    @Transactional
    public void delete(Long id) {
        personneRepository.deleteById(id);
    }
}