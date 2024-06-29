import React, { useState } from 'react';
import { Modal, Box, Typography, Button } from '@mui/material';
import { styled } from '@mui/material/styles';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 600,
  maxHeight: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
  overflowY: 'auto',
};

const StyledBox = styled(Box)({
  padding: '20px',
  maxHeight: '350px',
  overflowY: 'auto',
});

function TermsModal({ open, handleClose, termsContent }) {
//   const handleAgree = () => {
//     // Handle the action when the user agrees to the terms
//     console.log("User agreed to the terms");
//     handleClose();
//   };

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="terms-modal-title"
      aria-describedby="terms-modal-description"
    >
      <Box sx={style}>
        <Typography id="terms-modal-title" variant="h6" component="h2">
          Terms of Use
        </Typography>
        <StyledBox id="terms-modal-description">
          <Typography variant="body1">
            {termsContent}
          </Typography>
        </StyledBox>
        <Box display="flex" justifyContent="flex-end" mt={2}>
          {/* <Button variant="contained" color="primary" onClick={handleAgree}>
            Agree
          </Button> */}
          <Button variant="outlined" color="secondary" onClick={handleClose} sx={{ ml: 2 }}>
            Close
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default TermsModal;
