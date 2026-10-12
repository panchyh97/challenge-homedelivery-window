-- Zones
INSERT INTO zone (name) VALUES ('Zona Norte');
INSERT INTO zone (name) VALUES ('Zona Centro');

-- Communes (each belongs to exactly one zone)
INSERT INTO commune (name, zone_id) VALUES ('La Serena', (SELECT id FROM zone WHERE name = 'Zona Norte'));
INSERT INTO commune (name, zone_id) VALUES ('Coquimbo', (SELECT id FROM zone WHERE name = 'Zona Norte'));
INSERT INTO commune (name, zone_id) VALUES ('Santiago', (SELECT id FROM zone WHERE name = 'Zona Centro'));
INSERT INTO commune (name, zone_id) VALUES ('Providencia', (SELECT id FROM zone WHERE name = 'Zona Centro'));
INSERT INTO commune (name, zone_id) VALUES ('Las Condes', (SELECT id FROM zone WHERE name = 'Zona Centro'));

-- Delivery windows: every zone, next 7 days (today + 6), 4 time blocks per day, capacity 5
INSERT INTO delivery_window (zone_id, delivery_date, start_time, end_time, price, capacity, reserved_count)
SELECT z.id,
       DATEADD('DAY', d.x, CURRENT_DATE),
       b.start_time,
       b.end_time,
       b.price,
       5,
       0
FROM zone z
CROSS JOIN SYSTEM_RANGE(0, 6) d
CROSS JOIN (VALUES
    (TIME '16:00:00', TIME '18:00:00', 2990),
    (TIME '17:00:00', TIME '19:00:00', 2990),
    (TIME '18:00:00', TIME '20:00:00', 3490),
    (TIME '19:00:00', TIME '21:00:00', 3490)
) AS b(start_time, end_time, price);

-- Sold-out day: every window of Zona Norte, 6 days from today
UPDATE delivery_window
SET reserved_count = capacity
WHERE zone_id = (SELECT id FROM zone WHERE name = 'Zona Norte')
  AND delivery_date = DATEADD('DAY', 6, CURRENT_DATE);

-- Last slot left: Zona Norte, tomorrow, 16:00 block (useful for manual race tests)
UPDATE delivery_window
SET reserved_count = capacity - 1
WHERE zone_id = (SELECT id FROM zone WHERE name = 'Zona Norte')
  AND delivery_date = DATEADD('DAY', 1, CURRENT_DATE)
  AND start_time = TIME '16:00:00';