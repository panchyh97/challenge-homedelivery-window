export interface CreateReservationRequest {
  deliveryWindowId: number;
  orderReference: string;
  address: string;
  commune: string;
}

export interface Reservation {
  id: number;
  deliveryWindowId: number;
  orderReference: string;
  status: string;
}