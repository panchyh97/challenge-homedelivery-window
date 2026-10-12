package com.challenge.homedelivery;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.challenge.homedelivery.dto.CreateReservationRequest;
import com.challenge.homedelivery.exception.DuplicateOrderException;
import com.challenge.homedelivery.exception.WindowSoldOutException;
import com.challenge.homedelivery.service.ReservationService;

@SpringBootTest
class ReservationConcurrencyTest {
  private static final int THREADS = 50;
  private static final int CAPACITY = 5;

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private JdbcTemplate jdbc;

  private record Outcome(int success, int soldOut, int duplicate, List<Throwable> unexpected) {
  }

  @Test 
  void neverOverbooksWhenManyThreadsCompeteForTheSameWindow() throws Exception {
    Long windowId = createWindow(CAPACITY, 30);

    Outcome outcome = fire(THREADS,
        i -> new CreateReservationRequest(windowId, "CONC-" + i, "Some address " + i));

    assertThat(outcome.unexpected()).isEmpty();
    assertThat(outcome.success()).isEqualTo(CAPACITY);
    assertThat(outcome.soldOut()).isEqualTo(THREADS - CAPACITY);
    assertThat(reservedCount(windowId)).isEqualTo(CAPACITY);
    assertThat(reservedCount(windowId)).isEqualTo(CAPACITY);
  }
  
  @Test
  void sameOrderCanOnlyReserveOnce() throws Exception {
    Long windowId = createWindow(CAPACITY, 31);

    Outcome outcome = fire(20, i -> new CreateReservationRequest(windowId, "SAME-ORDER", "Some address"));

    assertThat(outcome.unexpected()).isEmpty();
    assertThat(outcome.success()).isEqualTo(1);
    assertThat(outcome.duplicate()).isEqualTo(19);
    assertThat(reservedCount(windowId)).isEqualTo(1);
    assertThat(reservationsOf(windowId)).isEqualTo(1);
  }

  private Outcome fire(int threads, IntFunction<CreateReservationRequest> requestFor) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    CountDownLatch ready = new CountDownLatch(threads);
    CountDownLatch go = new CountDownLatch(1);
    AtomicInteger success = new AtomicInteger();
    AtomicInteger soldOut = new AtomicInteger();
    AtomicInteger duplicate = new AtomicInteger();
    Queue<Throwable> unexpected = new ConcurrentLinkedQueue<>();
    List<Future<?>> futures = new ArrayList<>();

    for (int i = 0; i < threads; i++) {
      CreateReservationRequest request = requestFor.apply(i);
      futures.add(pool.submit(() -> {
        ready.countDown();
        try {
          go.await();
          reservationService.reserve(request);
          success.incrementAndGet();
        } catch (WindowSoldOutException e) {
          soldOut.incrementAndGet();
        } catch (DuplicateOrderException e) {
          duplicate.incrementAndGet();
        } catch (Throwable t) {
          unexpected.add(t);
        }
      }));
    }

    ready.await();
    go.countDown();
    for (Future<?> future : futures) {
      future.get(30, TimeUnit.SECONDS);
    }
    pool.shutdown();

    return new Outcome(success.get(), soldOut.get(), duplicate.get(), List.copyOf(unexpected));
  }
  
  private Long createWindow(int capacity, int daysAhead) {
    Date date = Date.valueOf(LocalDate.now().plusDays(daysAhead));
    jdbc.update("""
        INSERT INTO delivery_window
          (zone_id, delivery_date, start_time, end_time, price, capacity, reserved_count)
        SELECT id, CAST(? AS DATE), TIME '10:00:00', TIME '12:00:00', 1000, CAST(? AS INT), 0
        FROM zone WHERE name = 'Zona Norte'
        """, date, capacity);
    return jdbc.queryForObject("""
        SELECT w.id FROM delivery_window w JOIN zone z ON z.id = w.zone_id
        WHERE z.name = 'Zona Norte' AND w.delivery_date = ? AND w.start_time = TIME '10:00:00'
        """, Long.class, date);
  }
  
  private int reservedCount(Long windowId) {
    return jdbc.queryForObject(
        "SELECT reserved_count FROM delivery_window WHERE id = ?", Integer.class, windowId);
  }

  private int reservationsOf(Long windowId) {
    return jdbc.queryForObject(
        "SELECT COUNT(*) FROM reservation WHERE delivery_window_id = ?", Integer.class, windowId);
  }
}