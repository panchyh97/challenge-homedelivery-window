export interface Commune {
  id: number;
  name: string;
  zoneName: string;
}

export interface DateAvailability {
  date: string; // yyyy-MM-dd
  soldOut: boolean;
  availableSlots: number;
}

export interface DeliveryWindow {
  id: number;
  startTime: string; // HH:mm:ss
  endTime: string;   // HH:mm:ss
  price: number;     // CLP
  availableSlots: number;
  soldOut: boolean;
}