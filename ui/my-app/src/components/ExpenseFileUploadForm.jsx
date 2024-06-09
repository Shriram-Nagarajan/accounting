import React, { useState } from 'react';
import accountingApi from '../httputil/accountingApi';
import { Container, Paper, Chip, Divider, MenuItem, Typography, Grid, Button, Box, InputLabel, Select, Snackbar, Alert, TextField, FormControl } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import UploadIcon from '@mui/icons-material/Upload';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import ModalPopup from './Modal';
import Badge from '@mui/material/Badge';
import FormHelperText from '@mui/material/FormHelperText';
import DateRangePickerComponent from './DateRangePicker';
function ExpenseFileUploadForm() {
  const [open, setOpen] = useState(false);
  const [previewData, setPreviewData] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [formData, setFormData] = useState({
    date: new Date(),
    category: '',
    description: '',
    creditTxn: false,
    reversalTxn: false,
    amount: '',
  });
  const [expenseFromUser, setExpenseFromUser] = useState([{
    date: new Date(),
    category: '',
    description: '',
    creditTxn: false,
    reversalTxn: false,
    amount: '',

  }]);
  const [count, setCount] = useState(0);
  const [uploadStatus, setUploadStatus] = useState("");
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');
  const [categories, setCategories] = useState(['grocery', 'misc', 'emi', 'food order', 'medical']);
  const [categorySuggestions, setCategorySuggestions] = useState(['insurance', 'householditems', 'utility bills', 'service']);
  const [newCategory, setNewCategory] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [formErrors, setFormErrors] = useState({});
  // useEffect(() => {
  //   // Hardcoding the initial categories and suggestions
  //   const acategories = ['grocery', 'misc', 'emi', 'food order', 'medical'];
  //   const asuggestions = ['insurance', 'householditems', 'utility bills', 'service'];
  //   setCategories(acategories);
  //   setCategorySuggestions(asuggestions);

  //   // If using API:
  //   // const fetchCategoriesAndSuggestions = async () => {
  //   //   try {
  //   //     const categoriesResponse = await axios.get('https://api.example.com/categories'); // Replace with your API endpoint
  //   //     const suggestionsResponse = await axios.get('https://api.example.com/category-suggestions'); // Replace with your API endpoint

  //   //     setCategories(categoriesResponse.data);
  //   //     setCategorySuggestions(suggestionsResponse.data);
  //   //   } catch (error) {
  //   //     console.error('Error fetching data:', error);
  //   //   }
  //   // };

  //   // fetchCategoriesAndSuggestions();
  // }, []);
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
    link.href = '../template.xlsx';
    link.setAttribute('download', 'template.xlsx');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };
  const handleFileSubmit = (event) => {
    event.preventDefault();
    if (selectedFile) {
      const formData = new FormData();
      formData.append('file', selectedFile);
      // You can now send formData to your backend server using fetch or Axios
      console.log('File ready to be uploaded:', formData);
      accountingApi.uploadExpensesFile(formData, (response) => {
        setUploadStatus(response.data?.message);
        showAlert("File uploaded successfully", "success");

      }, (error) => {
        setUploadStatus("Error occurred:" + error);
        showAlert("Error uploading", "error");

      })
    } else {
      console.log('No file selected');
    }
  };
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleCategoryChange = (event) => {
    setSelectedCategory(event.target.value);
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });

  };

  const handleNewCategoryChange = (event) => {
    setNewCategory(event.target.value);
  };

  const handleAddCategory = () => {
    if (newCategory.trim() !== '' && !categories.includes(newCategory)) {
      setCategories([...categories, newCategory]);
      setNewCategory('');
      setSelectedCategory(newCategory);
    }
  };

  const handleChipClick = (suggestion) => {
    setNewCategory(suggestion);
  };

  const handleAddClick = () => {
    validateForm(formData);
    const isEmpty = Object.values(formData).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateForm(formData);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
    }
    else if (isEmpty) {
      showAlert("Date,Description,Amount,Category are mandatory. Please fill out.", "error")
    }
    else {
      setExpenseFromUser([...expenseFromUser, formData]);
      setFormData({
        date: new Date(),
        category: '',
        description: '',
        creditTxn: false,
        reversalTxn: false,
        amount: '',
      });
      setSelectedCategory('');
      setCount(count + 1);
      showAlert("Expense added", "success");
      console.log(expenseFromUser)
    }
  };


  const handleClose = () => {
    setOpen(false);
  };
  const handleSaveAPISuccess = () => {
    handleClose();
    setExpenseFromUser([]);
    setFormData({
      date: new Date(),
      category: '',
      description: '',
      creditTxn: false,
      reversalTxn: false,
      amount: '',
    });
    setSelectedCategory('');
    setCount(0);
  }
  const handlePreviewClick = () => {
    const fData = expenseFromUser;//.slice(1);
    setPreviewData(fData);
    setOpen(true);

  }
  const validateForm = (data) => {
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

  return (
    // <Container>
    //   <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
    //     <Typography variant="h4" gutterBottom>
    //     Input data
    //     </Typography>
    //     <Box
    //   sx={{
    //     display: 'flex',
    //     //justifyContent: 'center',
    //     //alignItems: 'center',
    //     //height: '100vh',
    //   }}
    // >
    //   <Button
    //     variant="contained"
    //     color="primary"
    //     startIcon={<DownloadIcon />}
    //     onClick={handleDownload}
    //   >
    //     Download Template
    //   </Button>
    // </Box>
    //   <form onSubmit={handleSubmit}>
    //      <input type="file" onChange={handleFileChange} />
    //     <button type="submit">Upload</button>

    //     <span>{uploadStatus}</span>
    //   </form>
    //   </Paper>
    //   <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
    //     <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
    //       {alertMessage}
    //     </Alert>
    //   </Snackbar>

    // </Container>
    <Container>
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
              <Box
                component="form"
                sx={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: 2,
                }}
              // onSubmit={handleFormSubmit}
              >
                <Typography variant="h6">Form Details</Typography>

                {/* <TextField
                  label="Date"
                  variant="outlined"
                  type="date" // Set type to 'date' for date input
                  name = "date"
                  value={formData.date}
                  onChange={handleInputChange}
                  fullWidth
                  InputLabelProps={{
                    shrink: true, // To shrink the label when a date is selected
                  }}
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  // required
                /> */}
                
                <DateRangePickerComponent xs={12} md={6}
                  label="Date"
                  value={formData.date}
                  onChange={handleInputChange}
                  fullWidth
                  error={!!formErrors.date}
                  helperText={formErrors.date}
                  
                />

                <TextField
                  label="Description"
                  variant="outlined"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
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
                  value={formData.amount}
                  onChange={handleInputChange}
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
                    <Button variant="contained" color="primary" type="button" fullWidth onClick={handleAddClick}>Add Expense</Button>
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Badge color="primary" badgeContent={count}>
                      <Button variant="contained" color="secondary" type="button" fullWidth onClick={handlePreviewClick}>
                        Preview & Save
                      </Button>
                    </Badge>

                  </Grid>
                  {/* <Grid item xs={12} md={5}>
                    <Button variant="contained" color="success" type="submit" fullWidth onClick = {handleSaveAllClick}>
                      Save All
                    </Button>
                  </Grid> */}
                </Grid>
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
      <ModalPopup open={open} handleClose={handleClose} data={previewData} from="efileuploadforpreview" onApiSuccess={handleSaveAPISuccess} />
    </Container>



  );
}

export default ExpenseFileUploadForm;
