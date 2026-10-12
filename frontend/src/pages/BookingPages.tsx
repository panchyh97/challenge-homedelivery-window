import { useState } from 'react'
import { AddressRow } from '../components/AddressRow'
import { BookingHeader } from '../components/BookingHeader'
import { DateSelector } from '../components/DateSelector'
import { DeliveryModeTabs, type DeliveryMode } from '../components/DeliveryModeTabs'
import { ReserveButton } from '../components/ReserveButton'
import { WindowList } from '../components/WindowList'
import { DUMMY_CUSTOMER, DUMMY_DATES, DUMMY_WINDOWS } from '../data/dummy'
import { MODE_LABEL } from '../lib/labels'

export function BookingPages() {
  const [mode, setMode] = useState<DeliveryMode>('delivery')
  const [selectedDate, setSelectedDate] = useState<string | null>(DUMMY_DATES[0].date)
  const [selectedWindowId, setSelectedWindowId] = useState<number | null>(null)

  function handleSelectDate(date: string) {
    setSelectedDate(date)
    setSelectedWindowId(null)
  }

  function handleReserve() {
    window.alert(`Dummy: reserva de la ventana ${selectedWindowId} para ${selectedDate}`)
  }

  return (
    <div className="mx-auto flex min-h-screen w-full max-w-md flex-col bg-white md:min-h-[680px] md:overflow-hidden md:rounded-xl md:shadow-lg text-font">
      <BookingHeader title="Reservar horario" />
      <DeliveryModeTabs value={mode} onChange={setMode} />
      <AddressRow name={DUMMY_CUSTOMER.name} address={DUMMY_CUSTOMER.address} />
      <div className="h-2 bg-gray-100" />
      <p className="px-4 pt-4 pb-3 text-sm text-gray-600">
        Elige tu {MODE_LABEL[mode]} fecha y hora
      </p>
      <DateSelector dates={DUMMY_DATES} selected={selectedDate} onSelect={handleSelectDate} />
      <div className="flex-1 pt-5 pb-4">
        <WindowList
          windows={DUMMY_WINDOWS}
          selectedId={selectedWindowId}
          onSelect={setSelectedWindowId}
        />
      </div>
      <ReserveButton disabled={selectedWindowId === null} onClick={handleReserve} />
    </div>
  )
}
