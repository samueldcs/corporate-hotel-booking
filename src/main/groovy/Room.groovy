class Room {
    RoomType type
    int number

    Room(int number, RoomType type) {
        this.number = number
        this.type = type
    }

    @Override
    boolean equals(final o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        final Room room = (Room) o

        if (number != room.number) return false
        if (type != room.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (type != null ? type.hashCode() : 0)
        result = 31 * result + number
        return result
    }
}
