import React, { useState ,useEffect} from 'react';
import accountingApi from '../httputil/accountingApi';
import { Container, Paper, Chip, Divider, MenuItem, Typography, Grid, Button, Box, InputLabel, Select, Snackbar, Alert, TextField, FormControl } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import UploadIcon from '@mui/icons-material/Upload';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import ModalPopup from './Modal';
import Badge from '@mui/material/Badge';
import FormHelperText from '@mui/material/FormHelperText';
import DateRangePickerComponent from './DateRangePicker';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import dayjs from 'dayjs';
import ModalPopupforIncomePreview from './IncomePreviewModal';
import { useSelector } from 'react-redux';
import { useDispatch } from 'react-redux';
import { addDefaultCategory } from '../actions/categoryActions';
import Loader from '../components/Loader';
import { addExpense,resetExpenses} from '../actions/expenseActions';
import { addIncome,resetIncome } from '../actions/incomeActions';
function ExpenseFileUploadForm() {
  const dispatch = useDispatch();
  const defaultCategories= useSelector(state => state.category.defaultCategories);
  const categoriesLoaded = useSelector(state => state.category.categoriesLoaded);
  const expensesInPreview = useSelector(state => state.expenses);
  const incomeInPreview = useSelector(state => state.income);
  console.log(expensesInPreview.expenses.length);
  const [expenseTableOpen, setExpenseTableOpen] = useState(false);
  const [incomeTableOpen, setIncomeTableOpen] = useState(false);

  const [expensepreviewData, setExpensePreviewData] = useState(null);
  const [incomepreviewData, setIncomePreviewData] = useState(null);

  const [selectedFile, setSelectedFile] = useState(null);
  const [tabValue, setTabValue] = useState(0);

  const [ExpenseFormData, setExpenseFormData] = useState({
    date: dayjs(),
    category: '',
    description: '',
    creditTxn: false,
    reversalTxn: false,
    amount: '',
  });
  const [IncomeFormData, setIncomeFormData] = useState({
    date: dayjs(),
    description:'',
    amount: '',
    creditTxn: true,
    reversalTxn: false,

  });
  const [expenseFromUser, setExpenseFromUser] = useState([{
    date: dayjs(),
    category: '',
    description: '',
    creditTxn: false,
    reversalTxn: false,
    amount: '',

  }]);
  const [incomeFromUser, setIncomeFromUser] = useState([{
    date: dayjs(),
    description:'',
    amount: '',
    creditTxn: true,
    reversalTxn: false,

  }]);
  const [ExpenseCount, setExpenseCount] = useState(0);
  const [IncomeCount, setIncomeCount] = useState(0);
  const [uploadStatus, setUploadStatus] = useState("");
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');
  const [categories, setCategories] = useState([]);//useState(['grocery', 'misc', 'emi', 'food order', 'medical']);
  const [categorySuggestions, setCategorySuggestions] = useState([]);//'insurance', 'householditems', 'utility bills', 'service']
  const [newCategory, setNewCategory] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [formErrors, setFormErrors] = useState({});
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    if (!categoriesLoaded) {
      const getDefaultCategories = () => {
          setLoading(true);
          accountingApi.getDefaultCategories('', (response) => {
              const categoryNames = response.data.map(x => x.categoryName);
              dispatch(addDefaultCategory(categoryNames));
              setLoading(false);
          }, (error) => {
              console.error("Error fetching expenses", error);
              setLoading(false);
          });
      };
      const getUserCategories = () => {
        setLoading(true);
        accountingApi.getUserCategories('', (response) => {
            const categoryNames = response.data.map(x => x.categoryName);
            // dispatch(addDefaultCategory(categoryNames));
            setCategories(categoryNames);
            setLoading(false);
        }, (error) => {
            console.error("Error fetching expenses", error);
            setLoading(false);
        });
    };
    getDefaultCategories();
      getUserCategories();
    }
    }, );
  useEffect(() => {
    setCategorySuggestions(defaultCategories);
  }, [defaultCategories]);
  useEffect (()=>
  {
    if(expensesInPreview != null || expensesInPreview != undefined || expensesInPreview.expenses.length > 0)
      {
        setExpenseFromUser(expensesInPreview.expenses);
        setExpenseCount(expensesInPreview.expenses.length);
        console.log(expenseFromUser);
      }
    if(incomeInPreview != null || incomeInPreview != undefined || incomeInPreview.income.length >0)
      {
        setIncomeFromUser(incomeInPreview.income);
        setIncomeCount(incomeInPreview.income.length);
        console.log(incomeFromUser);
      }
  })

  const clearError = (field) => {
    console.log(formErrors)
    setFormErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
  };

  const showAlert = (message, severity) => {
    setAlertMessage(message);
    setAlertSeverity(severity);
    setAlertOpen(true);
  };
  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleDownload = () => {
    const link = document.createElement('a');
    link.href = '../transactions.xlsx';
    link.setAttribute('download', 'transactions.xlsx');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };
  const handleFileSubmit = (event) => {
    setLoading(true);
    event.preventDefault();
    if (selectedFile) {
      const formData = new FormData();
      formData.append('file', selectedFile);
      // You can now send formData to your backend server using fetch or Axios
      console.log('File ready to be uploaded:', formData);
      accountingApi.uploadExpensesFile(formData, (response) => {
        setUploadStatus(response.data?.message);
        showAlert("File uploaded successfully", "success");
        setLoading(false);
      }, (error) => {
        setUploadStatus("Error occurred:" + error);
        showAlert("Error uploading", "error");
        setLoading(false);
      })
    } else {
      console.log('No file selected');
      setLoading(false);
    }
  };
  const handleExpenseInputChange = (e) => {
    if (e.target) {
      const { name, value } = e.target;
      setExpenseFormData({ ...ExpenseFormData, [name]: value });
      clearError(name); // Clear error for this field
  } 
  else if (e) {
      // Adjust this based on actual structure of e.value
      let formattedDate = e.format('YYYY-MM-DD');
      setExpenseFormData({ ...ExpenseFormData, date: formattedDate });
      clearError('date'); // Clear error for date field
  } 
  };
  const handleIncomeInputChange = (e) => {
    if (e.target) {
      const { name, value } = e.target;
      setIncomeFormData({ ...IncomeFormData, [name]: value });
      clearError(name); // Clear error for this field
  } 
  else if (e) {
      // Adjust this based on actual structure of e.value
      let formattedDate = e.format('YYYY-MM-DD');
      setIncomeFormData({ ...IncomeFormData, date: formattedDate });
      clearError('date'); // Clear error for date field
  }  
};

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };
  const handleCategoryChange = (event) => {
    setSelectedCategory(event.target.value);
    const { name, value } = event.target;
    setExpenseFormData({ ...ExpenseFormData, [name]: value });
    clearError('category');

  };

  const handleNewCategoryChange = (event) => {
    setNewCategory(event.target.value);
  };

  const handleAddCategory = () => {
    if (newCategory.trim() !== '' && !categories.includes(newCategory)) {
      setCategories([...categories, newCategory]);
      setNewCategory('');
      setSelectedCategory(newCategory);
      // const { name, value } = event.target;
      setExpenseFormData({ ...ExpenseFormData, category: newCategory });
  
    }
  };

  const handleChipClick = (suggestion) => {
    setNewCategory(suggestion);
  };

  const handleAddExpenseClick = () => {
    validateExpenseForm(ExpenseFormData);
    const isEmpty = Object.values(ExpenseFormData).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateExpenseForm(ExpenseFormData);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
    }
    else if (isEmpty) {
      showAlert("Date,Description,Amount. Please fill out.", "error")
    }
    else {
      dispatch(addExpense(ExpenseFormData));
      setExpenseFromUser([...expenseFromUser, ExpenseFormData]);
      setExpenseFormData({
        date: dayjs(),
        category: '',
        description: '',
        creditTxn: false,
        reversalTxn: false,
        amount: '',
      });
      setSelectedCategory('');
      setExpenseCount(ExpenseCount + 1);
      showAlert("Expense added", "success");
      console.log(expenseFromUser)
      setFormErrors({}); // Clear
    }
  };
  const handlePreviewExpenseClick = () => {
    console.log(expenseFromUser);
    console.log(expenseFromUser.length);

    if((expenseFromUser.length === 1 && expenseFromUser[0].amount == "" && expenseFromUser[0].description===""&& expenseFromUser[0].category==="")|| expenseFromUser.length === 0)
      {
        showAlert("No expenses added to preview",'warning');
      }
      else
      {
        if((expenseFromUser.length > 1 && expenseFromUser[0].amount == "" && expenseFromUser[0].description===""&& expenseFromUser[0].category==="")|| expenseFromUser.length === 0)
          {
    
        const fData = expenseFromUser.slice(1);
        setExpensePreviewData(fData);
        setExpenseTableOpen(true);
          }
          else
          {
            setExpensePreviewData(expenseFromUser);
            setExpenseTableOpen(true);
    
          }
      }

  }
  const handleAddIncomeClick = () => {
    validateIncomeForm(IncomeFormData);
    const isEmpty = Object.values(IncomeFormData).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateIncomeForm(IncomeFormData);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
    }
    else if (isEmpty) {
      showAlert("Date,Description,Amount,Category are mandatory. Please fill out.", "error")
    }
    else {
      dispatch(addIncome(IncomeFormData));
      setIncomeFromUser([...incomeFromUser, IncomeFormData]);
      setIncomeFormData({
        date: dayjs(),
        description:'',
        amount: '',
        creditTxn: true,
        reversalTxn: false,
    
          });
      setIncomeCount(IncomeCount + 1);
      showAlert("Income added", "success");
      console.log(incomeFromUser)
      setFormErrors({}); // Clear

    }
  };
  const handlePreviewIncomeClick = () => {
    if((incomeFromUser.length === 1 && incomeFromUser[0].amount == "" && incomeFromUser[0].description==="")|| incomeFromUser.length === 0)
      {
        showAlert("No income added to preview",'warning');
      }
      else
      {

        if((incomeFromUser.length > 1 && incomeFromUser[0].amount == "" && incomeFromUser[0].description==="")|| incomeFromUser.length === 0)
          {
    
        const fData = incomeFromUser.slice(1);
        setIncomePreviewData(fData);
        setIncomeTableOpen(true);
          }
          else
          {
            setIncomePreviewData(incomeFromUser);
            setIncomeTableOpen(true);
    
          }
    
      }

  }
  const handleExpenseTableClose = () => {
    setExpenseTableOpen(false);
  };
  const handleIncomeTableClose = () => {
    setIncomeTableOpen(false);
  };
  const handleSaveAPISuccess = () => {
    if(expenseFromUser != null)
      {
        handleExpenseTableClose();
        setExpenseFromUser([]);
        setExpenseFormData({
          date: dayjs(),
          category: '',
          description: '',
          creditTxn: false,
          reversalTxn: false,
          amount: '',
        });
        setSelectedCategory('');
        setExpenseCount(0);
        dispatch(resetExpenses());

      }
      if(incomeFromUser != null)
        {
          handleIncomeTableClose();
            setIncomeFromUser([]);
            setIncomeFormData({
            date: dayjs(),
            description:'',
            amount: '',
            creditTxn: true,
            reversalTxn: false,
        
              });
          setIncomeCount(0);
          dispatch(resetIncome());


        }
    
  }
  // const handleSaveIncomeAPISuccess = () => {
  //   handleClose();
  //   setIncomeFromUser([]);
  //   setIncomeFormData({
  //     date: dayjs(),
  //     description:'',
  //     amount: '',
  //       });
  //   setIncomeCount(0);
  // }
  const validateExpenseForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.date) {
      errors.date = 'Date is required';
    }
    if (!data.category) {
      errors.category = 'Category is required';
    } else if (scriptTagPattern.test(data.category)) {
      errors.category = 'Category must not contain script tags';
    }
    if (!data.description) {
      errors.description = 'Description is required';
    } else if (scriptTagPattern.test(data.description)) {
      errors.description = 'Description must not contain script tags';
    }
    
    if (!data.amount || isNaN(data.amount)) {
      errors.amount = 'Amount is required and must be a number';
    }

    return errors;
  };
  const validateIncomeForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.date) {
      errors.date = 'Date is required';
    }
    if (!data.description) {
      errors.description = 'Description is required';
    } else if (scriptTagPattern.test(data.description)) {
      errors.description = 'Description must not contain script tags';
    }
    if (!data.amount || isNaN(data.amount)) {
      errors.amount = 'Amount is required and must be a number';
    }

    return errors;
  };

  return (

    <Container>
      {loading ? <Loader /> :(<>
        <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
        <Box
          sx={{
            flexGrow: 1,
            p: 3,
          }}
        >
          <Grid container spacing={2}>
            <Grid item xs={12} md={5}>
              <Box
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  gap: 2,
                }}
              >
                <Typography variant="h6">
                  Download the Excel template here:
                </Typography>
                <Button
                  variant="contained"
                  color="primary"
                  startIcon={<DownloadIcon />}
                  onClick={handleDownload}
                >
                  Download Template
                </Button>

                <Typography variant="h6" sx={{ marginTop: 4 }}>
                  Choose a file to upload:
                </Typography>
                <input
                  accept=".xlsx"//, .xls, .csv"
                  style={{ display: 'none' }}
                  id="file-input"
                  type="file"
                  onChange={handleFileChange}
                />
                <label htmlFor="file-input">
                  <Button variant="contained" component="span">
                    Choose File
                  </Button>
                </label>
                {selectedFile && (
                  <TextField
                    variant="outlined"
                    fullWidth
                    value={selectedFile.name}
                    InputProps={{
                      readOnly: true,
                    }}
                    sx={{ marginTop: 2 }}
                  />
                )}
                <Button
                  variant="contained"
                  color="secondary"
                  startIcon={<UploadIcon />}
                  onClick={handleFileSubmit}
                  sx={{ marginTop: 2 }}
                >
                  Upload
                </Button>
              </Box>
            </Grid>
            <Grid item xs={12} md={2}>
              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  height: '100%',
                }}
              >
                <Divider orientation="vertical" flexItem />
                <Typography
                  variant="h6"
                  sx={{
                    position: 'absolute',
                    background: 'white',
                    padding: '0 10px',
                  }}
                >
                  OR
                </Typography>
              </Box>
            </Grid>
            <Grid item xs={12} md={5}>
            <Box sx={{ mb: 2 }}>
              <Tabs value={tabValue} onChange={handleTabChange} textColor="secondary"
        indicatorColor="secondary">
                <Tab label="Expense" />
                <Tab label="Income" />
              </Tabs>
              {tabValue === 0 && (
                // Expense form content
                <Box
                  component="form"
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 2,
                    mt: 2, // Add margin-top to create space between tabs line and form
                  }}
                >
                  {/* Expense form fields */}
                  {/* <TextField
                  label="Date"
                  variant="outlined"
                  type="date" // Set type to 'date' for date input
                  name = "date"
                  value={ExpenseFormData.date}
                  onChange={handleExpenseInputChange}
                  fullWidth
                  InputLabelProps={{
                    shrink: true, // To shrink the label when a date is selected
                  }}
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  // required
                /> */}
                
                <DateRangePickerComponent 
                  label="Date"
                  value={ExpenseFormData.date}
                  onChange={handleExpenseInputChange}
                  fullWidth
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  sx={{width:'100%'}}
                />

                <TextField
                  label="Description"
                  variant="outlined"
                  name="description"
                  value={ExpenseFormData.description}
                  onChange={handleExpenseInputChange}
                  multiline
                  rows={4}
                  fullWidth
                  error={!!formErrors.description}
                  helperText={formErrors.description}
                // required
                />
                <TextField
                  label="Amount"
                  variant="outlined"
                  name="amount"
                  value={ExpenseFormData.amount}
                  onChange={handleExpenseInputChange}
                  fullWidth
                  error={!!formErrors.amount}
                  helperText={formErrors.amount}
                // required
                />
                {/* Dropdown for existing categories */}
                <FormControl fullWidth error={!!formErrors.category}>
                  <InputLabel id="category-select-label">Category</InputLabel>
                  <Select
                    labelId="category-select-label"
                    label="Catgeory"
                    name="category"
                    value={selectedCategory}
                    onChange={handleCategoryChange}
                    fullWidth
                  >
                    {categories.map((category) => (
                      <MenuItem key={category} value={category}>
                        {category}
                      </MenuItem>
                    ))}
                  </Select>
                  {formErrors.category && <FormHelperText>{formErrors.category}</FormHelperText>}
                </FormControl>

                {/* TextField for new category input */}
                <Box mt={2}>
                  <Grid container>
                    <Grid item xs={12} md={10}>
                      <TextField
                        label="Choose new category"
                        variant="outlined"
                        value={newCategory}
                        onChange={handleNewCategoryChange}
                        fullWidth
                      />
                    </Grid>
                    <Grid item xs={12} md={1}>
                      <Button onClick={handleAddCategory} sx={{ mt: '10px' }}><AddCircleRoundedIcon />
                      </Button>

                    </Grid>
                  </Grid>
                </Box>
                {/* Chips for category suggestions */}
                <Box mt={2} display="flex" flexWrap="wrap" gap={1}>
                  {categorySuggestions.map((suggestion) => (
                    <Chip
                      key={suggestion}
                      label={suggestion}
                      onClick={() => handleChipClick(suggestion)}
                    />
                  ))}
                </Box>

                <br />
                <Grid container spacing={1} sx={{
                  alignItems: 'center',
                  justifyContent: 'center',
                }}>
                  <Grid item xs={12} md={6}>
                    <Button variant="contained" color="primary" type="button" fullWidth onClick={handleAddExpenseClick}>Add Expense</Button>
                  </Grid>
                  <Grid item xs={12} md={6}>
                      <Button variant="contained" color="secondary" type="button" fullWidth onClick={handlePreviewExpenseClick}>
                        Preview & Save
                        <Badge color="primary" badgeContent={ExpenseCount} anchorOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        style={{ position: 'absolute', top: 3, right: 3 }}>
                        </Badge>

                      </Button>
                  </Grid>
                  </Grid>
                </Box>
              )}
              {tabValue === 1 && (
                // Income form content
                <Box
                  component="form"
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: 2,
                    mt: 2, // Add margin-top to create space between tabs line and form

                  }}
                >

                  {/* Income form fields */}
                  {/* <DateRangePickerComponent xs={12} md={6}
                  label="Date"
                  value={IncomeFormData.date}
                  onChange={handleIncomeInputChange}
                  fullWidth
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  
                /> */}
                <DateRangePickerComponent xs={12} md={6}
                  label="Date"
                  value={IncomeFormData.date}
                  onChange={handleIncomeInputChange}
                  fullWidth
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  
                />

                <TextField
                  label="Description"
                  variant="outlined"
                  name="description"
                  value={IncomeFormData.description}
                  onChange={handleIncomeInputChange}
                  multiline
                  rows={4}
                  fullWidth
                  error={!!formErrors.description}
                  helperText={formErrors.description}
                // required
                />
                <TextField
                  label="Amount"
                  variant="outlined"
                  name="amount"
                  value={IncomeFormData.amount}
                  onChange={handleIncomeInputChange}
                  fullWidth
                  error={!!formErrors.amount}
                  helperText={formErrors.amount}
                // required
                />
                <Grid container spacing={1} sx={{
                  alignItems: 'center',
                  justifyContent: 'center',
                }}>
                  <Grid item xs={12} md={6}>
                    <Button variant="contained" color="primary" type="button" fullWidth onClick={handleAddIncomeClick}>Add Income</Button>
                  </Grid>
                  <Grid item xs={12} md={6}>
                    
                      <Button variant="contained" color="secondary" type="button" fullWidth onClick={handlePreviewIncomeClick}>
                        Preview & Save
                        <Badge color="primary" badgeContent={IncomeCount} anchorOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        style={{ position: 'absolute', top: 3, right: 3 }}></Badge>
                      </Button>
                    {/* </Badge> */}
                  </Grid>
                  </Grid>
                </Box>
              )}
              </Box>
            </Grid>
           
          </Grid>
        </Box>
      </Paper>
      <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
        <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
          {alertMessage}
        </Alert>
      </Snackbar>
      <ModalPopup open={expenseTableOpen} handleClose={handleExpenseTableClose} data={expensepreviewData} from="efileuploadforpreview" onApiSuccess={handleSaveAPISuccess} />
      <ModalPopupforIncomePreview open={incomeTableOpen} handleClose={handleIncomeTableClose} data={incomepreviewData} onSaveIncomeApiSuccess={handleSaveAPISuccess}/></>)
}
    </Container>



  );
}

