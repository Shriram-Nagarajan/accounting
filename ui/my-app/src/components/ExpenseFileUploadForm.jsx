import React, { useState } from 'react';
import accountingApi from '../httputil/accountingApi';

function ExpenseFileUploadForm() {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState("");

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleSubmit = (event) => {
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

  return (
    <div>
      <h2>File Upload</h2>
      <form onSubmit={handleSubmit}>
        <input type="file" onChange={handleFileChange} />
        <button type="submit">Upload</button>
        <span>{uploadStatus}</span>
      </form>
    </div>
  );
}

export default ExpenseFileUploadForm;
