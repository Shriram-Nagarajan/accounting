import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import Box from '@mui/material/Box';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { Link } from 'react-router-dom';
import Divider from '@mui/material/Divider';

function NavBar() {
    const [drawerOpen, setDrawerOpen] = React.useState(false);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const toggleDrawer = (open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }
        setDrawerOpen(open);
    };

    const list = (
        <Box
            sx={{ width: 250 }}
            role="presentation"
            onClick={toggleDrawer(false)}
            onKeyDown={toggleDrawer(false)}
        >
             <Box sx={{ padding: '16px' }}>
                <Typography variant="h6">Financial Freedom</Typography>
            </Box>
                    <Divider />
                    <List>
                <ListItemButton component={Link} to="/">
                    <ListItemText primary="HOME" />
                </ListItemButton>
                <ListItemButton component={Link} to="/file-upload">
                    <ListItemText primary="INPUT DATA" />
                </ListItemButton>
                <ListItemButton component={Link} to="/expense-insights">
                    <ListItemText primary="EXPENSE INSIGHTS" />
                </ListItemButton>
            </List>
            {/* <List>
                <ListItem button component={Link} href="#home">
                    <ListItemText primary="Home" />
                </ListItem>
                <ListItem button component={Link} href="#file-upload">
                    <ListItemText primary="File Upload" />
                </ListItem>
                <ListItem button component={Link} href="#expense-insights">
                    <ListItemText primary="Expense Insights" />
                </ListItem>
            </List> */}
        </Box>
    );

    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    {isMobile && (
                        <IconButton
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            onClick={toggleDrawer(true)}
                        >
                            <MenuIcon />
                        </IconButton>
                    )}
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Financial Freedom
                    </Typography>
                    {!isMobile && (
                        <Box>
                            <IconButton color="inherit" component={Link} to="/">HOME</IconButton>
                            <IconButton color="inherit" component={Link} to="/file-upload">INPUT DATA</IconButton>
                            <IconButton color="inherit" component={Link} to="/expense-insights">EXPENSE INSIGHTS</IconButton>
                        </Box>
                    )}
                </Toolbar>
            </AppBar>
            <Drawer
                anchor="left"
                open={drawerOpen}
                onClose={toggleDrawer(false)}
            >
                {list}
            </Drawer>
        </div>
    );
}

export default NavBar;
