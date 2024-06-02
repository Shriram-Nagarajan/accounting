import React from 'react';
import { Pie } from 'react-chartjs-2';
// import { Chart as ChartJS, ArcElement, Tooltip, Legend ,Colors} from 'chart.js';
// ChartJS.register(ArcElement, Tooltip, Legend,Title);

function PieChart(props) {
    return (
        <div style={{ width: '100%', height: '300px' }}>
            <Pie
                data={props.data}
                options={{
                    // title: {
                    //     display: true,
                    //     text: 'Expenditure made',
                    //     fontSize: 20
                    // },
                    // legend: {
                    //     display: true,
                    //     position: 'left',
                    // },
                        responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                        },
                        title: {
                            display: true,
                            text : props.title,
                            font: {
                                size: 20, // Increased font size for the title
                            },
                        },
                        tooltip: {
                            enabled: true,
                        }
                       
                    },
                    layout: {
                        padding: {
                            left: 0,
                            right: 0,
                            top: 0,
                            bottom: 0,
                        },
                    },
                    onHover: (event, chartElement) => {
                        event.native.target.style.cursor = chartElement.length ? 'pointer' : 'default';
                    },
                    onClick:(event,element) =>
                        {
                            console.log(event);
                            console.log(element);//element[0].index;
                            console.log(element[0].index);
                            console.log(props.data);//props.data.labels[element[0].index]
                            console.log(props.data.labels[element[0].index]);
                            props.onSliceClick(props.data.labels[element[0].index]);
                        },
                }}
            />
        </div>
    )
}

export default PieChart;