export default ExpenseFileUploadForm;


// //  <Grid item xs={12} md={5}>
// <Tabs value={tabValue} onChange={handleTabChange}>
// <Tab label="Expense" />
// <Tab label="Income" />
// </Tabs>
//     <Box
//       component="form"
//       sx={{
//         display: 'flex',
//         flexDirection: 'column',
//         gap: 2,
//       }}
//     // onSubmit={handleFormSubmit}
//     >
//       <Typography variant="h6">Form Details</Typography>

//       {/* <TextField
//         label="Date"
//         variant="outlined"
//         type="date" // Set type to 'date' for date input
//         name = "date"
//         value={ExpenseFormData.date}
//         onChange={handleExpenseInputChange}
//         fullWidth
//         InputLabelProps={{
//           shrink: true, // To shrink the label when a date is selected
//         }}
//         error={!!formErrors.date}
//         helperText={formErrors.date}
//         // required
//       /> */}
      
//       <DateRangePickerComponent xs={12} md={6}
//         label="Date"
//         value={ExpenseFormData.date}
//         onChange={handleExpenseInputChange}
//         fullWidth
//         error={!!formErrors.date}
//         helperText={formErrors.date}
        
//       />

//       <TextField
//         label="Description"
//         variant="outlined"
//         name="description"
//         value={ExpenseFormData.description}
//         onChange={handleExpenseInputChange}
//         multiline
//         rows={4}
//         fullWidth
//         error={!!formErrors.description}
//         helperText={formErrors.description}
//       // required
//       />
//       <TextField
//         label="Amount"
//         variant="outlined"
//         name="amount"
//         value={ExpenseFormData.amount}
//         onChange={handleExpenseInputChange}
//         fullWidth
//         error={!!formErrors.amount}
//         helperText={formErrors.amount}
//       // required
//       />
//       {/* Dropdown for existing categories */}
//       <FormControl fullWidth error={!!formErrors.category}>
//         <InputLabel id="category-select-label">Category</InputLabel>
//         <Select
//           labelId="category-select-label"
//           label="Catgeory"
//           name="category"
//           value={selectedCategory}
//           onChange={handleCategoryChange}
//           fullWidth
//         >
//           {categories.map((category) => (
//             <MenuItem key={category} value={category}>
//               {category}
//             </MenuItem>
//           ))}
//         </Select>
//         {formErrors.category && <FormHelperText>{formErrors.category}</FormHelperText>}
//       </FormControl>

