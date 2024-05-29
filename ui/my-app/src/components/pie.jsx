import React from 'react';
import { Pie } from 'react-chartjs-2';

function PieChart(props) {

    return (
        <div style={{ width: '100%', height: '300px' }}>
            <Pie
                data={props.data}
                options={{
                    title: {
                        display: true,
                        text: 'Expenditure made',
                        fontSize: 20
                    },
                    legend: {
                        display: true,
                        position: 'left',
                    },
                        responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                       
                    },
                    layout: {
                        padding: {
                            left: 0,
                            right: 0,
                            top: 0,
                            bottom: 0,
                        },
                    },
                }}
            />
        </div>
    )
}

export default PieChart;