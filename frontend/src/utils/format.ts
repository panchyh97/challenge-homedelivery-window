import type { DeliveryWindow } from "../api"

const WEEKDAYS = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb']

// new Date("2026-10-12") parses as UTC midnight, which is the previous day in
// Chile. Build the date from its parts so it stays in local time.
export function parseLocalDate(iso: string): Date {
  const [year, month, day] = iso.split('-').map(Number)
  return new Date(year, month - 1, day)
}

function isSameDay(a: Date, b: Date): boolean {
  return (
    a.getFullYear() === b.getFullYear() &&
    a.getMonth() === b.getMonth() &&
    a.getDate() === b.getDate()
  )
}

export function weekdayLabel(iso: string, today: Date = new Date()): string {
  const date = parseLocalDate(iso)
  return isSameDay(date, today) ? 'Hoy' : WEEKDAYS[date.getDay()]
}

export function dayMonthLabel(iso: string): string {
  const date = parseLocalDate(iso)
  return `${date.getDate()}/${date.getMonth() + 1}`
}

export function timeLabel(time: string): string {
  const [hours, minutes] = time.split(':').map(Number)
  const suffix = hours >= 12 ? 'pm' : 'am'
  const hours12 = hours % 12 === 0 ? 12 : hours % 12
  return `${hours12}:${String(minutes).padStart(2, '0')}${suffix}`
}

export function windowLabel(window: DeliveryWindow): string {
  return `${timeLabel(window.startTime)}-${timeLabel(window.endTime)}`
}

export function priceLabel(price: number): string {
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP',
  }).format(price)
}
