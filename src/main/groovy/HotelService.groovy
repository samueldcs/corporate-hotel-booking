class HotelService {

    List<Hotel> hotels = []

    void addHotel(hotelId, hotelName) {
        if(!hotelExists(hotelId)) {
            hotels.add(new Hotel(hotelId, hotelName))
        } else throw new IllegalArgumentException()
    }

    private boolean hotelExists(int hotelId) {
        findHotelBy(hotelId)
    }

    void setRoom(hotelId, number, roomType) {}
    def findHotelBy(hotelId) {
        hotels.find {it.id == hotelId}
    }
}
