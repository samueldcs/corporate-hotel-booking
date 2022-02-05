class HotelService {

    List<Hotel> hotels = []

    void addHotel(hotelId, hotelName) {
        hotels.add(new Hotel(hotelId, hotelName))
    }
    void setRoom(hotelId, number, roomType) {}
    def findHotelBy(hotelId) {
        hotels.find {it.id == hotelId}
    }
}
