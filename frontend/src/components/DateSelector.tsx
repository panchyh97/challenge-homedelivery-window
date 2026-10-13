import type { DateAvailability } from '../api/types'
import { dayMonthLabel, weekdayLabel } from '../lib/format'

interface Props {
  dates: DateAvailability[]
  selected: string | null
  onSelect: (date: string) => void
}

export function DateSelector({ dates, selected, onSelect }: Props) {
  return (
    <ul className="grid grid-cols-7 gap-1 px-4">
      {dates.map(({ date, soldOut }) => {
        const isSelected = date === selected
        return (
          <li key={date} className="flex flex-col items-center gap-1">
            <span className={`text-sm font-bold ${soldOut ? 'text-gray-400' : 'text-gray-900'}`}>
              {weekdayLabel(date)}
            </span>
            <button
              type="button"
              disabled={soldOut}
              aria-pressed={isSelected}
              onClick={() => onSelect(date)}
              className={`flex h-11 w-11 items-center justify-center rounded-full border text-xs ${
                isSelected
                  ? 'border-brand bg-brand font-bold text-white'
                  : soldOut
                    ? 'border-transparent bg-gray-200 text-gray-500'
                    : 'border-brand bg-white text-gray-900'
              }`}
            >
              {dayMonthLabel(date)}
            </button>
            {soldOut && <span className="text-[11px] text-gray-500">Agotado</span>}
          </li>
        )
      })}
    </ul>
  )
}
