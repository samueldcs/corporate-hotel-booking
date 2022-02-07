package hotel

class Hotel {
    int id
    String name
    private List<Room> rooms

    private List<Booking> bookings
    Hotel(int id, String name) {
        this.name = name
        this.id = id
        this.rooms = []
        this.bookings = []
    }

    List<Booking> getBookings() {
        return bookings
    }

    void addRoom(Room room) {
        def existingRoom = rooms.find {it.number == room.number}
        if(existingRoom) {
            existingRoom.type = room.type
        } else {
            rooms << room
        }
    }

    List<Room> getRooms() {
        return rooms
    }

    boolean allowsBooking(RoomType roomType, Date checkIn, Date checkOut) {
        def rooms = this.rooms.findAll {it.type == roomType}
        def bookedRooms = bookings
                .findAll {it.roomType == roomType}
                .findAll {(it.checkIn <= checkOut) && (it.checkOut >= checkIn)}
        rooms.size() > bookedRooms.size()
    }

    void addBooking(Booking booking) {
        bookings << booking
    }
}
