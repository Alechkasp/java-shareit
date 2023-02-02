package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                             LocalDateTime t1, LocalDateTime t2);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findByItemIdOrderByStartDesc(Long itemId);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                                 LocalDateTime t1, LocalDateTime t2);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status waiting);

    List<Booking> findAllByItemIdAndBookerIdAndEndBefore(Long itemId, Long userId, LocalDateTime now);

    List<Booking> findAllByItemIdOrderByStartDesc(Long itemId);
}
