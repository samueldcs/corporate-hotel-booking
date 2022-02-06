import bookingpolicy.BookingPolicyService
import hotel.EmployeeService
import hotel.Hotel
import hotel.HotelService
import hotel.Room
import hotel.RoomType
import spock.lang.Specification

import java.time.Instant

import static hotel.RoomType.*

class EmployeeSpec extends Specification {

    public static final int ONE_DAY_IN_SECONDS = 86400
    HotelService hotelService = Mock()
    BookingPolicyService bookingPolicyService = Mock()
    EmployeeService employeeService = new EmployeeService(hotelService, bookingPolicyService)

    Integer hotelId
    Integer employeeId
    Hotel hotelWithASingleBedRoom

    void setup() {
        hotelId = new Random().nextInt()
        employeeId = new Random().nextInt()
        hotelWithASingleBedRoom = new Hotel(hotelId,"Mock Hotel with a single bed room")
        hotelWithASingleBedRoom.addRoom(new Room(190, SINGLE_BED))
    }

    def "should book a room for one day when the hotel exists and policies allow"() {
        given:
        hotelService.findHotelBy(hotelId) >> hotelWithASingleBedRoom
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
        given:
        hotelService.findHotelBy(hotelId) >> hotelWithASingleBedRoom
        bookingPolicyService.isBookingAllowed(employeeId, SINGLE_BED) >> true

        when:
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        def checkout = Date.from(Instant.ofEpochSecond(50))
        employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, checkout)

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldn't book a room if policies don't allow it"() {
        given:
        hotelService.findHotelBy(hotelId) >> hotelWithASingleBedRoom
        bookingPolicyService.isBookingAllowed(employeeId, SINGLE_BED) >> false

        when:
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, new Date())

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldn't book on a non-existing hotel"() {
        given:
        hotelService.findHotelBy(hotelId) >> null
        bookingPolicyService.isBookingAllowed(employeeId, SINGLE_BED) >> true

        when:
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, new Date())

        then:
        thrown(IllegalArgumentException)
    }

    def "shouldn't book on a hotel that doesn't offer the requested room"() {
        given: "a hotel with a single bed room"
        hotelService.findHotelBy(hotelId) >> hotelWithASingleBedRoom
        bookingPolicyService.isBookingAllowed(employeeId, LUXURY) >> true

        when: "a booking for a luxury room is requested"
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        employeeService.book(employeeId, hotelId, LUXURY, checkIn, new Date())

        then:
        thrown(IllegalArgumentException)
    }
}
