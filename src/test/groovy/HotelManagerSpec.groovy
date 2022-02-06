import spock.lang.Specification

class HotelManagerSpec extends Specification {

    HotelService service = new HotelService()

    def "should save and retrieve persisted hotels"() {
        when:
        def firstHotelId = new Random().nextInt()
        def secondHotelId = new Random().nextInt()
        service.addHotel(firstHotelId, "Fancy Hotel")
        service.addHotel(secondHotelId, "Ugly Hotel")

        then:
        def firstHotel = service.findHotelBy(firstHotelId)
        firstHotel.id == firstHotelId
        firstHotel.name == "Fancy Hotel"

        def secondHotel = service.findHotelBy(secondHotelId)
        secondHotel.id == secondHotelId
        secondHotel.name == "Ugly Hotel"
    }

    def "shouldn't save hotels with overlapping ids"() {
        given:
        def hotelId = new Random().nextInt()
        service.addHotel(hotelId, "Unique Hotel")

        when:
        service.addHotel(hotelId, "Another Unique Hotel?")

        then:
        thrown(IllegalArgumentException)
        service.findHotelBy(hotelId).name == "Unique Hotel"
    }
}
