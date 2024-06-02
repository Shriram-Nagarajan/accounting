import React from 'react';
import { Box } from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';

function DateRangePickerComponent(props) {
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                <DatePicker
                    label={props.label}
                    value={props.value ? dayjs(props.value) : null}
                    onChange={(newValue) => {
                        props.onChange(newValue ? dayjs(newValue) : null);
                    }}
                />
            </Box>
        </LocalizationProvider>
    );
}

export default DateRangePickerComponent;
