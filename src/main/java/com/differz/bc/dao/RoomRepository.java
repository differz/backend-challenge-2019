package com.differz.bc.dao;

import com.differz.bc.core.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    Optional<Room> findFirstByNameAndCreatorId(String name, UUID creatorId);

    Optional<Room> findByIdAndCreatorId(UUID id, UUID creatorId);

}
