package com.differz.bc.web.room;

import com.differz.bc.core.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoomMapper {

    @Mapping(source = "id", target = "roomId")
    @Mapping(source = "name", target = "roomName")
    RoomDto mapToRoomDto(Room room);

}
