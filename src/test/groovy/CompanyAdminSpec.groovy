import spock.lang.Specification

class CompanyAdminSpec extends Specification {

    CompanyService service = new CompanyService()

    def "should not create employees with conflicting ids"() {
        when:
        def employeeId = new Random().nextInt()
        service.addEmployee(190, employeeId)
        service.addEmployee(180, employeeId)

        then:
        thrown(IllegalArgumentException)
    }

    def "should delete employees"() {
        given: "we create an employee"
        def employeeId = new Random().nextInt()
        service.addEmployee(321, employeeId)

        when: "we delete this employee and create another one with the same id"
        service.deleteEmployee(321)
        service.addEmployee(321, employeeId)

        then:
        noExceptionThrown()
    }

}
