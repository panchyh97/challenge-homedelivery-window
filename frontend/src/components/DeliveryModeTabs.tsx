import { CarIcon, HomeIcon } from './icons'

export type DeliveryMode = 'pickup' | 'delivery'

interface Props {
  value: DeliveryMode
  onChange: (mode: DeliveryMode) => void
}

const OPTIONS = [
  { mode: 'pickup', label: 'Retiro Pickup', Icon: CarIcon },
  { mode: 'delivery', label: 'Despacho a domicilio', Icon: HomeIcon },
] as const

export function DeliveryModeTabs({ value, onChange }: Props) {
  return (
    <div
      role="radiogroup"
      aria-label="Tipo de entrega"
      className="grid grid-cols-2 gap-3 px-4 pt-4 pb-3"
    >
      {OPTIONS.map(({ mode, label, Icon }) => {
        const selected = value === mode
        return (
          <button
            key={mode}
            type="button"
            role="radio"
            aria-checked={selected}
            onClick={() => onChange(mode)}
            className={`flex flex-col items-center gap-2 rounded-lg border-2 px-3 py-4 text-sm ${
              selected
                ? 'border-brand bg-brand-soft font-bold text-gray-900'
                : 'border-gray-200 bg-white text-gray-600'
            }`}
          >
            <Icon className={`h-7 w-7 ${selected ? 'text-brand' : 'text-gray-500'}`} />
            {label}
          </button>
        )
      })}
    </div>
  )
}