//       {/* TextField for new category input */}
//       <Box mt={2}>
//         <Grid container>
//           <Grid item xs={12} md={10}>
//             <TextField
//               label="Choose new category"
//               variant="outlined"
//               value={newCategory}
//               onChange={handleNewCategoryChange}
//               fullWidth
//             />
//           </Grid>
//           <Grid item xs={12} md={1}>
//             <Button onClick={handleAddCategory} sx={{ mt: '10px' }}><AddCircleRoundedIcon />
//             </Button>

//           </Grid>
//         </Grid>
//       </Box>
//       {/* Chips for category suggestions */}
//       <Box mt={2} display="flex" flexWrap="wrap" gap={1}>
//         {categorySuggestions.map((suggestion) => (
//           <Chip
//             key={suggestion}
//             label={suggestion}
//             onClick={() => handleChipClick(suggestion)}
//           />
//         ))}
//       </Box>

//       <br />
//       <Grid container spacing={1} sx={{
//         alignItems: 'center',
//         justifyContent: 'center',
//       }}>
//         <Grid item xs={12} md={6}>
//           <Button variant="contained" color="primary" type="button" fullWidth onClick={handleAddClick}>Add Expense</Button>
//         </Grid>
//         <Grid item xs={12} md={6}>
//           <Badge color="primary" badgeContent={count}>
//             <Button variant="contained" color="secondary" type="button" fullWidth onClick={handlePreviewClick}>
//               Preview & Save
//             </Button>
//           </Badge>

//         </Grid>
//         {/* <Grid item xs={12} md={5}>
//           <Button variant="contained" color="success" type="submit" fullWidth onClick = {handleSaveAllClick}>
//             Save All
//           </Button>
//         </Grid> */}
//       </Grid>
//     </Box>
//   </Grid>