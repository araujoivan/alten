package com.alten.cancun.repository;

import com.alten.cancun.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Eder_Crespo
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    public Room findByNumber(int number);

}
