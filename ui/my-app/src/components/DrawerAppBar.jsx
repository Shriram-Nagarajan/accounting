import React, { useState } from "react";
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import MenuIcon from '@mui/icons-material/Menu';
import ExpenseFileUploadForm from './ExpenseFileUploadForm'; // Import your ExpenseFileUploadForm component here
import ExpenseInsights from "./ExpenseInsights";
import { Container, Paper, Typography, Grid, Button, Box, AppBar, Toolbar, Snackbar, Alert } from '@mui/material';


const drawerWidth = 240;
const navItems = ['Home', 'Input data', 'Expense Insights'];

export default function DrawerAppBar(props) {
  const { window } = props;
  const [mobileOpen, setMobileOpen] = React.useState(false);
  const [selectedNavItem, setSelectedNavItem] = useState(navItems[0]); // Default to 'Home' as selected

  const handleDrawerToggle = () => {
    setMobileOpen((prevState) => !prevState);
  };

  const handleNavItemClick = (item) => {
    setSelectedNavItem(item);
    handleDrawerToggle(); // Close the drawer on mobile when an item is clicked
  };

  const drawer = (
    <Box onClick={handleDrawerToggle} sx={{ textAlign: 'center' }}>
      <Typography variant="h6" sx={{ my: 2 }}>
        FinancialFreedom
      </Typography>
      <Divider />
      <List>
        {navItems.map((item) => (
          <ListItem key={item} disablePadding>
            <ListItemButton
              selected={selectedNavItem === item}
              onClick={() => handleNavItemClick(item)}
              sx={{ textAlign: 'center' }}
            >
              <ListItemText primary={item} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );

  const container = window !== undefined ? () => window().document.body : undefined;

  // const renderSelectedComponent = () => {
  //   switch (selectedNavItem) {
  //     case 'Home':
  //       return <Typography variant="h2">Home Component</Typography>; // Replace with your Home component
  //     case 'Input data':
  //       return <ExpenseFileUploadForm />; // Render ExpenseFileUploadForm component
  //     case 'Expense Insights':
  //       return <ExpenseInsights />;//<Typography variant="h2">Expense Insights Component</Typography>; // Replace with your Expense Insights component
  //     default:
  //       return null;
  //   }
  // };

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar component="nav">
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2, display: { sm: 'none' } }}
          >
            <MenuIcon />
          </IconButton>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, display: { xs: 'none', sm: 'block' } }}
          >
            Financial Freedom
          </Typography>
          <Box sx={{ display: { xs: 'none', sm: 'block' } }}>
            {navItems.map((item) => (
              <Button
                key={item}
                sx={{ color: '#fff' }}
                onClick={() => handleNavItemClick(item)}
                variant={selectedNavItem === item ? "contained" : "text"}
              >
                {item}
              </Button>
            ))}
          </Box>
        </Toolbar>
      </AppBar>
      <nav>
        <Drawer
          container={container}
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
      </nav>
      <Box component="main" sx={{ p: 3 }}>
        <Toolbar />
        {/* <Container maxWidth="lg" sx={{width:'100%'}}>

        {renderSelectedComponent()}
        </Container> */}
      </Box>
    </Box>
  );
}