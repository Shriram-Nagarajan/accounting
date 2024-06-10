//date picker component
import React from 'react';
import { Box, TextField } from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';

function DateRangePickerComponent(props) {
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Box sx={{ display: 'flex', }}>
                <DatePicker
                    label={props.label}
                    value={props.value ? dayjs(props.value) : null}
                    onChange={(newValue) => {
                        props.onChange(newValue ? dayjs(newValue) : null);
                    }}
                    views={['year', 'month', 'day']} // Specify the views here
                    openTo="day" // Open the picker to the year view initially
                    renderInput={(params) => (
                        <TextField
                            {...params}
                            sx={{ width: '100%' }}
                            error={props.error}
                            helperText={props.helperText}
                        />
                    )}
                />
            </Box>
        </LocalizationProvider>
    );
}

export default DateRangePickerComponent;
