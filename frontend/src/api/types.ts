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
