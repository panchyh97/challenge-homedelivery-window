const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? '/api';

export class ApiError extends Error {
  status: number;
  fields?: Record<string, string>;

  constructor(status: number, message: string, fields?: Record<string, string>) {
    super(message);
    this.status = status;
    this.fields = fields;
  }
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response;
  try {
    response = await fetch(`${BASE_URL}${path}`, init);
  } catch {
    throw new ApiError(0, 'No se pudo conectar con el servidor. Intenta nuevamente en unos minutos.');
  }

  const body = await response.json().catch(() => null);
  if (!response.ok) {
    const fallback = response.status >= 500
      ? 'El servicio no está disponible en este momento.'
      : 'No se pudo completar la solicitud.';
    throw new ApiError(response.status, body?.message ?? fallback, body?.fields ?? undefined);
  }
  return body.data as T;
}

export const http = {
  get: <T>(path: string, params?: Record<string, string>) => {
    const query = params ? `?${new URLSearchParams(params)}` : '';
    return request<T>(`${path}${query}`);
  },
  post: <T>(path: string, payload: unknown) =>
    request<T>(path, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    }),
};