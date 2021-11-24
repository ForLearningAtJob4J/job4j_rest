package ru.job4j.auth.controller;

import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.StoreService;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final StoreService storeService;

    public EmployeeController(final StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/")
    public List<Employee> findAll() {
        return storeService.findAllEmployees();
    }

    @PostMapping("/{id}/account")
    public Employee addAccount(@PathVariable int id, @RequestBody Person person) {
        Employee employee = storeService.findEmployeeById(id).orElse(null);
        if (employee != null) {
            storeService.save(person);
            employee.addAccount(person);
            storeService.save(employee);
        }
        return employee;
    }

    @PutMapping("/{id}/account")
    public Employee editAccount(@PathVariable int id, @RequestBody Person person) {
        Employee employee = storeService.findEmployeeById(id).orElse(null);
        Person account = storeService.findPersonById(id).orElse(null);
        if (employee != null && account != null) {
            employee.removeAccount(account);
            employee.addAccount(person);
            storeService.save(employee);
        }
        return employee;
    }

    @DeleteMapping("/{id}/account/{accId}")
    public Employee deleteAccount(@PathVariable int id, @PathVariable int accId) {
        Employee employee = storeService.findEmployeeById(id).orElse(null);
        Person account = storeService.findPersonById(id).orElse(null);
        if (employee != null && account != null) {
            employee.removeAccount(account);
            storeService.save(employee);
        }
        return employee;
    }
}
