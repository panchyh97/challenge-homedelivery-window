import { useState } from 'react'
import { AddressRow } from '../components/AddressRow'
import { BookingHeader } from '../components/BookingHeader'
import { CommuneSelect } from '../components/CommuneSelect'
import { DateSelector } from '../components/DateSelector'
import { DeliveryModeTabs, type DeliveryMode } from '../components/DeliveryModeTabs'
import { ReserveButton } from '../components/ReserveButton'
import { WindowList } from '../components/WindowList'
import { DUMMY_CUSTOMER } from '../data/dummy'
import { useBooking } from '../hooks/useBooking'
import { MODE_LABEL } from '../lib/labels'

export function BookingPage() {
  const [mode, setMode] = useState<DeliveryMode>('delivery')
  const [editingAddress, setEditingAddress] = useState(false)
  const booking = useBooking(DUMMY_CUSTOMER.street)

  function handleCommuneChange(name: string) {
    booking.selectCommune(name)
    setEditingAddress(false)
  }

  return (
    <div className="mx-auto flex min-h-screen w-full max-w-md flex-col bg-white md:min-h-[680px] md:overflow-hidden md:rounded-xl md:shadow-lg">
      <BookingHeader title="Reservar horario" />
      <DeliveryModeTabs value={mode} onChange={setMode} />
      <AddressRow
        name={DUMMY_CUSTOMER.name}
        address={`${DUMMY_CUSTOMER.street}, ${booking.commune ?? ''}`}
        onChange={() => setEditingAddress((open) => !open)}
      />
      {editingAddress && (
        <CommuneSelect
          communes={booking.communes}
          value={booking.commune}
          onChange={handleCommuneChange}
        />
      )}
      <div className="h-2 bg-gray-100" />
      <p className="px-4 pt-4 pb-3 text-sm text-gray-600">
        Elige tu {MODE_LABEL[mode]} fecha y hora
      </p>
      <DateSelector
        dates={booking.dates}
        selected={booking.selectedDate}
        onSelect={booking.selectDate}
      />
      <div className="flex-1 pt-5 pb-4">
        <WindowList
          windows={booking.windows}
          selectedId={booking.selectedWindowId}
          onSelect={booking.selectWindow}
        />
      </div>
      {booking.error && (
        <p role="alert" className="px-4 pb-2 text-sm text-red-600">
          {booking.error}
        </p>
      )}
      {booking.reservation && (
        <p role="status" className="px-4 pb-2 text-sm text-green-700">
          Reserva confirmada.
        </p>
      )}
      <ReserveButton
        disabled={booking.selectedWindowId === null || booking.submitting}
        onClick={booking.reserve}
      />
    </div>
  )
}