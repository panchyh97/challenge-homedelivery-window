import type { Commune } from "../api"

interface Props {
  communes: Commune[]
  value: string | null
  onChange: (name: string) => void
}

export function CommuneSelect({ communes, value, onChange }: Props) {
  return (
    <div className="px-4 pb-4">
      <select
        aria-label="Comuna"
        value={value ?? ''}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-md border border-line bg-white px-3 py-2 text-sm"
      >
        {communes.map((c) => (
          <option key={c.id} value={c.name}>
            {c.name}
          </option>
        ))}
      </select>
    </div>
  )
}