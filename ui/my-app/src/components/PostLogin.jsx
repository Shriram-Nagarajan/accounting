import React, { useEffect,useState } from 'react';
import { Routes, Route,useNavigate  } from 'react-router-dom';
import { CssBaseline, Box, Toolbar, Typography, Container, AppBar, IconButton, Drawer, List, ListItemButton, ListItemText, Divider } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import WestRoundedIcon from '@mui/icons-material/WestRounded';
import { Link } from 'react-router-dom';
import Home from './Home';
import FileUpload from './ExpenseFileUploadForm';
import ExpenseInsights from './ExpenseInsights';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import InputRoundedIcon from '@mui/icons-material/InputRounded';
import InsightsRoundedIcon from '@mui/icons-material/InsightsRounded';
import MoneyRoundedIcon from '@mui/icons-material/MoneyRounded';
import Footer from './Footer';
import UserAvatar from '../components/Avatar';
import UserDropdown from '../components/DropdownforUserAvatar';
import { logout } from '../actions/authActions';
import constants from '../common/constants';
import { useSelector } from 'react-redux';
import { useDispatch } from 'react-redux';
import Income from './Income';
import UAMApi from '../httputil/uam';
import Loader from '../components/Loader';

const drawerWidth = 240;

function PostLogin(props) {
    const userName = useSelector(state => state.auth.userName)// assuming you store user info in Redux state
    const navigate = useNavigate();
    const dispatch = useDispatch();
    useEffect(() => {
        const currentPath = window.location.pathname;
        // console.log(currentPath);
        // console.log(props.startPage);
        if (props.startPage && currentPath !== props.startPage) {
            navigate(props.startPage);
        }
        // if (props.startPage && currentPath === "/") {
        //     navigate(props.startPage);
        // }
        
    }, []);
    
    const [mobileOpen, setMobileOpen] = useState(false);
    const [drawerOpen, setDrawerOpen] = useState(true);
    const [loading, setLoading] = useState(false);

    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    const handleDrawerOpen = () => {
        setDrawerOpen(true);
    };

    const handleDrawerClose = () => {
        setDrawerOpen(false);
    };
    const handleLogout = () => {
        setLoading(true);
        UAMApi.logoutUser(
            '',
            (response) => {
                console.log("logout user", response);
                dispatch(logout());
                navigate(constants.loginURL);
                setLoading(false);

            },
            (error) => {
                console.error("Error fetching session", error);
                dispatch(logout()); // dispatch the logout action
                navigate(constants.loginURL);
                setLoading(false);
            }
        );
      };
    const drawer = (
        <div>
            <Toolbar>
                {drawerOpen && (
                    <IconButton onClick={handleDrawerClose}>
                       <WestRoundedIcon />
                    </IconButton>
                )}
                <Typography variant="h6" sx={{ my: 2 }}>
        FinancialFreedom
      </Typography>
            </Toolbar>
            <Divider />
            
            <List>
                <ListItemButton component={Link} to="/home">
                    <HomeRoundedIcon color="action" fontSize="large" />&nbsp;<ListItemText primary="Home" />
                </ListItemButton>
                <ListItemButton component={Link} to="/file-upload">
                    <InputRoundedIcon color="action" fontSize="large"/>&nbsp;<ListItemText primary="File Upload" />
                </ListItemButton>
                <ListItemButton component={Link} to="/expense-insights">
                    <InsightsRoundedIcon color="action" fontSize="large"/>&nbsp;<ListItemText primary="Expense Insights" />
                </ListItemButton>
                <ListItemButton component={Link} to="/income-insights">
                    <MoneyRoundedIcon color="action" fontSize="large"/>&nbsp;<ListItemText primary="Income Insights" />
                </ListItemButton>
            </List>
        </div>
    );

    return (
        loading? <Loader />:
        <>
            <Box sx={{ display: 'flex',minHeight: '90vh' }}>
                <CssBaseline />
                <AppBar position="fixed">
                    <Toolbar>
                        <div>
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            edge="start"
                            onClick={isMobile ? handleDrawerToggle : handleDrawerOpen}
                            sx={{ mr: 2 }}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="h6" noWrap component="div" sx={{marginLeft: '30px',marginTop: '-36px'}}>
                            Financial Freedom
                        </Typography>
                        </div>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {/* <Box sx={{ ml: 130 }}>
              <UserAvatar userName={userName} />
            </Box> */}
            <Box sx={{ ml: 115 }}>
            <UserDropdown userName={userName} onLogout={handleLogout} />
            {/* userName={userName} */}
            </Box>
          </Box>
                    </Toolbar>
                </AppBar>
                

                <Box
                    component="nav"
                    sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
                    aria-label="mailbox folders"
                >
                    <Drawer
                        variant={isMobile ? "temporary" : "persistent"}
                        open={isMobile ? mobileOpen : drawerOpen}
                        onClose={handleDrawerToggle}
                        ModalProps={{
                            keepMounted: true, // Better open performance on mobile.
                        }}
                        sx={{
                            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                        }}
                    >
                        {drawer}
                    </Drawer>
                </Box>
                <Box
                    component="main"
                    sx={{
                        width:'100%',
                        flexGrow: 1,
                        p: 3,
                        //paddingBottom: '64px',
                        transition: theme.transitions.create('margin', {
                            easing: theme.transitions.easing.sharp,
                            duration: theme.transitions.duration.leavingScreen,
                        }),
                        //marginLeft: { sm: `${drawerOpen ? {drawerWidth} : 0}px` },
                        marginLeft: { sm: `${drawerOpen ? 0 : `-${drawerWidth}px`}` },
                    }}
                >
                    <Toolbar />
                    <Container>
                        <Routes>
                            <Route path="/home" element={<Home />} />
                            <Route path="/file-upload" element={<FileUpload />} />
                            <Route path="/expense-insights" element={<ExpenseInsights />} />
                            <Route path="/income-insights" element={<Income />} />
                        </Routes>
                    </Container>
                </Box>
            </Box>
            <Footer drawerOpen={drawerOpen} />
            
        </>
    );
}

export default PostLogin;


