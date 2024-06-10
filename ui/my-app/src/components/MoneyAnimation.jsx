// MoneyAnimation.js
import React from 'react';
import { useState,useEffect } from 'react';
import styled, { keyframes } from 'styled-components';
import { useSpring, animated } from 'react-spring';
import moneyImage from '../common/assets/money5.png';

const fall = keyframes`
  0% {
    transform: translateY(-100%) rotate(0deg);
  }
  100% {
    transform: translateY(100vh) rotate(360deg);
  }
`;

const MoneyWrapper = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  overflow: hidden;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 9999;
`;

const Money = styled.div`
  position: absolute;
  top: -50px;
  width: 50px; /* Adjust the width as needed */
  height: 50px; /* Adjust the height as needed */
  background-image: url(${moneyImage});
  background-size: contain; /* Ensure the entire image fits */
  background-repeat: no-repeat; /* Prevent image repetition */
  animation: ${fall} 5s linear infinite;
  animation-timing-function: ease-in-out;
`;

const generateMoney = (isActive) => {
    if (!isActive) return null; // Stop generating money if not active
    const moneyItems = [];
    const numItems = 50; // Number of money items
    for (let i = 0; i < numItems; i++) {
      const delay = Math.random() * 5; // Random delay up to 5 seconds
      const xPosition = Math.random() * 100; // Random horizontal position
      const duration = 3 + Math.random() * 3; // Random duration between 3 and 6 seconds
      moneyItems.push(
        <Money
          key={i}
          style={{
            left: `${xPosition}%`, // Use percentage for positioning within the wrapper
            animationDelay: `${delay}s`,
            animationDuration: `${duration}s`,
          }}
        />
      );
    }
    return moneyItems;
  };
  
  const MoneyAnimation = () => {
    const [isActive, setIsActive] = useState(true);
  
    useEffect(() => {
      const timer = setTimeout(() => {
        setIsActive(false);
      }, 10000); // Stop the animation after 10 seconds
  
      return () => clearTimeout(timer); // Cleanup the timer
    }, []);
  
    return <MoneyWrapper>{generateMoney(isActive)}</MoneyWrapper>;
  };
  
  export default MoneyAnimation;