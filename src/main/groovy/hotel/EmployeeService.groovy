package hotel

import bookingpolicy.BookingPolicyService

import java.time.temporal.ChronoUnit

class EmployeeService {

    private final HotelService hotelService
    private final BookingPolicyService bookingPolicyService

    EmployeeService(final HotelService hotelService, final BookingPolicyService bookingPolicyService) {
        this.hotelService = hotelService
        this.bookingPolicyService = bookingPolicyService
    }

    Booking book(Integer employeeId, Integer hotelId, RoomType roomType, Date checkIn, Date checkOut) {
        def hotel = hotelService.findHotelBy(hotelId)

        def policyAllows = bookingPolicyService.isBookingAllowed(employeeId, roomType)
        if(hotel && isBookingForAtLeastOneDay(checkIn, checkOut) && policyAllows) {
            def hotelOffersRoom = hotel.rooms.any {it.type == roomType}
            if(hotelOffersRoom) {
                return new Booking(
                        new Random().nextInt(Integer.MAX_VALUE),
                        employeeId,
                        hotelId,
                        checkIn,
                        checkOut,
                        roomType
                )
            }
        }
        throw new IllegalArgumentException()
    }

    private boolean isBookingForAtLeastOneDay(Date checkIn, Date checkOut) {
        ChronoUnit.DAYS.between(checkIn.toInstant(), checkOut.toInstant()) >= 1
    }
}
