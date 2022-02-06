import static java.util.stream.Collectors.*

class CompanyService {
    List<Employee> employees = []

    void addEmployee(Integer companyId, Integer employeeId) {
        def existingEmployee = findEmployee(employeeId)
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

    Integer findCompanyIdByEmployee(int employeeId) {
        return employees.find {it.id == employeeId}?.companyId
    }

    private Employee findEmployee(int employeeId) {
        employees.find { it.id == employeeId }
    }
}
