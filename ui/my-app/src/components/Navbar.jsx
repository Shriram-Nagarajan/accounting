import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { CssBaseline, Box, Toolbar, Typography, Container, AppBar, IconButton, Drawer, List, ListItemButton, ListItemText, Divider } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import { Link } from 'react-router-dom';
import Home from './Home';
import FileUpload from './ExpenseFileUploadForm';
import ExpenseInsights from './ExpenseInsights';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import InputRoundedIcon from '@mui/icons-material/InputRounded';
import InsightsRoundedIcon from '@mui/icons-material/InsightsRounded';
import WestRoundedIcon from '@mui/icons-material/WestRounded';
const drawerWidth = 240;


function NavbarAndDrawer()
{
    const [mobileOpen, setMobileOpen] = React.useState(false);
    const [drawerOpen, setDrawerOpen] = React.useState(true);
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

    const drawer = (
        <div>
            <Toolbar>
                {drawerOpen && (
                    <IconButton onClick={handleDrawerClose}>
                       <WestRoundedIcon /> {/* <ChevronLeftIcon /> */}
                    </IconButton>
                )}
                {/* <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
                    MyApp
                </Typography> */}
            </Toolbar>
            <Divider />
            <List>
                <ListItemButton component={Link} to="/">
                    <HomeRoundedIcon color="action" fontSize="large" />&nbsp;<ListItemText primary="Home" />
                </ListItemButton>
                <ListItemButton component={Link} to="/file-upload">
                    <InputRoundedIcon color="action" fontSize="large"/>&nbsp;<ListItemText primary="File Upload" />
                </ListItemButton>
                <ListItemButton component={Link} to="/expense-insights">
                    <InsightsRoundedIcon color="action" fontSize="large"/>&nbsp;<ListItemText primary="Expense Insights" />
                </ListItemButton>
            </List>
        </div>
    );
    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar
                position="fixed"
                sx={{
                    width: { sm: `calc(100% - ${drawerOpen ? drawerWidth : 0}px)` },
                    ml: { sm: `${drawerOpen ? drawerWidth : 0}px` }
                }}
            >
                <Toolbar>
                    {!drawerOpen && (
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            edge="start"
                            onClick={handleDrawerOpen}
                            sx={{ mr: 2, ...(drawerOpen && { display: 'none' }) }}
                        >
                            <MenuIcon />
                        </IconButton>
                    )}
                    <Typography variant="h6" noWrap component="div">
                        MyApp
                    </Typography>
                </Toolbar>
            </AppBar>
            <Box
                component="nav"
                sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
                aria-label="mailbox folders"
            >
                <Drawer
                    variant="temporary"
                    open={mobileOpen}
                    onClose={handleDrawerToggle}
                    ModalProps={{
                        keepMounted: true, // Better open performance on mobile.
                    }}
                    sx={{
                        display: { xs: 'block', sm: 'none' },
                        '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                    }}
                >
                    {drawer}
                </Drawer>
                <Drawer
                    variant="persistent"
                    open={drawerOpen}
                    sx={{
                        display: { xs: 'none', sm: 'block' },
                        '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                    }}
                >
                    {drawer}
                </Drawer>
            </Box>
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                    transition: theme.transitions.create('margin', {
                        easing: theme.transitions.easing.sharp,
                        duration: theme.transitions.duration.leavingScreen,
                    }),
                    marginLeft: { sm: `${drawerOpen ? 0 : `-${drawerWidth}px`}` },
                }}
            >
                <Toolbar />
                <Container>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/file-upload" element={<FileUpload />} />
                        <Route path="/expense-insights" element={<ExpenseInsights />} />
                    </Routes>
                </Container>
            </Box>
        </Box>
    );

}


export default NavbarAndDrawer;


// import React from 'react';
// import AppBar from '@mui/material/AppBar';
// import Toolbar from '@mui/material/Toolbar';
// import Typography from '@mui/material/Typography';
// import IconButton from '@mui/material/IconButton';
// import MenuIcon from '@mui/icons-material/Menu';
// import Drawer from '@mui/material/Drawer';
// import List from '@mui/material/List';
// import ListItemButton from '@mui/material/ListItemButton';
// import ListItemText from '@mui/material/ListItemText';
// import Box from '@mui/material/Box';
// import useMediaQuery from '@mui/material/useMediaQuery';
// import { useTheme } from '@mui/material/styles';
// import { Link } from 'react-router-dom';
// import Divider from '@mui/material/Divider';

// function NavBar() {
//     const [drawerOpen, setDrawerOpen] = React.useState(false);
//     const theme = useTheme();
//     const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

//     const toggleDrawer = (open) => (event) => {
//         if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
//             return;
//         }
//         setDrawerOpen(open);
//     };

//     const list = (
//         <Box
//             sx={{ width: 250 }}
//             role="presentation"
//             onClick={toggleDrawer(false)}
//             onKeyDown={toggleDrawer(false)}
//         >
//              <Box sx={{ padding: '16px' }}>
//                 <Typography variant="h6">Financial Freedom</Typography>
//             </Box>
//                     <Divider />
//                     <List>
//                 <ListItemButton component={Link} to="/">
//                     <ListItemText primary="HOME" />
//                 </ListItemButton>
//                 <ListItemButton component={Link} to="/file-upload">
//                     <ListItemText primary="INPUT DATA" />
//                 </ListItemButton>
//                 <ListItemButton component={Link} to="/expense-insights">
//                     <ListItemText primary="EXPENSE INSIGHTS" />
//                 </ListItemButton>
//             </List>
//             {/* <List>
//                 <ListItem button component={Link} href="#home">
//                     <ListItemText primary="Home" />
//                 </ListItem>
//                 <ListItem button component={Link} href="#file-upload">
//                     <ListItemText primary="File Upload" />
//                 </ListItem>
//                 <ListItem button component={Link} href="#expense-insights">
//                     <ListItemText primary="Expense Insights" />
//                 </ListItem>
//             </List> */}
//         </Box>
//     );

//     return (
//         <div>
//             <AppBar position="static">
//                 <Toolbar>
//                     {isMobile && (
//                         <IconButton
//                             edge="start"
//                             color="inherit"
//                             aria-label="menu"
//                             onClick={toggleDrawer(true)}
//                         >
//                             <MenuIcon />
//                         </IconButton>
//                     )}
//                     <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
//                         Financial Freedom
//                     </Typography>
//                     {!isMobile && (
//                         <Box>
//                             <IconButton color="inherit" component={Link} to="/">HOME</IconButton>
//                             <IconButton color="inherit" component={Link} to="/file-upload">INPUT DATA</IconButton>
//                             <IconButton color="inherit" component={Link} to="/expense-insights">EXPENSE INSIGHTS</IconButton>
//                         </Box>
//                     )}
//                 </Toolbar>
//             </AppBar>
//             <Drawer
//                 anchor="left"
//                 open={drawerOpen}
//                 onClose={toggleDrawer(false)}
//             >
//                 {list}
//             </Drawer>
//         </div>
//     );
// }

// export default NavBar;
