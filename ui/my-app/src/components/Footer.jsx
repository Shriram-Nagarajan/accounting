import React,{useState} from 'react';
import { Box, Container, Grid, Link, Typography,Link as MuiLink } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import TermsModal from '../components/TermsModal';
function Footer({ drawerOpen }) {
    const [open, setOpen] = useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const termsContent = `
    These are the terms of use for our application. By using this application, you agree to the following terms...
  `;
    const footerStyle = {
        width: drawerOpen ? { sm: `calc(100% - 240px)` } : '100%', // Adjust width based on drawer state
        backgroundColor: 'primary.main',
        color: 'white',
        padding: '16px 0',
        position: 'relative',
        bottom: 0,
        left: 0,
        zIndex: 999,
        transition: 'width 0.3s ease',
        marginLeft: drawerOpen ? { sm: '240px' }:'0px' , // Adjust margin based on drawer state
        marginTop: 'auto', // Ensure it stays at the bottom of the content
    };

    return (
        // <Container>
        //     <Box sx={footerStyle}>
        //         <Container maxWidth="lg">

        <Box component="footer" sx={footerStyle}>
        <Box maxWidth="lg" sx={{ mx: 'auto', px: 2 }}>
                    <Grid container spacing={4} justifyContent="space-between">
                        <Grid item xs={12} sm={4}>
                            <Typography variant="h6" gutterBottom>
                                About Us
                            </Typography>
                            <Typography variant="body2">
                                We are a company dedicated to providing the best services in the industry.
                            </Typography>
                        </Grid>
                        <Grid item xs={12} sm={4}>
                            <Typography variant="h6" gutterBottom>
                                Quick Links
                            </Typography>
                            {/* <Link href="/home" color="inherit" variant="body2" underline="none">
                                Home
                            </Link>
                            <br />
                            <Link href="/file-upload" color="inherit" variant="body2" underline="none">
                                File Upload
                            </Link>
                            <br />
                            <Link href="/expense-insights" color="inherit" variant="body2" underline="none">
                                Expense Insights
                            </Link>
                            <br/>
                            <Link href="/income-insights" color="inherit" variant="body2" underline="none">
                                Income Insights
                            </Link> */}
                            <MuiLink component={RouterLink} to="/home" color="inherit" variant="body2" underline="none">
                            Home
                        </MuiLink>
                        <br />
                        <MuiLink component={RouterLink} to="/file-upload" color="inherit" variant="body2" underline="none">
                            File Upload
                        </MuiLink>
                        <br />
                        <MuiLink component={RouterLink} to="/expense-insights" color="inherit" variant="body2" underline="none">
                            Expense Insights
                        </MuiLink>
                        <br />
                        <MuiLink component={RouterLink} to="/income-insights" color="inherit" variant="body2" underline="none">
                            Income Insights
                        </MuiLink>
                        </Grid>
                        <Grid item xs={12} sm={4}>
                            <Typography variant="h6" gutterBottom>
                                Contact Us
                            </Typography>
                            <Typography variant="body2">
                                Email: <a href='mailto:freedom@gmail.com'>freedom@gmail.com</a>
                            </Typography>
                            <Typography variant="body2">
                                Phone: 8610318284/9841636807
                            </Typography>
                        </Grid>
                    </Grid>
                   
                    <Box mt={4} textAlign="center">
                    
                        <Typography variant="body2" color="inherit">
                            © {new Date().getFullYear()} Freedom. All rights reserved.
                        </Typography>
                        {/* <Typography variant="body2" color="inherit">
                        <a href='#' onClick={handleClickOpen}>Terms of use</a>
                           
                        </Typography> */}
                    </Box>
                    </Box>
                    <TermsModal open={open} handleClose={handleClose} termsContent={termsContent} />
                    </Box>
    // </Container>
    //         </Box>
    //     </Container>           
    );
}

export default Footer;

// import React from 'react';
// import { Box, Container, Grid, Link, Typography } from '@mui/material';

// function Footer({ drawerOpen, isMobile }) {
//     const footerStyle = {
//         width: drawerOpen && !isMobile ? `calc(100% - 240px)` : '100%', // Adjust width based on drawer state and screen size
//         backgroundColor: 'primary.main',
//         color: 'white',
//         padding: '16px 0',
//         position: 'fixed',
//         bottom: 0,
//         left: 0,
//         zIndex: 999,
//         transition: 'width 0.3s ease',
//     };

