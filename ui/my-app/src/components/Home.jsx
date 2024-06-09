
// src/Home.js
import React from 'react';
import styled from 'styled-components';
import { useSpring, animated } from 'react-spring';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFileExcel, faPlusCircle, faChartPie } from '@fortawesome/free-solid-svg-icons';
import '@fontsource/roboto';
// import manualEntryImage from './assets/manual-entry.jpg';
// import excelUploadImage from './assets/excel-upload.jpg';
// import insightsImage from './assets/insights.jpg';


const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100%;
  background: transparent;
  position: relative;
  overflow: hidden;
  font-family: 'Roboto', sans-serif;
`;

const BackgroundImage = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: "../common/assets/expense.jpg";
  // background-size: cover;
  // background-position: center;
  // filter: blur(5px);
  // z-index: -1;
`;

const Content = styled(animated.div)`
  text-align: center;
  color: white;
  padding: 40px;
  border-radius: 15px;
  background: rgba(0, 0, 0,0.6);
  max-width: 800px;
`;

const Title = styled.h1`
  font-size: 3em;
  margin-bottom: 20px;
  font-weight: 700;
`;

const Description = styled.p`
  font-size: 1.5em;
  margin-bottom: 30px;
  line-height: 1.6;
`;

const Icons = styled.div`
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
`;

const IconItem = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
`;

const IconText = styled.p`
  margin-top: 10px;
  font-size: 1.2em;
`;
const Home = () => {
  const fadeIn = useSpring({
    from: { opacity: 0 },
    to: { opacity: 1 },
    config: { duration: 1000 },
  });

  return (
    <Container>
      <BackgroundImage />
      <Content style={fadeIn}>
        <Title>Welcome to Expense Tracker</Title>
        <Description>
          Easily input your expense data and gain insights into your spending and income.
          You can enter each expense manually or upload an Excel file.
        </Description>
        <Icons>
          <IconItem>
            <FontAwesomeIcon icon={faPlusCircle} size="3x" />
            <IconText>Manual Entry</IconText>
          </IconItem>
          <IconItem>
            <FontAwesomeIcon icon={faFileExcel} size="3x" />
            <IconText>Excel Upload</IconText>
          </IconItem>
          <IconItem>
            <FontAwesomeIcon icon={faChartPie} size="3x" />
            <IconText>Insights</IconText>
          </IconItem>
        </Icons>
      </Content>
    </Container>
  );
};


export default Home;

// // src/Home.js
// import React from 'react';
// import styled from 'styled-components';
// import { useSpring, animated } from 'react-spring';

// const Home = () => {
//   const fadeIn = useSpring({
//     from: { opacity: 0 },
//     to: { opacity: 1 },
//     config: { duration: 1000 }
//   });

//   return (
//     <Container>
//       <BackgroundImage />
//       <Content style={fadeIn}>
//         <Title>Welcome to Expense Tracker</Title>
//         <Description>
//           Easily input your expense data and gain insights into your spending and income. 
//           You can enter each expense manually or upload an Excel file.
//         </Description>
//       </Content>
//     </Container>
//   );
// };

// const Container = styled.div`
//   display: flex;
//   flex-direction: column;
//   align-items: center;
//   justify-content: center;
//   height: 100vh;
//   width: 100%;
//   background: #f5f5f5;
//   position: relative;
//   overflow: hidden;
// `;

// const BackgroundImage = styled.div`
//   position: absolute;
//   top: 0;
//   left: 0;
//   width: 100%;
//   height: 100%;
//   background-image: url('https://source.unsplash.com/random/1920x1080');
//   background-size: cover;
//   background-position: center;
//   filter: blur(5px);
//   z-index: -1;
// `;

// const Content = styled(animated.div)`
//   text-align: center;
//   color: white;
//   padding: 20px;
//   border-radius: 10px;
//   background: rgba(0, 0, 0, 0.5);
// `;

// const Title = styled.h1`
//   font-size: 3em;
//   margin-bottom: 20px;
// `;

// const Description = styled.p`
//   font-size: 1.5em;
// `;

// export default Home;


// // import React, { useState } from 'react';
// // import accountingApi from '../httputil/accountingApi';

// // function Home() {
// // //   const [selectedFile, setSelectedFile] = useState(null);
// // //   const [uploadStatus, setUploadStatus] = useState("");

// // //   const handleFileChange = (event) => {
// // //     setSelectedFile(event.target.files[0]);
// // //   };

// // //   const handleSubmit = (event) => {
// // //     event.preventDefault();
// // //     if (selectedFile) {
// // //       const formData = new FormData();
// // //       formData.append('file', selectedFile);
// // //       // You can now send formData to your backend server using fetch or Axios
// // //       console.log('File ready to be uploaded:', formData);
// // //       accountingApi.uploadExpensesFile(formData, (response) => {
// // //         setUploadStatus(response.data?.message);
// // //       }, (error) => {
// // //         setUploadStatus("Error occurred:" + error);
// // //       })
// // //     } else {
// // //       console.log('No file selected');
// // //     }
// // //   };

// //   return (
// //     <div>
// //       <h2>Home Component</h2>
// //     </div>
// //   );
// // }

// // export default Home;
