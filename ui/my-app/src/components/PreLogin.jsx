// src/SignIn.js
import React, { useState, useEffect, useRef } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';
import Avatar from '@mui/material/Avatar';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import UAMApi from '../httputil/uam';
import { useDispatch } from 'react-redux';
import { loginSuccess } from '../actions/authActions';
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
  //common
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [formErrors, setFormErrors] = useState({});
  const [email, setEmail] = useState('');
  const [emailError, setEmailError] = useState('');
  const [otp, setOtp] = useState(Array(6).fill(''));
  const [concatOTPForRegister,setConcatOTPOTPForRegister] = useState('');
  const [otpErrors, setOtpErrors] = useState(Array(6).fill(false));
  const inputRefs = useRef([]);
  const [loading, setLoading] = useState(false);
  const [alertOpen, setAlertOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState('');
  const [alertSeverity, setAlertSeverity] = useState('success');

  //related to login
  const [isforLogin, setIsForLogin] = useState(true);
  const [loginInputs, setLoginInputs] = useState({
    "userId": "",
    "password": "",
  })
  //related to register
  const [isforRegister, setIsForRegister] = useState(false);
  const [showVerifyOTPSignUpDetails, setShowVerifyOTPSignUpDetails] = useState(false);
  const [showEmailForRegisterForm, setShowEmailForRegisterForm] = useState(true);
  const [showRegisterForm, setShowRegisterForm] = useState(false);
  const [registerInputs, setRegisterInputs] = useState({
    "fullName": "",
    "password": "",
    "confirmPassword":"",
  })

  //related to forgot password
  const [isforForgotpwd, setIsForForgotPwd] = useState(false);
  const [showEmailForForgotPwdForm, setShowEmailForForgotPwdForm] = useState(true);
  const [showVerifyOTPPwdResetDetails, setShowVerifyOTPPwdResetDetails] = useState(false);
  const [showResetPwdForm, setShowResetPwdForm] = useState(false);
  const [forgotpwdInputs, setForgotPwdInputs] = useState({
    "password": "",
    "confirmPassword": "",
  })

  useEffect(() => {
    // Simulate a login for testing purposes
    //setIsForLogin(true);
    // setisforRegister(false);
    // setisforForgotpwd(false);
    navigate("/");
  }, []);
  //common methods
  const showAlert = (message, severity) => {
    setAlertMessage(message);
    setAlertSeverity(severity);
    setAlertOpen(true);
  };
  const clearError = (field) => {
    console.log(formErrors)
    setFormErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
  };
  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };
  const validateOtp = (otp) => {
    const otpRegex = /^\d{6}$/;
    return otpRegex.test(otp);
  };
  // const handleOtpChange = (e) => {
  //   const value = e.target.value;
  //   setOtp(value);
  //   if (!validateOtp(value)) {
  //     setOtpError('Enter a valid 6-digit OTP');
  //   } else {
  //     setOtpError('');
  //   }
  // };
  // const handleotpChange = (element, index) => {
  //   const value = element.value;
  //   const newOtp = [...otp];
  //   const newErrors = [...errors];

  //   if (/[^0-9]/.test(value)) {
  //     newErrors[index] = true;
  //   } else {
  //     newErrors[index] = false;
  //   }

  //   newOtp[index] = value;
  //   setOtp(newOtp);
  //   setErrors(newErrors);

  //   // Focus next input box if current input is valid and not empty
  //   if (value && !newErrors[index] && element.nextSibling) {
  //     element.nextSibling.focus();
  //   }
  // };

  const handleOTPChange = (element, index) => {
    const value = element.value;
    if (/[^0-9]/.test(value)) {
      return; // Only allow numeric input
    }

    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);

    // Focus next input box
    // if (value && element.nextSibling) {
    //   element.nextSibling.focus();
    // }
    if (value && index < 5) {
      inputRefs.current[index + 1].focus();
    }
  };
  const handleKeyDown = (event, index) => {
    if (event.key === 'Backspace' && !otp[index]) {
      if (index > 0) {
        inputRefs.current[index - 1].focus();
      }
    }
  };
  //login page related methods
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
    else {
      UAMApi.loginUser(loginInputs, (response) => {
        console.log(response)
        if (response.data.user) {
          dispatch(loginSuccess(response.data.user.name));
          navigate("/home");
          setLoading(false);
        }


      }, (errorResponse) => {
        console.log(errorResponse);
        setLoading(false);
        if (errorResponse.response.data.message == "PASSWORD_INCORRECT") {
          showAlert("Incorrect password. Login with correct credentials", "error");

        }
        else if (errorResponse.response.data.message == "USERID_INCORRECT") {
          showAlert("Incorrect userid. Login with correct credentials", "error");

        }

      })
    }
  };
  const handleSwitchToLogin = () => {
    setIsForLogin(true);
    setIsForRegister(false);
    setIsForForgotPwd(false);
  };
  const handleLoginInputChange = (e) => {
    const { name, value } = e.target;
    setLoginInputs({ ...loginInputs, [name]: value });
    clearError(name);
  };
  const validateLoginForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.userId) {
      errors.userId = 'This field is required';
    }
    if (!data.password) {
      errors.password = 'Password is required';
    } else if (scriptTagPattern.test(data.password)) {
      errors.category = 'Password must not contain script tags';
    }

    return errors;
  };
  //register page related methods  
  const handleSendOTPCall = (event) => {

    event.preventDefault();
    setLoading(true);
    if (email === "") {
      setEmailError("Email is mandatory");
      showAlert("Email is mandatory", "error");
      setLoading(false);
    }
    else if (emailError) {
      console.log(emailError);
      showAlert(emailError, "error");
      setLoading(false);
    }
    else {
      const reqData = {
        "emailId": email,
      }
      UAMApi.sendOTPForRegister(reqData, (response) => {
        console.log(response);
        if (response.data.message === "TOKEN_SENT") {
          showAlert("OTP sent to given email ID", "success");
          setOtp(['','','','','','']);
          setShowVerifyOTPSignUpDetails(true);
          setShowEmailForRegisterForm(false);
          setShowRegisterForm(false);
          setLoading(false);

        }
        

      },
        (error) => {
          console.log(error);
          if (error.response.data.message === "EMAIL_ID_EMPTY" || error.response.data.message === "REQD_PARAMS_NOT_PROVIDED") {
            showAlert("Please provide email ID", "error");
            setShowVerifyOTPSignUpDetails(false);
            setShowEmailForRegisterForm(true);
            setShowRegisterForm(false);
            setLoading(false);
  
          }
          else if (error.response.data.message === "USER_ALREADY_EXISTS") {
            showAlert("You are already our valued user.Please login", "error");
            setShowVerifyOTPSignUpDetails(false);
            setShowEmailForRegisterForm(false);
            setShowRegisterForm(false);
            handleSwitchToLogin();
            setLoading(false);
  
          }        })


    }

  };
  const verifyOTP = (event) => {

    event.preventDefault();
    setLoading(true);
    console.log(otp);
    var dummyvar = '';
    otp.map(x => {
      dummyvar += x;
    })
    setConcatOTPOTPForRegister(dummyvar);
    console.log(concatOTPForRegister);
    if (dummyvar === "") {

      showAlert("OTP is mandatory", "error");
      setLoading(false);
    }

    else {
      const reqData = {
        "emailId": email,
        "token": dummyvar,
      }
      UAMApi.verifyOTPForRegister(reqData, (response) => {
        console.log(response);
        if (response.data.message === "VERIFIED_SUCCESSFULLY") {
          //setOtp(['','','','','','']);
          showAlert("OTP verified successfully. Enter your details to register", "success");
          setShowVerifyOTPSignUpDetails(false);
          setShowEmailForRegisterForm(false);
          setShowRegisterForm(true);
          setLoading(false);

        }
        

      },
        (error) => {
          console.log(error);
          if (error.response.data.message === "USER_ALREADY_EXISTS") {
            setOtp(['', '', '', '', '', '']);
            showAlert("You are already our valued user.Please login", "error");
            setShowVerifyOTPSignUpDetails(false);
            setShowEmailForRegisterForm(false);
            setShowRegisterForm(false);
            handleSwitchToLogin();
            setLoading(false);
  
          }
          else if (error.response.data.message === "TOKEN_EXPIRED") {
            setOtp(['', '', '', '', '', '']);
            showAlert("OTP expired.Please generate again", "error");
            setShowVerifyOTPSignUpDetails(false);
            setShowEmailForRegisterForm(true);
            setShowRegisterForm(false);
            setLoading(false);
  
          }
          else if (error.response.data.message === "INVALID_TOKEN_OR_EMAIL") {
            setOtp(['', '', '', '', '', '']);
            showAlert("Invalid/incorrect OTP provided.Please enter again", "error");
            setShowVerifyOTPSignUpDetails(true);
            setShowEmailForRegisterForm(false);
            setShowRegisterForm(false);
            setLoading(false);
  
          }
        })


    }

  };
  const handleRegisterSubmit = (event) => {
    event.preventDefault();
    setLoading(true);
    validateRegisterForm(registerInputs);
    const isEmpty = Object.values(registerInputs).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateRegisterForm(registerInputs);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);

    }
    else if (isEmpty) {
      showAlert("All fields are mandatory", "error");
      setLoading(false);

    }
    else {
      const reqData = {
        "userId": email,
        "name": registerInputs.fullName,
        "emailId": email,
        "token": concatOTPForRegister,
        "password": registerInputs.password,
        "confirmPassword": registerInputs.confirmPassword,
    }
      UAMApi.registerUser(reqData, (response) => {
        console.log(response)
        if (response.data.message === "SUCCESS") {
          dispatch(loginSuccess(response.data.user.name));
          navigate("/home");
          // handleSwitchToLogin();
          setLoading(false);
        }
        else if (response.data.message === "USER_ALREADY_EXISTS") {
          showAlert("You are already our valued user.Please login", "error");
          handleSwitchToLogin();
          setLoading(false);
        }
        else if (response.data.message === "UNVERIFIED_TOKEN") {
          showAlert("Your OTP is not verified. Please register again", "error");
          handleSwitchToRegister();
          setLoading(false);
        }
        else if (response.data.message === "INVALID_TOKEN_OR_EMAIL") {
          showAlert("Something went wrong.Please register again", "error");
          handleSwitchToRegister();
          setLoading(false);
        }
        else if (response.data.message === "TOKEN_EXPIRED") {
          showAlert("OTP expired.Please generate again", "error");
          handleSwitchToRegister();
          setLoading(false);

        }
        else if (response.data.message === "PASSWORDS_DOESNT_MATCH") {
          showAlert("Password and confirm password should match. Please enter details again", "error");
          handleSwitchToRegister();
          setLoading(false);

        }



      }, (errorResponse) => {
        console.log(errorResponse);
        setLoading(false);
        // if(errorResponse.response.data.message == "PASSWORD_INCORRECT")
        //   {
        //     showAlert("Incorrect password. Login with correct credentials","error");

        //   } 
        // else if(errorResponse.response.data.message == "USERID_INCORRECT")
        //     {
        //       showAlert("Incorrect userid. Login with correct credentials","error");

        //     } 

      })
    }
  };
  const handleSwitchToRegister = () => {
    setIsForLogin(false);
    setIsForRegister(true);
    setShowEmailForRegisterForm(true);
    setShowVerifyOTPSignUpDetails(false);
    setShowRegisterForm(false);
    setIsForForgotPwd(false);
  };
  const handleEmailInRegisterChange = (e) => {
    if (e.target.value === "") {
      setEmailError("Email is mandatory");
    }
    else if (!validateEmail(e.target.value)) {
      setEmailError("Enter a valid email");
    }
    else {
      setEmailError("");
    }
    setEmail(e.target.value);
  }
  const handleRegisterInputChange = (e) => {

    const { name, value } = e.target;
    setRegisterInputs({ ...registerInputs, [name]: value });
    clearError(name);
    if (name === 'confirmPassword' && value != registerInputs['password']) {
      setFormErrors({ ...formErrors, confirmPassword: 'Password and confirm password should match' });
    } else {
      setFormErrors({ ...formErrors, confirmPassword: '' });
      clearError("confirmPassword");

    }
  };
  const validateRegisterForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.fullName) {
      errors.fullName = 'Name is required';
    }
    if (!data.password) {
      errors.password = 'Password is required';
    }

    return errors;
  };
  //forgot password page related methods
  const handleForgotPasswordSubmit = (event) => {
    event.preventDefault();
    setLoading(true);
    validateForgotPwdForm(forgotpwdInputs);
    const isEmpty = Object.values(forgotpwdInputs).some(value =>
      (typeof value === 'string' && value.trim() === '') ||
      value === null ||
      value === undefined
    );
    const errors = validateForgotPwdForm(forgotpwdInputs);
    if (Object.keys(errors).length > 0) {
      setFormErrors(errors);
      setLoading(false);

    }
    else if (isEmpty) {
      showAlert("All fields are mandatory", "error");
      setLoading(false);

    }
    else {
      UAMApi.resetPassword(forgotpwdInputs, (response) => {
        console.log(response)
        if (response.data.user) {
          // dispatch(loginSuccess(response.data.user.name));
          // navigate("/home");
          handleSwitchToLogin();
          setLoading(false);
        }


      }, (errorResponse) => {
        console.log(errorResponse);
        setLoading(false);
        // if(errorResponse.response.data.message == "PASSWORD_INCORRECT")
        //   {
        //     showAlert("Incorrect password. Login with correct credentials","error");

        //   } 
        // else if(errorResponse.response.data.message == "USERID_INCORRECT")
        //     {
        //       showAlert("Incorrect userid. Login with correct credentials","error");

        //     } 

      })
    }
  };
  const handleSwitchToForgotPwd = () => {
    setIsForLogin(false);
    setIsForRegister(false);
    setIsForForgotPwd(true);
    setShowEmailForForgotPwdForm(true);
    setShowVerifyOTPPwdResetDetails(false);
    setShowResetPwdForm(false);

  };
  const handleForgotPwdInputChange = (e) => {
    const { name, value } = e.target;
    setForgotPwdInputs({ ...forgotpwdInputs, [name]: value });
    clearError(name);

    if (name === 'confirmPassword' && value != forgotpwdInputs['password']) {
      setFormErrors({ ...formErrors, confirmPassword: 'Password and confirm password should match' });
    } else {
      setFormErrors({ ...formErrors, confirmPassword: '' });
      clearError("confirmPassword");

    }
  };
  const handleEmailInForgotPwdChange = (e) => {
    if (e.target.value === "") {
      setEmailError("Email is mandatory");
    }
    else if (!validateEmail(e.target.value)) {
      setEmailError("Enter a valid email");
    }
    else {
      setEmailError("");
    }
    setEmail(e.target.value);
  }
  const handleSendOTPPwdresetCall = (event) => {

    event.preventDefault();
    setLoading(true);
    if (email === "") {
      setEmailError("Email is mandatory");
      showAlert("Email is mandatory", "error");
      setLoading(false);
    }
    else if (emailError) {
      console.log(emailError);
      showAlert(emailError, "error");
      setLoading(false);
    }
    else {
      UAMApi.sendOTPForForgotPassword(email, (response) => {
        console.log(response);
        if (response) {
          showAlert("OTP sent to the given email ID", "success")
          setShowVerifyOTPPwdResetDetails(true);
          setShowEmailForForgotPwdForm(false);
          setShowResetPwdForm(false);
          setLoading(false);

        }

      },
        (error) => {
          console.log(error);
          showAlert("", "error")
          setShowVerifyOTPPwdResetDetails(false);
          setShowEmailForForgotPwdForm(true);
          setShowResetPwdForm(false);
          setLoading(false);

        })


    }

  };
  const verifyOTPforPwdReset = (event) => {

    event.preventDefault();
    setLoading(true);
    console.log(otp);
    var concatOTP = '';
    otp.map(x => {
      concatOTP += x;
    })
    console.log(concatOTP);
    if (concatOTP === "") {

      showAlert("OTP is mandatory", "error");
      setLoading(false);
    }

    else {
      UAMApi.verifyOTPForForgotPassword(concatOTP, (response) => {
        console.log(response);
        if (response) {
          showAlert("OTP verified successfully. You can reset your password now", "success");
          setShowVerifyOTPPwdResetDetails(false);
          setShowEmailForForgotPwdForm(false);
          setShowResetPwdForm(true);
          setLoading(false);

        }

      },
        (error) => {
          console.log(error);
          showAlert("", "error");
          setShowVerifyOTPPwdResetDetails(true);
          setShowEmailForForgotPwdForm(false);
          setShowResetPwdForm(false);
          setLoading(false);

        })


    }

  };
  const validateForgotPwdForm = (data) => {
    const errors = {};
    const scriptTagPattern = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi;

    if (!data.password) {
      errors.password = 'Password is required';
    }
    if (!data.confirmPassword) {
      errors.confirmPassword = 'Confirm password is required';
    }

    return errors;
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />

        {loading ? <Loader /> : (<>
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
                  <Link href="#" variant="body2" onClick={handleSwitchToForgotPwd}>
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

              {/* <Grid container spacing={2}>
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
              </Grid> */}

              {/* <Grid item xs={12}> */}
              {showEmailForRegisterForm && <TextField
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                error={!!emailError}
                helperText={emailError}
                onChange={handleEmailInRegisterChange}

              />}
              {/* </Grid> 
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
            </Grid> */}
              {showEmailForRegisterForm && <Button
                type="button"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={handleSendOTPCall}>
                Send OTP
              </Button>}
              {showVerifyOTPSignUpDetails && <Grid container spacing={2}>
                <Grid item xs={12}>
                  {/* <TextField
              margin="normal"
              required
              fullWidth
              name="otp"
              label="OTP"
              type="text"
              id="otp"
              autoComplete="otp"
              onChange={handleOtpChange}
              error={!!otpError}
              helperText={otpError}
            /> */}
                  {otp.map((data, index) => (
                    <TextField
                      key={index}
                      value={data}
                      onChange={e => handleOTPChange(e.target, index)}
                      onKeyDown={e => handleKeyDown(e, index)}
                      inputProps={{ maxLength: 1, style: { textAlign: 'center' } }}
                      style={{ width: '3rem', margin: '5px' }}
                      inputRef={(el) => (inputRefs.current[index] = el)}
                    />
                  ))}
                </Grid>
              </Grid>}
              {showVerifyOTPSignUpDetails && <Button
                type="button"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={verifyOTP}
              >
                Verify OTP
              </Button>}
              {showRegisterForm && <Grid container spacing={2}>
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
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="confirmPassword"
                    label="Confirm Password"
                    type="password"
                    id="confirmPassword"
                    autoComplete="new-password"
                    onChange={handleRegisterInputChange}
                    error={!!formErrors.confirmPassword}
                    helperText={formErrors.confirmPassword}


                  />
                </Grid>
              </Grid>}
              {showRegisterForm && <Button
                type="button"
                fullWidth
                variant="contained"
                color='success'
                sx={{ mt: 3, mb: 2 }}
                onClick={handleRegisterSubmit}
              >
                Register
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

            <Box component="form" noValidate sx={{ mt: 1 }}>
              {showEmailForForgotPwdForm && <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                error={emailError}
                helperText={emailError}
                onChange={handleEmailInForgotPwdChange}
              />}
              {showEmailForForgotPwdForm && <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={handleSendOTPPwdresetCall}
              >
                Send OTP to verify
              </Button>}
              {showVerifyOTPPwdResetDetails && <Grid container spacing={2}>
                <Grid item xs={12}>
                  {/* <TextField
              margin="normal"
              required
              fullWidth
              name="otp"
              label="OTP"
              type="text"
              id="otp"
              autoComplete="otp"
              onChange={handleOtpChange}
              error={!!otpError}
              helperText={otpError}
            /> */}
                  {otp.map((data, index) => (
                    <TextField
                      key={index}
                      value={data}
                      onChange={e => handleOTPChange(e.target, index)}
                      onKeyDown={e => handleKeyDown(e, index)}
                      inputProps={{ maxLength: 1, style: { textAlign: 'center' } }}
                      style={{ width: '3rem', margin: '5px' }}
                      inputRef={(el) => (inputRefs.current[index] = el)}
                    />
                  ))}
                </Grid>
              </Grid>}
              {showVerifyOTPPwdResetDetails && <Button
                type="button"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                onClick={verifyOTPforPwdReset}
              >
                Verify OTP
              </Button>}
              {showResetPwdForm && <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    autoComplete="password"
                    name="password"
                    required
                    fullWidth
                    id="password"
                    label="Password"
                    type="password"
                    autoFocus
                    onChange={handleForgotPwdInputChange}
                    error={!!formErrors.password}
                    helperText={formErrors.password}


                  />
                </Grid>


                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    name="confirmPassword"
                    label="Confirm Password"
                    type="password"
                    id="confirmPassword"
                    autoComplete="new-password"
                    onChange={handleForgotPwdInputChange}
                    error={!!formErrors.confirmPassword}
                    helperText={formErrors.confirmPassword}


                  />
                </Grid>
              </Grid>}
              {showResetPwdForm && <Button
                type="button"
                fullWidth
                variant="contained"
                color='success'
                sx={{ mt: 3, mb: 2 }}
                onClick={handleForgotPasswordSubmit}
              >
                Reset Password
              </Button>}

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
