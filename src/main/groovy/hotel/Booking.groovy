package hotel

class Booking {
    Integer id
    Integer employeeId
    Integer hotelId
    Date checkIn
    Date checkOut
    RoomType roomType

    Booking(final Integer id, final Integer employeeId, final Integer hotelId, final Date checkIn, final Date checkOut, final RoomType roomType) {
        this.id = id
        this.employeeId = employeeId
        this.hotelId = hotelId
        this.checkIn = checkIn
        this.checkOut = checkOut
        this.roomType = roomType
    }
}
