import bookingpolicy.BookingPolicyService
import company.CompanyService
import hotel.RoomType
import spock.lang.Specification

class CompanyAdminSpec extends Specification {

    CompanyService companyService = new CompanyService()
    BookingPolicyService bookingPolicyService = new BookingPolicyService(companyService)

    def "should not create employees with conflicting ids"() {
        when:
        def employeeId = new Random().nextInt()
        companyService.addEmployee(190, employeeId)
        companyService.addEmployee(180, employeeId)

        then:
        thrown(IllegalArgumentException)
    }

    def "should delete employees"() {
        given: "we create an employee"
        def employeeId = new Random().nextInt()
        companyService.addEmployee(321, employeeId)

        when: "we delete this employee and create another one with the same id"
        companyService.deleteEmployee(321)
        companyService.addEmployee(321, employeeId)

        then:
        noExceptionThrown()
    }

    def "should allow unrestricted booking when there are no policies"() {
        given: "we create an employee and company with no policies"
        def employeeId = new Random().nextInt()
        companyService.addEmployee(577, employeeId)

        expect:
        RoomType.values()
                .every { bookingPolicyService.isBookingAllowed(employeeId, it) }
    }

    def "should restrict booking according to company policies"() {
        given:
        def companyId = new Random().nextInt()
        companyService.addEmployee(companyId, 999)
        bookingPolicyService.setCompanyPolicy(companyId, [RoomType.SINGLE_BED, RoomType.LUXURY])

        expect:
        bookingPolicyService.isBookingAllowed(999, RoomType.SINGLE_BED)
        bookingPolicyService.isBookingAllowed(999, RoomType.LUXURY)
        !bookingPolicyService.isBookingAllowed(999, RoomType.DOUBLE_BED)
        !bookingPolicyService.isBookingAllowed(999, RoomType.TRIPLE_BED)
    }

    def "should update company policies"() {
        given:
        def companyId = new Random().nextInt()
        companyService.addEmployee(companyId, 999)
        bookingPolicyService.setCompanyPolicy(companyId, [RoomType.SINGLE_BED])
        bookingPolicyService.setCompanyPolicy(companyId, [RoomType.LUXURY, RoomType.DOUBLE_BED])

        expect:
        bookingPolicyService.isBookingAllowed(999, RoomType.LUXURY)
        bookingPolicyService.isBookingAllowed(999, RoomType.DOUBLE_BED)
        !bookingPolicyService.isBookingAllowed(999, RoomType.SINGLE_BED)
    }

    def "should update employee policies"() {
        given:
        def companyId = new Random().nextInt()
        companyService.addEmployee(companyId, 999)
        bookingPolicyService.setCompanyPolicy(companyId, [RoomType.SINGLE_BED])
        bookingPolicyService.setCompanyPolicy(companyId, [RoomType.LUXURY, RoomType.DOUBLE_BED])

        expect:
        bookingPolicyService.isBookingAllowed(999, RoomType.LUXURY)
        bookingPolicyService.isBookingAllowed(999, RoomType.DOUBLE_BED)
        !bookingPolicyService.isBookingAllowed(999, RoomType.SINGLE_BED)
    }

    def "should restrict booking according to employee policies"() {
        given:
        def employeeId = new Random().nextInt()
        companyService.addEmployee(512, employeeId)
        bookingPolicyService.setEmployeePolicy(employeeId, [RoomType.LUXURY, RoomType.SINGLE_BED])
        bookingPolicyService.setEmployeePolicy(employeeId, [])

        expect:
        RoomType.values()
                .every {!bookingPolicyService.isBookingAllowed(employeeId, it)}
    }

    def "should override company policy with employee policy"() {
        given:
        def companyId = new Random().nextInt()
        def employeeId = new Random().nextInt()
        companyService.addEmployee(190, employeeId)
        bookingPolicyService.setCompanyPolicy(companyId, [])
        bookingPolicyService.setEmployeePolicy(employeeId, [RoomType.SINGLE_BED])

        expect:
        bookingPolicyService.isBookingAllowed(employeeId, RoomType.SINGLE_BED)
        !bookingPolicyService.isBookingAllowed(employeeId, RoomType.LUXURY)
        !bookingPolicyService.isBookingAllowed(employeeId, RoomType.TRIPLE_BED)
        !bookingPolicyService.isBookingAllowed(employeeId, RoomType.DOUBLE_BED)
    }
}
