export interface DateAvailability {
  date: string // ISO yyyy-MM-dd
  soldOut: boolean
  availableSlots: number
}

export interface DeliveryWindow {
  id: number
  startTime: string // HH:mm:ss
  endTime: string // HH:mm:ss
  price: number // CLP, no decimals
  availableSlots: number
  soldOut: boolean
}

export interface ApiEnvelope<T> {
  status: number
  message: string
  data: T | null
  error: string | null
  fields: Record<string, string> | null
}

export interface Commune {
  id: number
  name: string
  zoneName: string
}

export interface CreateReservationRequest {
  deliveryWindowId: number
  orderReference: string
  address: string
  commune: string
}

export interface Reservation {
  id: number
  orderReference: string
  status: string
}
