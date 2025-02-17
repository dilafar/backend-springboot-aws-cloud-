package com.employees.employeemanager.service.impl;

import com.employees.employeemanager.dto.EmployeeDto;
import com.employees.employeemanager.entity.Employee;
import com.employees.employeemanager.exception.EmployeeAlreadyExistsException;
import com.employees.employeemanager.exception.EmptyResourceListException;
import com.employees.employeemanager.exception.ResourceNotFoundException;
import com.employees.employeemanager.mapper.EmployeeMapper;
import com.employees.employeemanager.repository.EmployeeRepository;
import com.employees.employeemanager.service.IEmployeeService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    public static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);
    private EmployeeRepository employeeRepository;
    /**
     * @param employeeDto
     */
    @Override
    public void createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto,new Employee());
        employee.setEmployeeCode(UUID.randomUUID().toString());
        Optional<Employee> optionalEmployee = employeeRepository.findByPhone(employeeDto.getPhone());
        if(optionalEmployee.isPresent()){
            throw new EmployeeAlreadyExistsException("Employee already registered with given mobileNumber"+employeeDto.getPhone());
        }
        logger.info("EmployeeController:create employee payload :",employee);
        employeeRepository.save(employee);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> fetchEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new EmptyResourceListException("employees list is empty");
        }
        return employees;
    }

    /**
     * @param employeeId
     * @param employeeDto
     * @return
     */
    @Override
    public boolean updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        boolean isupdated = false;
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("employee","employeeId",employeeId.toString())
        );
        EmployeeMapper.mapToEmployee(employeeDto,employee);
        employeeRepository.save(employee);
        logger.info("EmployeeController:update employee payload :",employee);
        isupdated = true;
        return isupdated;
    }

    /**
     * @param employeeId
     * @return
     */
    @Override
    public boolean deleteEmployee(Long employeeId) {
        boolean isupdated = false;
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("employee","employeeId",employeeId.toString())
        );
        employeeRepository.deleteById(employeeId);
        logger.info("EmployeeController: employee deleted from repo :",employeeId);
        isupdated = true;
        return isupdated;
    }

    /**
     * @param employeeId
     * @return
     */
    @Override
    public Employee fetchEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("employee","employeeId",employeeId.toString())
        );
        return employee;
    }
}
