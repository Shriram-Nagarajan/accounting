import React,{useState} from 'react';
import MoneyAnimation from '../components/MoneyAnimation';
import IncomeInsightsTable from './IncomeInsightsTable';
import dayjs from 'dayjs';
import DateRangePickerComponent from "../components/DateRangePicker";
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';
import accountingApi from '../httputil/accountingApi';

function Income() {
  const [income, setIncome] = useState([]);
  const [fromDate, setFromDate] = useState(dayjs());
  const [toDate, setToDate] = useState(dayjs());
  const [dataPresent, setDataPresent] = useState(false);
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');

  const handleSubmit = (event) => {
    event.preventDefault();
    let data = { fromDate: fromDate.format('YYYY-MM-DD'), toDate: toDate.format('YYYY-MM-DD') };
    accountingApi.getIncome(data, (response) => {
      setIncome(response.data);
      setDataPresent(true);
      console.log(response.data);
    }, (error) => {
      console.error("Error fetching income", error);
      setDataPresent(false);
      showAlert('Error fetching income','error');
    });
  };
  const handleFromDateChange = (newValue) => {
    setFromDate(newValue);
  };

  const handleToDateChange = (newValue) => {
    setToDate(newValue);
  };
  const showAlert = (message, severity) => {
    setAlertMessage(message);
    setAlertSeverity(severity);
    setAlertOpen(true);
  };
  return (
    <Container >
        <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h4" gutterBottom>
          Income Insights
        </Typography>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={4}>
              <DateRangePickerComponent value={fromDate} onChange={handleFromDateChange} label="From Date" />
            </Grid>
            <Grid item xs={12} sm={4}>
              <DateRangePickerComponent value={toDate} onChange={handleToDateChange} label="To Date" />
            </Grid>
            <Grid item xs={12} sm={4}>
              <Button variant="contained" color="primary" type="submit" fullWidth>
                Submit
              </Button>
            </Grid>
          </Grid>
        </form>

        {dataPresent && (
          <Box mt={4}>
            <Grid container spacing={4}>
              <Grid item xs={12} md={12}>
                <IncomeInsightsTable income={income} />
              </Grid>
              {/* <Grid item xs={12} md={5}>
                <PieChart data={state} title={pieTitle} onSliceClick={handlePieSliceClick} />
              </Grid> */}
            </Grid>
          </Box>
        )}
        {/* <ModalPopup open={open} handleClose={handleClose} data={sliceData} from="einsights" /> */}
        
      </Paper>
      <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
        <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
          {alertMessage}
        </Alert>
      </Snackbar>
      <div >
      <MoneyAnimation />
      {/* Your other app content */}
    </div>
      </Container>
    
  );
}

export default Income;