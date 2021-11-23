package ru.job4j.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final PersonRepository personRepository;

    @Autowired
    public StoreService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return new ArrayList<>((Collection<Person>) personRepository.findAll());
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }
}