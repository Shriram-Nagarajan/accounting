import React from 'react';
import { CircularProgress, Box } from '@mui/material';
import { styled } from '@mui/system';

const Overlay = styled(Box)({
  position: 'fixed',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: 'rgba(0, 0, 0, 0.5)',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  zIndex: 9999,
});

const ColorfulCircularProgress = styled(CircularProgress)({
  width: '100px !important',
  height: '100px !important',
  color: 'transparent !important',
  position: 'relative',
  '&::before': {
    content: '""',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    margin: 'auto',
    width: '100%',
    height: '100%',
    borderRadius: '50%',
    border: '6px solid',
    borderColor: 'transparent',
    borderTopColor: '#FE6B8B', // Start color
    borderRightColor: '#FF8E53', // End color
    animation: 'spin 1.5s linear infinite',
  },
  '@keyframes spin': {
    '0%': {
      transform: 'rotate(0deg)',
    },
    '100%': {
      transform: 'rotate(360deg)',
    },
  },
});

const Loader = () => (
  <Overlay>
    <ColorfulCircularProgress />
  </Overlay>
);

export default Loader;


// import React from 'react';
// import { CircularProgress, Box } from '@mui/material';
// import { styled } from '@mui/system';

// const LoaderWrapper = styled(Box)({
//   display: 'flex',
//   justifyContent: 'center',
//   alignItems: 'center',
//   height: '100vh',
// });

// const GradientBackground = styled(Box)({
//   display: 'inline-block',
//   borderRadius: '50%',
//   padding: '10px',
//   background: 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)',
// });

// const Loader = () => (
//   <LoaderWrapper>
//     <GradientBackground>
//       <CircularProgress style={{ color: 'white' }} />
//     </GradientBackground>
//   </LoaderWrapper>
// );

// export default Loader;
