import { ChevronLeftIcon, CloseIcon } from './icons'

interface Props {
  title: string
  onBack?: () => void
  onClose?: () => void
}

export function BookingHeader({ title, onBack, onClose }: Props) {
  return (
    <header className="flex h-14 items-center gap-3 bg-brand px-4 text-white">
      <button type="button" onClick={onBack} aria-label="Volver" className="-ml-1 p-1">
        <ChevronLeftIcon className="h-6 w-6" />
      </button>
      <h1 className="flex-1 text-lg font-bold">{title}</h1>
      <button type="button" onClick={onClose} aria-label="Cerrar" className="p-1">
        <CloseIcon className="h-6 w-6" />
      </button>
    </header>
  )
}
