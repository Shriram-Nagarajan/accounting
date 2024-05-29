// import logo from './logo.svg';
// import './App.css';
import { useEffect, useState } from "react";
import accountingApi from  "./httputil/accountingApi";//"../../httputil/accountingApi";
import './App.css';
import DatePicker from "./components/DatePicker";
import CategoryWiseExpensesTable from './components/CategoryWiseExpensesTable';
import ExpenseFileUploadForm from './components/ExpenseFileUploadForm';
import PieChart from './components/pie';
import { Chart as ChartJS, ArcElement, Tooltip, Legend ,Colors} from 'chart.js';

ChartJS.register(Colors);
ChartJS.register(ArcElement, Tooltip, Legend);






function App() {
  const [expenses, setExpenses] = useState([]);
const [fromDate, setFromDate] = useState("");
const [toDate, setToDate] = useState("");
const [dataPresent, setDataPresent] = useState(false);

const handleSubmit = (event) => {
  event.preventDefault();
  let data = {fromDate: fromDate, toDate: toDate};
  accountingApi.getCategoryWiseExpenses(data, (response) => {
      setExpenses(response.data);
      setDataPresent(true);
      console.log(response.data)
  }, (error) => {
      console.error("Error fetching expenses", error);
      setDataPresent(false);
  });
}

const state = {
  labels: expenses.map(x =>x.categoryName),//['January', 'February', 'March','April', 'May'],
  datasets: [
    {
      label: 'Expenditure',
      // backgroundColor: [
      //   '#B21F00',
      //   '#C9DE00',
      //   '#2FDE00',
      //   '#00A6B4',
      //   '#6800B4',
      //   '#4B5000',
      //   '#175000',
      //   '#003350',
      // ],
      // hoverBackgroundColor: [
      //   '#501800',
      //   '#4B5000',
      //   '#175000',
      //   '#003350',
      //   '#35014F',
      //   '#B21F00',
      //   '#C9DE00',
      //   '#2FDE00',

      // ],
      borderWidth: 2,
      hoverOffset: 10,  // Offset slices on hover
      data: expenses.map(x =>x.totalExpenditure)//[65, 59, 80, 81, 56,66,56,67,78]
    }
  ]
}

  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <p>
          Accounting data
        </p>
        <form onSubmit={handleSubmit}>
                <span>{'From Date: '}</span>
                <DatePicker name="from-date" onChange={setFromDate} /> <br/>
                <span>{'To Date: '}</span>
                <DatePicker name = "to-date" onChange={setToDate} /> <br/>
                <button type="submit">Submit</button>
            </form>
          <div className="container">
          <div className="item">
            {dataPresent && <CategoryWiseExpensesTable expenses = {expenses}/> }
            </div>
            <div className="item">
            {dataPresent && <PieChart data={state}/>}
            </div>
          </div>  

            
        <ExpenseFileUploadForm  />
      </header>
    </div>
  );
}

export default App;
