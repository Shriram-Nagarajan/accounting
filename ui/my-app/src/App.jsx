import React, { useState } from "react";
import './App.css';
import { Route, Routes } from 'react-router-dom';
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from 'chart.js';
import dayjs from 'dayjs';
import ExpenseFileUploadForm from './components/ExpenseFileUploadForm';
import ExpenseInsights from "./components/ExpenseInsights";
import NavBar from "./components/Navbar";
import Home from "./components/Home";
ChartJS.register(ArcElement, Tooltip, Legend, Title);


function App() {
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');
  const [expenses, setExpenses] = useState([]);
  const [fromDate, setFromDate] = useState(dayjs());
  const [toDate, setToDate] = useState(dayjs());
  const [dataPresent, setDataPresent] = useState(false);
  const [pieTitle, setPieTitle] = useState("");
  const [open, setOpen] = useState(false);
  const [sliceData, setSliceData] = useState(null);
  
  return (
    // <Container maxWidth="lg">
    <div>
      <NavBar />
      <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/file-upload" element={<ExpenseFileUploadForm />} />
                <Route path="/expense-insights" element={<ExpenseInsights />} />
            </Routes>
      {/* <ExpenseInsights /> */}
      {/* <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
        <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
          {alertMessage}
        </Alert>
      </Snackbar> */}
    {/* </Container> */}
    </div>
  );
}

export default App;

        {/* <DrawerAppBar /> */}
        {/* <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Typography variant="h4" gutterBottom>
          Accounting Data
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
        {/* <Box mt={4}>
          <Typography variant="h6">
            Selected From Date: {fromDate ? fromDate.format('YYYY-MM-DD') : 'None'}
          </Typography>
          <Typography variant="h6">
            Selected To Date: {toDate ? toDate.format('YYYY-MM-DD') : 'None'}
          </Typography>
        </Box> */}
      //   {dataPresent && (
      //     <Box mt={4}>
      //       <Grid container spacing={4}>
      //         <Grid item xs={12} md={6}>
      //           <CategoryWiseExpensesTable expenses={expenses} />
      //         </Grid>
      //         <Grid item xs={12} md={6}>
      //           <PieChart data={state} title={pieTitle} onSliceClick={handlePieSliceClick} />
      //         </Grid>
      //       </Grid>
      //     </Box>
      //   )}
      //   <ModalPopup open={open} handleClose={handleClose} sliceData={sliceData} />
      //   {/* <Box mt={4}>
      //     <ExpenseFileUploadForm />
      //   </Box> */}
      // </Paper> */}


// import { useState } from "react";
// import accountingApi from "./httputil/accountingApi";
// import './App.css';
// import DateRangePickerComponent from "./components/DateRangePicker";
// import CategoryWiseExpensesTable from './components/CategoryWiseExpensesTable';
// import ExpenseFileUploadForm from './components/ExpenseFileUploadForm';
// import PieChart from './components/pie';
// import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from 'chart.js';
// import ModalPopup from './components/Modal';
// import dayjs from 'dayjs';

// ChartJS.register(ArcElement, Tooltip, Legend, Title);

// function App() {
//   const [expenses, setExpenses] = useState([]);
//   const [fromDate, setFromDate] = useState(dayjs());
//   const [toDate, setToDate] = useState(dayjs());
//   const [dataPresent, setDataPresent] = useState(false);
//   const [pieTitle, setPieTitle] = useState("");
//   const [open, setOpen] = useState(false);
//   const [sliceData, setSliceData] = useState(null);

//   const handleFromDateChange = (newValue) => {
//     setFromDate(newValue);
//   };

//   const handleToDateChange = (newValue) => {
//     setToDate(newValue);
//   };

//   const handleClose = () => {
//     setOpen(false);
//   };

//   const handleSubmit = (event) => {
//     event.preventDefault();
//     let data = { fromDate: fromDate.format('YYYY-MM-DD'), toDate: toDate.format('YYYY-MM-DD') };
//     accountingApi.getCategoryWiseExpenses(data, (response) => {
//       setExpenses(response.data);
//       setDataPresent(true);
//       setPieTitle("Expenditure for the period of " + fromDate.format('YYYY-MM-DD') + " to " + toDate.format('YYYY-MM-DD'));
//       console.log(response.data);
//     }, (error) => {
//       console.error("Error fetching expenses", error);
//       setDataPresent(false);
//     });
//   };

//   function handlePieSliceClick(data) {
//     console.log(data);
//     var catId = expenses.find(x => x.categoryName === data).categoryId;
//     console.log(catId);
//     accountingApi.getExpenses({ categoryId: catId, fromDate: fromDate.format('YYYY-MM-DD'), toDate: toDate.format('YYYY-MM-DD') }, (response) => {
//       console.log("getExpenses: " + JSON.stringify(response.data));
//       setSliceData(response.data);
//       setOpen(true);
//     }, (error) => {
//       console.error(error);
//     });
//   }

//   const state = {
//     labels: expenses.map(x => x.categoryName),
//     datasets: [
//       {
//         label: 'Expenditure',
//         backgroundColor: [
//           '#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5',
//           '#2196F3', '#03A9F4', '#00BCD4', '#009688', '#4CAF50',
//           '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107', '#FF9800',
//           '#FF5722', '#795548', '#9E9E9E', '#607D8B', '#000000'
//         ],
//         borderWidth: 3,
//         hoverOffset: 20,
//         data: expenses.map(x => x.totalExpenditure)
//       }
//     ]
//   };

//   return (
//     <div className="App">
//       <header className="App-header">
//         <p>Accounting data</p>
//         <form onSubmit={handleSubmit}>
//           <span>{'From Date: '}</span>
//           <DateRangePickerComponent value={fromDate} onChange={handleFromDateChange} label="From date" /> <br />
//           <span>{'To Date: '}</span>
//           <DateRangePickerComponent value={toDate} onChange={handleToDateChange} label="To date" /> <br />
//           <button type="submit">Submit</button>
//         </form>
//         <div>
//           <p>Selected From Date: {fromDate ? fromDate.format('YYYY-MM-DD') : 'None'}</p>
//           <p>Selected To Date: {toDate ? toDate.format('YYYY-MM-DD') : 'None'}</p>
//         </div>
//         <div className="container">
//           <div className="item">
//             {dataPresent && <CategoryWiseExpensesTable expenses={expenses} />}
//           </div>
//           <div className="item">
//             {dataPresent && <PieChart data={state} title={pieTitle} onSliceClick={handlePieSliceClick} />}
//           </div>
//           <ModalPopup open={open} handleClose={handleClose} sliceData={sliceData} />
//         </div>
//         <ExpenseFileUploadForm />
//       </header>
//     </div>
//   );
// }

// export default App;
