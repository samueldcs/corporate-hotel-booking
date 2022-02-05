import spock.lang.Specification

class HotelServiceTest extends Specification {

    HotelService service = new HotelService()

    def "should be instantiated"() {
        expect: service != null
    }
}