//     return (
//         <Box sx={footerStyle}>
//             <Container maxWidth="lg">
//                 <Grid container spacing={4} justifyContent="space-between">
//                     <Grid item xs={12} sm={4}>
//                         <Typography variant="h6" gutterBottom>
//                             About Us
//                         </Typography>
//                         <Typography variant="body2">
//                             We are a company dedicated to providing the best services in the industry.
//                         </Typography>
//                     </Grid>
//                     <Grid item xs={12} sm={4}>
//                         <Typography variant="h6" gutterBottom>
//                             Quick Links
//                         </Typography>
//                         <Link href="#" color="inherit" variant="body2" underline="none">
//                             Home
//                         </Link>
//                         <br />
//                         <Link href="#" color="inherit" variant="body2" underline="none">
//                             Services
//                         </Link>
//                         <br />
//                         <Link href="#" color="inherit" variant="body2" underline="none">
//                             Contact
//                         </Link>
//                     </Grid>
//                     <Grid item xs={12} sm={4}>
//                         <Typography variant="h6" gutterBottom>
//                             Contact Us
//                         </Typography>
//                         <Typography variant="body2">
//                             Email: info@company.com
//                         </Typography>
//                         <Typography variant="body2">
//                             Phone: +123 456 7890
//                         </Typography>
//                     </Grid>
//                 </Grid>
//                 <Box mt={4} textAlign="center">
//                     <Typography variant="body2" color="inherit">
//                         © {new Date().getFullYear()} Company Name. All rights reserved.
//                     </Typography>
//                 </Box>
//             </Container>
//         </Box>
//     );
// }

// export default Footer;





// // import React from 'react';
// // import { Box, Container, Grid, Link, Typography } from '@mui/material';

// // function Footer({ drawerOpen }) {
// //     const footerStyle = {
// //         width: drawerOpen ? `calc(100% - 240px)` : '100%',
// //         backgroundColor: '#333', // Adjust the color according to your design
// //         color: '#fff', // Adjust the text color according to your design
// //         padding: '16px 0',
// //         position: 'fixed',
// //         bottom: 0,
// //         left: 0,
// //         zIndex: 1000, // Set a higher z-index than the drawer
// //         transition: 'width 0.3s ease',
// //         marginLeft: drawerOpen ? '240px' : 0, // Adjust margin based on drawer state
// //     };

// //     return (
// //         <Box sx={footerStyle}>
// //             <Container maxWidth="lg">
// //                 <Grid container spacing={4} justifyContent="space-between">
// //                     <Grid item xs={12} sm={4}>
// //                         <Typography variant="h6" gutterBottom>
// //                             About Us
// //                         </Typography>
// //                         <Typography variant="body2">
// //                             We are a company dedicated to providing the best services in the industry.
// //                         </Typography>
// //                     </Grid>
// //                     <Grid item xs={12} sm={4}>
// //                         <Typography variant="h6" gutterBottom>
// //                             Quick Links
// //                         </Typography>
// //                         <Link href="#" color="inherit" variant="body2" underline="none">
// //                             Home
// //                         </Link>
// //                         <br />
// //                         <Link href="#" color="inherit" variant="body2" underline="none">
// //                             Services
// //                         </Link>
// //                         <br />
// //                         <Link href="#" color="inherit" variant="body2" underline="none">
// //                             Contact
// //                         </Link>
// //                     </Grid>
// //                     <Grid item xs={12} sm={4}>
// //                         <Typography variant="h6" gutterBottom>
// //                             Contact Us
// //                         </Typography>
// //                         <Typography variant="body2">
// //                             Email: info@company.com
// //                         </Typography>
// //                         <Typography variant="body2">
// //                             Phone: +123 456 7890
// //                         </Typography>
// //                     </Grid>
// //                 </Grid>
// //                 <Box mt={4} textAlign="center">
// //                     <Typography variant="body2" color="inherit">
// //                         © {new Date().getFullYear()} Company Name. All rights reserved.
// //                     </Typography>
// //                 </Box>
// //             </Container>
// //         </Box>
// //     );
// // }

// // export default Footer;
