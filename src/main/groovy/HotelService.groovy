class HotelService {

    List<Hotel> hotels = []

    void addHotel(hotelId, hotelName) {
        if(!hotelExists(hotelId))
            hotels << new Hotel(hotelId, hotelName)
        else
            throw new IllegalArgumentException()
    }

    private boolean hotelExists(int hotelId) {
        findHotelBy(hotelId)
    }

    void setRoom(hotelId, number, roomType) {
        def hotel = findHotelBy(hotelId)
        if(hotel) {
            hotel.addRoom(new Room(number, roomType))
        } else {
            throw new IllegalArgumentException()
        }
    }

    def findHotelBy(hotelId) {
        hotels.find {it.id == hotelId}
    }
}
