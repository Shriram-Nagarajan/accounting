import React, { useState } from 'react';
import accountingApi from '../httputil/accountingApi';
import { Container, Paper, Divider, MenuItem,Typography, Grid, Button, Box,InputLabel,Select, Alert, TextField } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';
import UploadIcon from '@mui/icons-material/Upload';


function ExpenseFileUploadForm() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [formData, setFormData] = useState({
    date: '',
    catgeory: '',
    description: '',
    amount: '',
  });
  const [newOption, setNewOption] = useState('');
  const [selectedOption, setSelectedOption] = useState('');

  const [uploadStatus, setUploadStatus] = useState("");
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');



  const showAlert = (message, severity = 'error') => {
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
      }, (error) => {
        setUploadStatus("Error occurred:" + error);
      })
    } else {
      console.log('No file selected');
    }
  };
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();
    // Handle form submission logic here
    console.log('Form data:', formData);
  };
  const handleNewOptionChange = (event) => {
    setNewOption(event.target.value);
  };

  const handleAddOption = () => {
    if (newOption.trim() !== '') {
      setSelectedOption(newOption);
      setNewOption('');
    }
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
                onSubmit={handleFormSubmit}
              >
                <Typography variant="h6">Form Details</Typography>
                
            <TextField
      label="Date"
      variant="outlined"
      type="date" // Set type to 'date' for date input
      value={formData.value}
      onChange={handleInputChange}
      fullWidth
      InputLabelProps={{
        shrink: true, // To shrink the label when a date is selected
      }}
    />
                
                <TextField
                  label="Category"
                  variant="outlined"
                  name="category"
                  value={formData.category}
                  onChange={handleInputChange}
                  fullWidth
                />
                {/* <InputLabel id="dropdown-label">Choose or Add Option</InputLabel>
        <Select
          labelId="dropdown-label"
          value={selectedOption}
          onChange={handleInputChange}
          fullWidth
        >
          {/* Predefined options */}
          {/* <MenuItem value="option1">Option 1</MenuItem>
          <MenuItem value="option2">Option 2</MenuItem>
          <MenuItem value="option3">Option 3</MenuItem>
          {/* Render new option input field */}
          {/* <MenuItem disabled>
            <TextField
              label="Add New Option"
              value={newOption}
              onChange={handleNewOptionChange}
              fullWidth
            />
          </MenuItem>
        </Select> */}
        {/* <Box mt={2}> Add margin top to space out the button
        <Button variant="contained" onClick={handleAddOption}>
          Add Option
        </Button>
      </Box>   */}
                <TextField
                  label="Description"
                  variant="outlined"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  multiline
                  rows={4}
                  fullWidth
                />
                <TextField
                  label="Amount"
                  variant="outlined"
                  name="amount"
                  value={formData.amount}
                  onChange={handleInputChange}
                  fullWidth
                />
                <Button variant="contained" color="primary" type="submit">
                  Submit
                </Button>
              </Box>
            </Grid>
          </Grid>
        </Box>
      </Paper>
    </Container>



  );
}

export default ExpenseFileUploadForm;
