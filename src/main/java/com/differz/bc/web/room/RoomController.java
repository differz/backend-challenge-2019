package com.differz.bc.web.room;

import com.differz.bc.dto.ResultDto;
import com.differz.bc.web.user.UserIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private static final String ROOM_CREATED = "room created successfully";
    private static final String ROOM_REMOVED = "room removed successfully";
    private static final String USER_JOINED = "user successfully joined the room";
    private static final String USER_LEFT = "user successfully left the room";

    private final RoomService roomService;

    @GetMapping("/rooms")
    public List<RoomDto> getRooms() {
        return roomService.findAllRooms();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable UUID id) {
        return roomService.findRoomById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/rooms")
    public RoomDto createRoom(@RequestBody RoomInputDto loginInputDto) {
        RoomDto roomDto = roomService.createRoom(loginInputDto);
        log.debug(ROOM_CREATED);
        return roomDto;
    }

    @DeleteMapping("/rooms/{id}")
    public ResultDto deleteRoom(@PathVariable UUID id, @RequestBody UserIdDto userIdDto) {
        roomService.deleteRoom(id, userIdDto.getUserId());
        log.debug(ROOM_REMOVED);
        return new ResultDto(ROOM_REMOVED);
    }

    @PostMapping("/rooms/{id}/join")
    public ResultDto joinRoom(@PathVariable UUID id, @RequestBody UserIdDto userIdDto) {
        roomService.joinRoom(id, userIdDto.getUserId());
        log.debug(USER_JOINED);
        return new ResultDto(USER_JOINED);
    }

    @PostMapping("/rooms/{id}/leave")
    public ResultDto leaveRoom(@PathVariable UUID id, @RequestBody UserIdDto userIdDto) {
        roomService.leaveRoom(id, userIdDto.getUserId());
        log.debug(USER_LEFT);
        return new ResultDto(USER_LEFT);
    }
}
