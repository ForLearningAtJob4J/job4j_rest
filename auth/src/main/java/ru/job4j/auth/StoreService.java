package ru.job4j.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.EmployeeRepository;
import ru.job4j.auth.repository.PersonRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public StoreService(PersonRepository personRepository, EmployeeRepository employeeRepository) {
        this.personRepository = personRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Person> findAllPerson() {
        return new ArrayList<>((Collection<Person>) personRepository.findAll());
    }

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public List<Employee> findAllEmployees() {
        return new ArrayList<>((Collection<Employee>) employeeRepository.findAll());
    }

    public Optional<Employee> findEmployeeById(int id) {
        return employeeRepository.findById(id);
    }
}