import spock.lang.Specification

class HotelServiceTest extends Specification {

    HotelService service = new HotelService()

    def "should be instantiated"() {
        expect:
        service != null
    }

    def "should save and find an existing hotel"() {
        when:
        service.addHotel(10, "Happy Hotel")

        then:
        def returnedHotel = service.findHotelBy(10)
        returnedHotel.id == 10
        returnedHotel.name == "Happy Hotel"
    }

    def "should save and find multiple hotels"() {
        when:
        def firstHotelId = new Random().nextInt()
        def secondtHotelId = new Random().nextInt()
        service.addHotel(firstHotelId, "Fancy Hotel")
        service.addHotel(secondtHotelId, "Ugly Hotel")

        then:
        def firstHotel = service.findHotelBy(firstHotelId)
        firstHotel.id == firstHotelId
        firstHotel.name == "Fancy Hotel"

        def secondHotel = service.findHotelBy(secondtHotelId)
        secondHotel.id == secondtHotelId
        secondHotel.name == "Ugly Hotel"
    }
}
