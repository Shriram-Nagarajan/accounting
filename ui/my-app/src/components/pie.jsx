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
                            callbacks: {
                                label: function(tooltipItem) {
                                    const data = tooltipItem.chart.data;
                                    const dataset = data.datasets[tooltipItem.datasetIndex];
                                    const total = dataset.data.reduce((sum, value) => sum + value, 0);
                                    const currentValue = dataset.data[tooltipItem.dataIndex];
                                    const percentage = ((currentValue / total) * 100).toFixed(2);
                                    return `Spent: ${currentValue} (${percentage}%)`;
                                    //${data.labels[tooltipItem.dataIndex]}
                                }
                            }
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
                    onClick: (event, element) => {
                        if (element.length > 0) {
                            console.log(props.data.labels[element[0].index]);
                            props.onSliceClick(props.data.labels[element[0].index]);
                        }
                    },
                }}
            />
        </div>
    )
}

export default PieChart;