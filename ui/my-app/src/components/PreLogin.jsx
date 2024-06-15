// src/SignIn.js
import React,{useState,useEffect} from 'react';
import { Routes, Route,useNavigate  } from 'react-router-dom';
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import UAMApi from '../httputil/uam';
import { useDispatch } from 'react-redux';
import {  loginSuccess } from '../actions/authActions';
import Loader from '../components/Loader';

const theme = createTheme();

function Copyright(props) {

  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="https://mui.com/">
        Freedom
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

function PreLogin() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [isforLogin,setIsForLogin] = useState(true);
  const [isforRegister,setIsForRegister] = useState(false);
  const [isforForgotpwd,setIsForForgotPwd] = useState(false);
  const [showVerifyOTPSignUpDetails, setShowVerifyOTPSignUpDetails] = useState(false);
  const [showRegisterForm,setShowRegisterForm] = useState(true);
  const [formErrors, setFormErrors] = useState({});
  const [loginInputs,setLoginInputs] = useState({
    "userId" : "",
    "password" : "",
  })
  const [registerInputs,setRegisterInputs] = useState({
    "fullName" : "",
    "email" : "",
    "password" : "",
  })
  const [forgotpwdInputs,setforgotpwdInputs] = useState({
    "userId" : "",
    "password" : "",
  })
  const [loading, setLoading] = useState(false);
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');

  useEffect(() => {
      // Simulate a login for testing purposes
      //setIsForLogin(true);
      // setisforRegister(false);
      // setisforForgotpwd(false);
      navigate("/");
  }, []); 
  const showAlert = (message, severity) => {
    setAlertMessage(message);
    setAlertSeverity(severity);
    setAlertOpen(true);
  };  
  const handleSendOTPCall = (event) => {
    event.preventDefault();
    setLoading(true);
    // const data = new FormData(event.currentTarget);
    // console.log({
    //   fullName: data.get('fullName'),
    //   email: data.get('email'),
    //   password: data.get('password'),
    // });
    UAMApi.registerUser(registerInputs,(response) =>
    {
      console.log(response);
      if(response)
        {
          setShowVerifyOTPSignUpDetails(true);
          setShowRegisterForm(false);
        }

    },
  (error)=>
  {
    console.log(error);
    setLoading(false);
    setShowVerifyOTPSignUpDetails(false);
    setShowRegisterForm(true);

  })
  };
    const handleForgotPasswordSubmit = (event) => {
      event.preventDefault();
      const data = new FormData(event.currentTarget);
      console.log({
        email: data.get('email'),
      });
    };
  const handleLoginSubmit = (event) => {
    event.preventDefault();
    setLoading(true);
    validateLoginForm(loginInputs);
    const isEmpty = Object.values(loginInputs).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateLoginForm(loginInputs);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);

    }
    else if (isEmpty) {
      showAlert("All fields are mandatory", "error");
      setLoading(false);

    }
    else
    {
    UAMApi.loginUser(loginInputs, (response) => {
      console.log(response)
      if(response.data.user)
      {
        dispatch(loginSuccess(response.data.user.name));
        navigate("/home");
        setLoading(false);
      }

    }, (error) => {
      console.log(error);
      setLoading(false);
    })
  }
  };
  const handleSwitchToRegister = () => {
    setIsForLogin(false);
    setIsForRegister(true);
    setIsForForgotPwd(false);
  };

  const handleSwitchToForgotPwd = () => {
    setIsForLogin(false);
    setIsForRegister(false);
    setIsForForgotPwd(true);
  };

  const handleSwitchToLogin = () => {
    setIsForLogin(true);
    setIsForRegister(false);
    setIsForForgotPwd(false);
  };
  const clearError = (field) => {
    console.log(formErrors)
    setFormErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
  };
  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };
  const handleLoginInputChange = (e) => {
    const { name, value } = e.target;
    setLoginInputs({ ...loginInputs, [name]: value });
    clearError(name);
  };
  const handleRegisterInputChange = (e) => {
    const { name, value } = e.target;
    setLoginInputs({ ...loginInputs, [name]: value });
    clearError(name);

    if (name === 'email' && !validateEmail(value)) {
      setFormErrors({ ...formErrors, email: 'Enter a valid email' });

    } else {
      setFormErrors({ ...formErrors, email: '' });
      clearError('email');

    }
  };
  const handleForgotPwdInputChange = (e) => {
    const { name, value } = e.target;
    setLoginInputs({ ...loginInputs, [name]: value });
    clearError(name);

    if (name === 'email' && !validateEmail(value)) {
      setFormErrors({ ...formErrors, email: 'Enter a valid email' });
    } else {
      setFormErrors({ ...formErrors, email: '' });
      clearError("email");

    }
  };
  const validateLoginForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.userId ) {
      errors.userId = 'This field is required';
    }
    if (!data.password) {
      errors.password = 'Password is required';
    } else if (scriptTagPattern.test(data.password)) {
      errors.category = 'Password must not contain script tags';
    }
    
    return errors;
  };
  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        
        {loading ? <Loader />:(<>
        {/* <Box component="main"></Box> */}
        {/* LOGIN PAGE STARTS */}
        {isforLogin && <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box component="form" onSubmit={handleLoginSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="userId"
              label="UserID/Email"
              name="userId"
              autoComplete="userId"
              autoFocus
              onChange={handleLoginInputChange}
              error={!!formErrors.userId}
              helperText={formErrors.userId}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={handleLoginInputChange}
              error={!!formErrors.password}
              helperText={formErrors.password}

            />
            {/* <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            /> */}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2" onClick ={handleSwitchToForgotPwd}>
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href="#" variant="body2" onClick={handleSwitchToRegister}>
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>}
        {/* LOGIN PAGE ENDS */}
        {/* REGISTRATION PAGE STARTS */}
        {isforRegister && <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box component="form" noValidate sx={{ mt: 3 }}>
          {showRegisterForm &&<Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  autoComplete="given-name"
                  name="fullName"
                  required
                  fullWidth
                  id="fullName"
                  label="Full Name"
                  autoFocus
                  onChange={handleRegisterInputChange}
                  error={!!formErrors.fullName}
                  helperText={formErrors.fullName}

                  
                />
              </Grid>
             
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  error={!!formErrors.email}
                  helperText={formErrors.email}
                  onChange={handleRegisterInputChange}
                  
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                  onChange={handleRegisterInputChange}
                  error={!!formErrors.password}
                  helperText={formErrors.password}

                  
                />
              </Grid>
            </Grid>}
            {showRegisterForm &&<Button
              type="button"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            onClick={handleSendOTPCall}>
              Send OTP
            </Button>}
            {showVerifyOTPSignUpDetails && <Button
              type="button"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Verify OTP
            </Button>}
            {showVerifyOTPSignUpDetails && <Button
              type="button"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>}
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Link href="#" variant="body2" onClick={handleSwitchToLogin}>
                  Already have an account? Sign in
                </Link>
              </Grid>
            </Grid>
          </Box>
          </Box>}
        {/* REGISTRATION PAGE ENDS */}
        {/* FORGOT PASSWORD PAGE STARTS */}
        {isforForgotpwd && <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Forgot Password
          </Typography>
          <Box component="form" noValidate onSubmit={handleForgotPasswordSubmit} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              error={!!formErrors.email}
              helperText={formErrors.email}
              onChange={handleForgotPwdInputChange}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Reset Password
            </Button>
          </Box>
        </Box>}
        {/* FORGOT PASSWORD PAGE ENDS */}
        <Copyright sx={{ mt: 8, mb: 4 }} />
        <Snackbar open={alertOpen} autoHideDuration={6000} onClose={() => setAlertOpen(false)}>
        <Alert onClose={() => setAlertOpen(false)} severity={alertSeverity} sx={{ width: '100%' }}>
          {alertMessage}
        </Alert>
      </Snackbar>
      </>)}
      </Container>
    </ThemeProvider>
  );
}

export default PreLogin;
