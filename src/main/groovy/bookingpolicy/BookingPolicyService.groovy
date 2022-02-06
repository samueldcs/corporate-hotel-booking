package bookingpolicy

import company.CompanyService
import hotel.RoomType

class BookingPolicyService {

    private final CompanyService companyService
    List<BookingPolicy> companyPolicies = []
    List<BookingPolicy> employeePolicies = []

    BookingPolicyService(final CompanyService companyService) {
        this.companyService = companyService
    }

    void setCompanyPolicy(Integer companyId, List<RoomType> roomTypes) {
        setPolicy(companyPolicies, companyId, roomTypes)
    }

    void setEmployeePolicy(Integer employeeId, List<RoomType> roomTypes) {
        setPolicy(employeePolicies, employeeId, roomTypes)
    }

    boolean isBookingAllowed(Integer employeeId, RoomType roomType) {
        def employeePolicy = findPolicy(employeePolicies, employeeId)
        if(employeePolicy) {
            return employeePolicy.allows(roomType)
        }
        def companyPolicy = findCompanyPolicy(employeeId)
        if(companyPolicy) {
            return companyPolicy.allows(roomType)
        }
        return true
    }

    private void setPolicy(List<BookingPolicy> policies, Integer targetId, List<RoomType> roomTypes) {
        def currentPolicy = policies.findIndexOf {it.targetId == targetId}
        if(currentPolicy >= 0) {
            policies[currentPolicy] = new BookingPolicy(targetId, roomTypes)
        } else {
            policies << new BookingPolicy(targetId, roomTypes)
        }
    }

    private BookingPolicy findCompanyPolicy(Integer employeeId) {
        def companyId = companyService.findCompanyIdByEmployee(employeeId)
        if(companyId) {
            return findPolicy(companyPolicies, companyId)
        }
        return null
    }

    private BookingPolicy findPolicy(List<BookingPolicy> policies, Integer targetId) {
        return policies.find{it.targetId == targetId}
    }
}
