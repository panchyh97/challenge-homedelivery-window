import { http } from '../http';
import type { Commune, DateAvailability, DeliveryWindow } from './delivery.types';

export const deliveryService = {
  getCommunes: () => http.get<Commune[]>('/communes'),
  getAvailableDates: (commune: string) =>
    http.get<DateAvailability[]>('/available-dates', { commune }),
  getDeliveryWindows: (commune: string, date: string) =>
    http.get<DeliveryWindow[]>('/delivery-windows', { commune, date }),
};