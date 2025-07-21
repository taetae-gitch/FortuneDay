import React, { useState } from "react";

export default function Header() {
  
  const [anchorEl, setAnchorEl] = useState(null);
  const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = useState(null);

  const isMenuOpen =Boolean(anchorEl);
  const isMobileMenu =Boolean(mobileMoreAnchorEl);

  const handleProfileMenuOpen =(event) => setAnchorEl(event.currentTarget);

  const handleMenuClose = (event) => setAnchorEl(null);

  const handleMobileMenuClose =(event) => setMobileMoreAnchorEl(null);

  const handleMobileProfileMenuOpen =(event) => {
    setMobileMoreAnchorEl(event.currentTarget);
    
   }

  const menuId = 'primary-search-account-menu';
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin ={{vertical:'top', horizontial: 'right',}}
      open={isMenuOpen}
      onClose={handleMenuClose}>
        <MenuItem onClick={handleMenuClose}>Profile</MenuItem>
        <MenuItem onClick={handleMenuClose}>My account</MenuItem>
      </Menu>
  )

  return(
    <AppBar>
      <Toolbar></Toolbar>
    </AppBar>
  )
}