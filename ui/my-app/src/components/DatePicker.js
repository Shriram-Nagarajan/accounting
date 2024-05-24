import React, { useState } from 'react';

const formatDate = (date) => {
    if (!date) return null;
    const formattedDate = new Date(date).toISOString().slice(0, 10);
    return formattedDate;
  };

const DatePicker = ({onChange, name}) => {
  const [selectedDate, setSelectedDate] = useState(null);

  const handleDateChange = (e) => {
      let date = e.target.value;
      setSelectedDate(date);
      onChange(date);
  };

  return (
    <div>
      <label htmlFor="date">Select a date: </label>
      <input
        type="date"
        id={name}
        name={name}
        value={selectedDate}
        onChange={handleDateChange}
      />
    </div>
  );
};

export default DatePicker;
