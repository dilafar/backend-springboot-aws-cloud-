package com.employees.employeemanager.controller;

import com.employees.employeemanager.constants.EmployeeConstants;
import com.employees.employeemanager.dto.EmployeeDto;
import com.employees.employeemanager.dto.ResponseDto;
import com.employees.employeemanager.entity.Employee;
import com.employees.employeemanager.service.IEmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api",produces = {MediaType.APPLICATION_JSON_VALUE})
public class EmployeeController {
    Logger logger = LogManager.getLogger(EmployeeController.class);
    private final IEmployeeService employeeService;

    @Autowired
    public EmployeeController(IEmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEmployee(@RequestBody EmployeeDto employeeDto){
        logger.info("EmployeeController:create employee execution staqrted ...");
        logger.info("EmployeeController:create employee payload :",employeeDto);
        employeeService.createEmployee(employeeDto);
        logger.info("EmployeeController:create employee execution ended ...");
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(EmployeeConstants.STATUS_201,EmployeeConstants.MESSAGE_201));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteEmployee(@RequestParam Long id){
        logger.info("EmployeeController:delete Employee execution started ...");
        boolean isUpdated = employeeService.deleteEmployee(id);
        if(isUpdated){
            logger.info("EmployeeController:delete Employee execution ended ...");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(EmployeeConstants.STATUS_200,EmployeeConstants.MESSAGE_200));
        }else {
            logger.info("EmployeeController:delete Employee execution failure ...");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(EmployeeConstants.STATUS_417,EmployeeConstants.MESSAGE_417_DELETE));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateEmployee(@RequestParam Long id,@RequestBody EmployeeDto employeeDto){
        logger.info("EmployeeController:update Employee execution started ...");
        logger.info("EmployeeController:update Employee request ...",employeeDto);
        boolean isUpdated = employeeService.updateEmployee(id,employeeDto);
        if (isUpdated){
            logger.info("EmployeeController:update Employee execution ended ...");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(EmployeeConstants.STATUS_200,EmployeeConstants.MESSAGE_200));
        }else {
            logger.info("EmployeeController:update Employee execution failure ...");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(EmployeeConstants.STATUS_417,EmployeeConstants.MESSAGE_417_UPDATE));
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> fetchAllEmployees(){
        logger.info("EmployeeController:fetch all employee execution started ...");
        List<Employee> employees = employeeService.fetchEmployees();
        logger.info("EmployeeController:fetch all employee execution ended ...",employees);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/fetch")
    public ResponseEntity<Employee> fetchEmployee(@RequestParam Long id){
        logger.info("EmployeeController:fetch employee execution started ...");
        Employee employee = employeeService.fetchEmployee(id);
        logger.info("EmployeeController:fetch employee execution ended ...",employee);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }
}
