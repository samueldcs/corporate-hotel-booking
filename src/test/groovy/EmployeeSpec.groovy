import bookingpolicy.BookingPolicyService
import hotel.EmployeeService
import hotel.Hotel
import hotel.HotelService
import spock.lang.Specification

import java.time.Duration
import java.time.Instant

import static hotel.RoomType.*
import static java.time.Instant.now

class EmployeeSpec extends Specification {

    public static final int ONE_DAY_IN_SECONDS = 86400
    HotelService hotelService = Mock()
    BookingPolicyService bookingPolicyService = Mock()
    EmployeeService employeeService = new EmployeeService(hotelService, bookingPolicyService)

    def "should book a room for one day when the hotel exists and policies allow"() {
        def hotelId = new Random().nextInt()
        def employeeId = new Random().nextInt()

        given:
        hotelService.findHotelBy(hotelId) >> new Hotel(hotelId, "Mocked Hotel")
        bookingPolicyService.isBookingAllowed(employeeId, SINGLE_BED) >> true

        when:
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        def checkout = Date.from(Instant.ofEpochSecond(10 + ONE_DAY_IN_SECONDS))
        def booking = employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, checkout)

        then:
        with(booking) {
            it.id > 0
            it.hotelId == hotelId
            it.checkIn.toInstant() == checkIn.toInstant()
            it.checkOut.toInstant() == checkout.toInstant()
            it.employeeId == employeeId
            it.roomType == SINGLE_BED
        }

    }

    def "shouldn't book a room for less than one day"() {
        def hotelId = new Random().nextInt()
        def employeeId = new Random().nextInt()

        given:
        hotelService.findHotelBy(hotelId) >> new Hotel(hotelId, "Mocked Hotel")
        bookingPolicyService.isBookingAllowed(employeeId, SINGLE_BED) >> true

        when:
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        def checkout = Date.from(Instant.ofEpochSecond(50))
        employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, checkout)

        then:
        thrown(IllegalArgumentException)
    }

    private Date tomorrow() {
        Date.from now() + Duration.ofDays(1)
    }
}
