import React, { useState } from "react";
import accountingApi from "../httputil/accountingApi";
import DateRangePickerComponent from "../components/DateRangePicker";
import CategoryWiseExpensesTable from '../components/CategoryWiseExpensesTable';
import ExpenseFileUploadForm from '../components/ExpenseFileUploadForm';
import PieChart from '../components/pie';
import ModalPopup from '../components/Modal';
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from 'chart.js';
import dayjs from 'dayjs';
import Loader from '../components/Loader';

ChartJS.register(ArcElement, Tooltip, Legend, Title);

function ExpenseInsights()
{
    const [expenses, setExpenses] = useState([]);
    const [fromDate, setFromDate] = useState(dayjs());
    const [toDate, setToDate] = useState(dayjs());
    const [dataPresent, setDataPresent] = useState(false);
    const [pieTitle, setPieTitle] = useState("");
    const [open, setOpen] = useState(false);
    const [sliceData, setSliceData] = useState(null);
    const [alertOpen, setAlertOpen] = useState(false);
    const [alertMessage, setAlertMessage] = useState('');
    const [alertSeverity, setAlertSeverity] = useState('success');
    const [loading, setLoading] = useState(false);
    const handleFromDateChange = (newValue) => {
        setFromDate(newValue);
      };
    
      const handleToDateChange = (newValue) => {
        setToDate(newValue);
      };
    
      const handleClose = () => {
        setOpen(false);
      };
      const showAlert = (message, severity) => {
        setAlertMessage(message);
        setAlertSeverity(severity);
        setAlertOpen(true);
      };
    
    const handleSubmit = (event) => {
        event.preventDefault();
        setLoading(true);
        let data = { fromDate: fromDate.format('YYYY-MM-DD'), toDate: toDate.format('YYYY-MM-DD') };
        accountingApi.getCategoryWiseExpenses(data, (response) => {
          setExpenses(response.data);
          setDataPresent(true);
          setPieTitle("Expenditure for the period of " + fromDate.format('YYYY-MM-DD') + " to " + toDate.format('YYYY-MM-DD'));
          console.log(response.data);
          setLoading(false);
        }, (error) => {
          console.error("Error fetching category wise expenses", error);
          setDataPresent(false);
          showAlert('Error fetching category wise expenses','error');
           setLoading(false);
        });
      };
    
      const handlePieSliceClick = (data) => {
        console.log(data);
        var catId = expenses.find(x => x.categoryName === data).categoryId;
        console.log(catId);
        setLoading(true);
        accountingApi.getExpenses({ categoryId: catId, fromDate: fromDate.format('YYYY-MM-DD'), toDate: toDate.format('YYYY-MM-DD') }, (response) => {
          setLoading(false);
          console.log("getExpenses: " + JSON.stringify(response.data));
          setSliceData(response.data);
          setOpen(true);
        }, (error) => {
          setLoading(false);
          console.error(error);
          showAlert('Error fetching expenses','error');
        });
      };
    
      const state = {
        labels: expenses.map(x => x.categoryName),
        datasets: [
          {
            label: 'Expenditure',
            backgroundColor: [
              '#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5',
              '#2196F3', '#03A9F4', '#00BCD4', '#009688', '#4CAF50',
              '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107', '#FF9800',
              '#FF5722', '#795548', '#9E9E9E', '#607D8B', '#000000'
            ],
            borderWidth: 3,
            hoverOffset: 20,
            data: expenses.map(x => x.totalExpenditure)
          }
        ]
      };
    return(
     
        <Container >
           {loading ? (<Loader />) :(
            <>
        <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h4" gutterBottom>
          Expense Insights
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
              <Grid item xs={12} md={7}>
                <CategoryWiseExpensesTable expenses={expenses} />
              </Grid>
              <Grid item xs={12} md={5}>
                <PieChart data={state} title={pieTitle} onSliceClick={handlePieSliceClick} />
              </Grid>
            </Grid>
          </Box>
        )}
        <ModalPopup open={open} handleClose={handleClose} data={sliceData} from="einsights" />
        
      </Paper>
      <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
        <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
          {alertMessage}
        </Alert>
      </Snackbar></>
           )
           }
      </Container>
      
    );
}


export default ExpenseInsights;