import type { DateAvailability, DeliveryWindow } from '../api/types'

function toIsoDate(date: Date): string {
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${date.getFullYear()}-${month}-${day}`
}

function daysFromToday(offset: number): string {
  const date = new Date()
  date.setDate(date.getDate() + offset)
  return toIsoDate(date)
}

export const DUMMY_DATES: DateAvailability[] = Array.from({ length: 7 }, (_, i) => ({
  date: daysFromToday(i),
  soldOut: i === 6,
  availableSlots: i === 6 ? 0 : 20,
}))

export const DUMMY_WINDOWS: DeliveryWindow[] = [
  { id: 1, startTime: '16:00:00', endTime: '18:00:00', price: 2990, availableSlots: 5, soldOut: false },
  { id: 2, startTime: '17:00:00', endTime: '19:00:00', price: 2990, availableSlots: 5, soldOut: false },
  { id: 3, startTime: '18:00:00', endTime: '20:00:00', price: 2990, availableSlots: 5, soldOut: false },
  { id: 4, startTime: '19:00:00', endTime: '21:00:00', price: 2990, availableSlots: 5, soldOut: false },
]

export const DUMMY_CUSTOMER = {
  name: 'Pedro Perez',
  street: 'Monjitas, 2334, La Serena, Coquimbo',
}
