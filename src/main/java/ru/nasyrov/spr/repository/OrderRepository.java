package ru.nasyrov.spr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nasyrov.spr.dto.OrderInfoDTO;
import ru.nasyrov.spr.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.startTime >= :startTime AND o.startTime < :endTime")
    List<Order> findAllByStartTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Optional<Order> findById(String id);

    @Query("SELECT new ru.nasyrov.spr.dto.OrderInfoDTO(o.id, c.name, o.startTime) " +
            "FROM Order o " +
            "INNER JOIN Client c " +
            "ON o.client = c " +
            "WHERE o.startTime >= :startTime AND o.startTime <= :endTime " +
            "AND lower(c.name) LIKE lower(concat('%', :clientName,'%'))")
    List<OrderInfoDTO> getOrdersByDateBetweenAndClientName(@Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime,
                                                           @Param("clientName") String clientName);
}
