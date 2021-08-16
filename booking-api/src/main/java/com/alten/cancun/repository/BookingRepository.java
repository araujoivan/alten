package com.alten.cancun.repository;

import com.alten.cancun.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Eder_Crespo
 */
public interface BookingRepository extends JpaRepository<Reservation, Long>{

    public Reservation findByCode(String code);
    
    public List<Reservation> findByRoom_Number(int roomNumber);

    public boolean existsByCode(String code);

    public void deleteByCode(String code);
}