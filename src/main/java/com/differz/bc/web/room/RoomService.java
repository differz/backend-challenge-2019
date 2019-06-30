package com.differz.bc.web.room;

import com.differz.bc.core.Room;
import com.differz.bc.core.User;
import com.differz.bc.dao.RoomRepository;
import com.differz.bc.web.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final UserService userService;

    public List<RoomDto> findAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::mapToRoomDto)
                .collect(Collectors.toList());
    }

    public Optional<RoomDto> findRoomById(UUID id) {
        return roomRepository.findById(id)
                .map(roomMapper::mapToRoomDto);
    }

    public RoomDto createRoom(RoomInputDto roomInputDto) {
        String roomName = roomInputDto.getName();
        UUID creatorId = roomInputDto.getCreatorId();
        userService.getUserByIdOrThrow(creatorId);

        Room room = roomRepository.findFirstByNameAndCreatorId(roomName, creatorId)
                .orElse(new Room());
        room.setName(roomName);
        room.setCreatorId(creatorId);
        room.setCreatedAt(getCreatedAt());
        roomRepository.save(room);
        return roomMapper.mapToRoomDto(room);
    }

    public void deleteRoom(UUID id, UUID creatorId) {
        userService.getUserByIdOrThrow(creatorId);
        Room room = roomRepository.findByIdAndCreatorId(id, creatorId)
                .orElseThrow(() -> new NoSuchElementException("can't get room id " + id + " for " + creatorId));
        roomRepository.delete(room);
    }

    @Transactional
    public void joinRoom(UUID roomId, UUID userId) {
        User user = userService.getUserByIdOrThrow(userId);
        Room room = getRoomByIdOrThrow(roomId);
        Set<User> users = room.getUsers();
        if (!users.add(user)) {
            throw new RuntimeException("user " + userId + " already joined to room id " + roomId);
        }
        roomRepository.save(room);
    }

    @Transactional
    public void leaveRoom(UUID roomId, UUID userId) {
        User user = userService.getUserByIdOrThrow(userId);
        Room room = getRoomByIdOrThrow(roomId);
        Set<User> users = room.getUsers();
        if (!users.remove(user)) {
            throw new RuntimeException("user " + userId + " hasn't joined to room id " + roomId);
        }
        roomRepository.save(room);
    }

    public boolean isUserInRoom(UUID userId, UUID roomId) {
        User user = userService.getUserByIdOrThrow(userId);
        Room room = getRoomByIdOrThrow(roomId);
        Set<User> users = room.getUsers();
        return users.contains(user);
    }

    public Room getRoomByIdOrThrow(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("can't get room id " + roomId));
    }

    private Instant getCreatedAt() {
        return Instant.now().truncatedTo(SECONDS);
    }
}
