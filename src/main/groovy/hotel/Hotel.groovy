package hotel

class Hotel {
    int id
    String name
    private List<Room> rooms

    Hotel(int id, String name) {
        this.name = name
        this.id = id
        this.rooms = new ArrayList<>()
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
}
