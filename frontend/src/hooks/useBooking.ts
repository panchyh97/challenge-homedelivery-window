import { useEffect, useState } from 'react'
import { type Commune, type DateAvailability, type DeliveryWindow, type Reservation, ApiError, deliveryService, reservationService } from '../api'

function messageOf(error: unknown): string {
  return error instanceof Error ? error.message : 'Error inesperado'
}

export function useBooking(streetAddress: string) {
  const [communes, setCommunes] = useState<Commune[]>([])
  const [commune, setCommune] = useState<string | null>(null)
  const [dates, setDates] = useState<DateAvailability[]>([])
  const [selectedDate, setSelectedDate] = useState<string | null>(null)
  const [windows, setWindows] = useState<DeliveryWindow[]>([])
  const [selectedWindowId, setSelectedWindowId] = useState<number | null>(null)
  const [reservation, setReservation] = useState<Reservation | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [submitting, setSubmitting] = useState(false)
  // Bumping this re-runs the data effects (used after a reservation attempt)
  const [version, setVersion] = useState(0)

  // Communes
  useEffect(() => {
    let cancelled = false
    deliveryService.getCommunes()
      .then((data) => {
        if (cancelled) return
        setCommunes(data)
        setCommune((current) => current ?? data[0]?.name ?? null)
      })
      .catch((e) => !cancelled && setError(messageOf(e)))
    return () => {
      cancelled = true
    }
  }, [])

  // Available dates
  useEffect(() => {
    if (!commune) return
    let cancelled = false
    deliveryService.getAvailableDates(commune)
      .then((data) => {
        if (cancelled) return
        setDates(data)
        setSelectedDate((previous) => {
          const stillValid = data.some((d) => d.date === previous && !d.soldOut)
          return stillValid ? previous : (data.find((d) => !d.soldOut)?.date ?? null)
        })
      })
      .catch((e) => !cancelled && setError(messageOf(e)))
    return () => {
      cancelled = true
    }
  }, [commune, version])

  useEffect(() => {
    if (!commune || !selectedDate) return
    let cancelled = false
    deliveryService.getDeliveryWindows(commune, selectedDate)
      .then((data) => {
        if (cancelled) return
        setWindows(data)
        setSelectedWindowId((previous) =>
          data.some((w) => w.id === previous && !w.soldOut) ? previous : null,
        )
      })
      .catch((e) => !cancelled && setError(messageOf(e)))
    return () => {
      cancelled = true
    }
  }, [commune, selectedDate, version])

  function selectCommune(name: string) {
    setCommune(name)
    setDates([])
    setSelectedDate(null)
    setWindows([])
    setSelectedWindowId(null)
    setReservation(null)
    setError(null)
  }

  function selectDate(date: string) {
    setSelectedDate(date)
    setSelectedWindowId(null)
    setReservation(null)
    setError(null)
  }

  async function reserve() {
    if (selectedWindowId === null || !commune) return
    setSubmitting(true)
    setError(null)
    setReservation(null)
    try {
      const result = await reservationService.create({
        deliveryWindowId: selectedWindowId,
        orderReference: crypto.randomUUID(),
        address: `${streetAddress}, ${commune}`,
        commune
      })
      setReservation(result)
    } catch (e) {
      setError(
        e instanceof ApiError && e.status === 409
          ? 'Esa ventana se agotó justo ahora. Elige otra.'
          : messageOf(e),
      )
    } finally {
      setSubmitting(false)
      setVersion((v) => v + 1)
    }
  }

  return {
    communes, commune, dates, selectedDate, windows, selectedWindowId,
    reservation, error, submitting,
    selectCommune, selectDate, selectWindow: setSelectedWindowId, reserve,
  }
}