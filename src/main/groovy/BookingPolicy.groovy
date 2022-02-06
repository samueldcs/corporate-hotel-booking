class BookingPolicy {
    Integer targetId
    List<RoomType> allowedRoomTypes

    BookingPolicy(final Integer targetId, final List<RoomType> allowedRoomTypes) {
        this.targetId = targetId
        this.allowedRoomTypes = allowedRoomTypes
    }

    boolean allows(RoomType type) {
        allowedRoomTypes.contains(type)
    }
}
