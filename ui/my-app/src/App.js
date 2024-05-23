// import logo from './logo.svg';
// import './App.css';
import CategoryWiseExpensesTable from './components/CategoryWiseExpensesTable';
import ExpenseFileUploadForm from './components/ExpenseFileUploadForm';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <p>
          Accounting data
        </p>
        <CategoryWiseExpensesTable />
        <ExpenseFileUploadForm  />
      </header>
    </div>
  );
}

export default App;
