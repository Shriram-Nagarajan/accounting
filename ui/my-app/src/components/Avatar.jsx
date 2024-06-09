// Avatar component
import React from 'react';
import Avatar from '@mui/material/Avatar';

const UserAvatar = ({ userName }) => {
  const firstLetter = userName ? userName.charAt(0).toUpperCase() : '';
  
  return (
    <Avatar>{firstLetter}</Avatar>
  );
};

export default UserAvatar;
