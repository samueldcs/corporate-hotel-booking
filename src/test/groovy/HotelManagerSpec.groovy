import hotel.HotelService
import hotel.Room
import hotel.RoomType
import spock.lang.Specification

class HotelManagerSpec extends Specification {

    HotelService service = new HotelService()

    def "should save and retrieve persisted hotels"() {
        when:
        def firstHotelId = new Random().nextInt()
        def secondHotelId = new Random().nextInt()
        service.addHotel(firstHotelId, "Fancy hotel.Hotel")
        service.addHotel(secondHotelId, "Ugly hotel.Hotel")

        then:
        def firstHotel = service.findHotelBy(firstHotelId)
        firstHotel.id == firstHotelId
        firstHotel.name == "Fancy hotel.Hotel"

        def secondHotel = service.findHotelBy(secondHotelId)
        secondHotel.id == secondHotelId
        secondHotel.name == "Ugly hotel.Hotel"
    }

    def "shouldn't save hotels with overlapping ids"() {
        given:
        def hotelId = new Random().nextInt()
        service.addHotel(hotelId, "Unique hotel.Hotel")

        when:
        service.addHotel(hotelId, "Another Unique hotel.Hotel?")

        then:
        thrown(IllegalArgumentException)
        service.findHotelBy(hotelId).name == "Unique hotel.Hotel"
    }

    def "shouldn't add rooms to non-existing hotels"() {
        when:
        def hotelId = new Random().nextInt()
        service.setRoom(hotelId, 441, RoomType.SINGLE_BED)

        then:
        thrown(IllegalArgumentException)
    }

    def "should add rooms to existing hotels and retrieve them"() {
        given:
        def hotelId = new Random().nextInt()
        service.addHotel(hotelId, "hotel.Hotel with Rooms")

        when:
        def roomNumber = new Random().nextInt()
        def roomType = Arrays.stream(RoomType.values()).findAny().get()
        service.setRoom(hotelId, roomNumber, roomType)

        then:
        def hotel = service.findHotelBy(hotelId)
        hotel.rooms == [new Room(roomNumber, roomType)]
        hotel.name == "hotel.Hotel with Rooms"
    }

    def "should update an existing room"() {
        given:
        def hotelId = new Random().nextInt()
        def roomNumber = new Random().nextInt()
        service.addHotel(hotelId, "hotel.Hotel to be Updated")
        service.setRoom(hotelId, roomNumber, RoomType.SINGLE_BED)
        service.setRoom(hotelId, 441, RoomType.SINGLE_BED)

        when:
        service.setRoom(hotelId, roomNumber, RoomType.DOUBLE_BED)

        then:
        def hotel = service.findHotelBy(hotelId)
        hotel.rooms == [
                new Room(roomNumber, RoomType.DOUBLE_BED),
                new Room(441, RoomType.SINGLE_BED)
        ]
    }
}
