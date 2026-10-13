interface Props {
  disabled?: boolean
  onClick: () => void
}

export function ReserveButton({ disabled, onClick }: Props) {
  return (
    <div className="bg-white px-4 pt-4 pb-5 shadow-[0_-8px_12px_-8px_rgba(0,0,0,0.12)]">
      <button
        type="button"
        disabled={disabled}
        onClick={onClick}
        className="w-full rounded-full bg-brand py-3 text-sm font-bold text-white disabled:bg-gray-300"
      >
        Reservar
      </button>
    </div>
  )
}
