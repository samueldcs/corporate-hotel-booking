import bookingpolicy.BookingPolicyService
import hotel.Booking
import hotel.EmployeeService
import hotel.Hotel
import hotel.HotelService
import hotel.Room
import spock.lang.Specification

import java.awt.print.Book
import java.time.Instant

import static hotel.RoomType.*

class EmployeeSpec extends Specification {

    public static final int ONE_DAY_IN_SECONDS = 86400
    public static final int TWO_DAYS_IN_SECONDS = ONE_DAY_IN_SECONDS * 2
    HotelService hotelService = Mock()
    BookingPolicyService bookingPolicyService = Mock()
    EmployeeService employeeService = new EmployeeService(hotelService, bookingPolicyService)

    Integer hotelId
    Integer employeeId
    Hotel hotelWithASingleBedRoom
    Hotel hotelWithTwoLuxuryRooms

    void setup() {
        hotelId = new Random().nextInt()
        employeeId = new Random().nextInt()

        hotelWithASingleBedRoom = new Hotel(hotelId, "Mock Hotel with a single bed room")
        hotelWithASingleBedRoom.addRoom(new Room(190, SINGLE_BED))

        hotelWithTwoLuxuryRooms = new Hotel(hotelId, "Full hotel")
        hotelWithTwoLuxuryRooms.addRoom(new Room(405, LUXURY))
        hotelWithTwoLuxuryRooms.addRoom(new Room(404, LUXURY))
    }

    def "should book a room for one day when the hotel exists, has enough room and the policies allow it"() {
        given: "a hotel with one luxury room left"
        def currentBookingCheckIn = Date.from(Instant.ofEpochSecond(5))
        def currentBookingCheckOut = Date.from(Instant.ofEpochSecond(90 + TWO_DAYS_IN_SECONDS))
        def currentBooking = new Booking(1, 10, hotelId, currentBookingCheckIn, currentBookingCheckOut, LUXURY)
        hotelWithTwoLuxuryRooms.addBooking(currentBooking)

        hotelService.findHotelBy(hotelId) >> hotelWithTwoLuxuryRooms
        bookingPolicyService.isBookingAllowed(employeeId, LUXURY) >> true

        when: "a booking for a luxury room is requested"
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        def checkout = Date.from(Instant.ofEpochSecond(10 + ONE_DAY_IN_SECONDS))
        Booking booking = employeeService.book(employeeId, hotelId, LUXURY, checkIn, checkout)

        then:
        1 * hotelService.updateBookingStatus(!null)
        booking.id > 0
        booking.hotelId == hotelId
        booking.checkIn.toInstant() == checkIn.toInstant()
        booking.checkOut.toInstant() == checkout.toInstant()
        booking.employeeId == employeeId
        booking.roomType == LUXURY
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

    def "shouldn't book on a hotel that is overbooked"() {
        given: "a hotel with a two booked single bed rooms"

        def currentBookingCheckIn = Date.from(Instant.ofEpochSecond(5))
        def currentBookingCheckOut = Date.from(Instant.ofEpochSecond(90 + TWO_DAYS_IN_SECONDS))
        def currentBooking = new Booking(1, 10, hotelId, currentBookingCheckIn, currentBookingCheckOut, LUXURY)

        hotelWithTwoLuxuryRooms.addBooking(currentBooking)
        hotelWithTwoLuxuryRooms.addBooking(currentBooking)

        hotelService.findHotelBy(hotelId) >> hotelWithTwoLuxuryRooms
        bookingPolicyService.isBookingAllowed(employeeId, LUXURY) >> true

        when: "a booking is requested while both rooms are already booked"
        Date checkIn = Date.from(Instant.ofEpochSecond(10))
        Date checkOut = Date.from(Instant.ofEpochSecond(10 + ONE_DAY_IN_SECONDS))
        employeeService.book(employeeId, hotelId, SINGLE_BED, checkIn, checkOut)

        then:
        thrown(IllegalArgumentException)
    }
}
