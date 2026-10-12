interface Props {
  name: string
  address: string
  onChange?: () => void
}

export function AddressRow({ name, address, onChange }: Props) {
  return (
    <section className="flex items-end justify-between gap-4 px-4 pb-4">
      <div className="min-w-0">
        <p className="text-sm font-bold text-gray-900">{name}</p>
        <p className="truncate text-xs text-gray-600">{address}</p>
      </div>
      <button type="button" onClick={onChange} className="shrink-0 text-xs text-gray-900 underline">
        Cambiar
      </button>
    </section>
  )
}
