import type {
  ApiEnvelope,
  Commune,
  CreateReservationRequest,
  DateAvailability,
  DeliveryWindow,
  Reservation,
} from './types'

const BASE_URL = '/api'

export class ApiError extends Error {
  status: number
  fields?: Record<string, string>

  constructor(status: number, message: string, fields?: Record<string, string>) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.fields = fields
  }
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...init,
  })

  const body: ApiEnvelope<T> | null = await response.json().catch(() => null)

  if (!response.ok || body === null) {
    throw new ApiError(
      response.status,
      body?.message ?? 'Unexpected error',
      body?.fields ?? undefined,
    )
  }

  return body.data as T
}

export function getCommunes(): Promise<Commune[]> {
  return request('/communes')
}

export function getAvailableDates(commune: string): Promise<DateAvailability[]> {
  const query = new URLSearchParams({ commune })
  return request(`/available-dates?${query}`)
}

export function getDeliveryWindows(commune: string, date: string): Promise<DeliveryWindow[]> {
  const query = new URLSearchParams({ commune, date })
  return request(`/delivery-windows?${query}`)
}

export function createReservation(body: CreateReservationRequest): Promise<Reservation> {
  return request('/reservations', { method: 'POST', body: JSON.stringify(body) })
}