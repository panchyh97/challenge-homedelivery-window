import type { DeliveryWindow } from '../api'
import { priceLabel, windowLabel } from '../utils/format'

interface Props {
  windows: DeliveryWindow[]
  selectedId: number | null
  onSelect: (id: number) => void
}

export function WindowList({ windows, selectedId, onSelect }: Props) {
  return (
    <ul className="space-y-3 px-4">
      {windows.map((window) => (
        <li key={window.id}>
          <label className="flex cursor-pointer items-center gap-3 rounded-md border border-line bg-white px-4 py-4 text-sm text-gray-900 has-[:checked]:border-brand has-[:checked]:bg-brand-soft has-[:disabled]:cursor-not-allowed has-[:disabled]:opacity-50">
            <input
              type="radio"
              name="delivery-window"
              className="peer sr-only"
              checked={selectedId === window.id}
              disabled={window.soldOut}
              onChange={() => onSelect(window.id)}
            />
            <span
              aria-hidden="true"
              className="h-5 w-5 shrink-0 rounded-full border-2 border-gray-500 bg-white peer-checked:border-[6px] peer-checked:border-brand peer-focus-visible:ring-2 peer-focus-visible:ring-brand peer-focus-visible:ring-offset-2"
            />
            <span className="flex-1">{windowLabel(window)}</span>
            <span>{priceLabel(window.price)}</span>
          </label>
        </li>
      ))}
    </ul>
  )
}
