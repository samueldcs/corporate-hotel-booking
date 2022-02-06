import groovy.transform.NamedParam
import groovy.transform.NamedParams

import javax.naming.Name
import java.util.stream.Collectors

import static java.util.stream.Collectors.*

class CompanyService {
    List<Employee> employees = []

    void addEmployee(Integer companyId, Integer employeeId) {
        def existingEmployee = employees.find {it.id == employeeId}
        if(!existingEmployee) {
            employees << new Employee(companyId, employeeId)
        } else {
            throw new IllegalArgumentException()
        }
    }

    void deleteEmployee(employeeId) {
        employees = employees.stream()
                .filter {it.id == employeeId}
                .collect(toList())
    }
}
