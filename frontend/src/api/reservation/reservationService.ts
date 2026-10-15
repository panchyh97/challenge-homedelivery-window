import { http } from '../http';
import type { CreateReservationRequest, Reservation } from './reservation.types';

export const reservationService = {
  create: (request: CreateReservationRequest) => http.post<Reservation>('/reservations', request),
};