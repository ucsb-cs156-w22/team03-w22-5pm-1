import React from "react";
import OurTable from "main/components/OurTable";

export default function EarthquakesTable({ Features, currentUser }) {

    const columns = [
        {
            Header: 'Title',
            accessor: 'title', // accessor is the "key" in the data
        },
        {
            Header: 'Magnitude',
            accessor: 'mag',
        },
        {
            Header: 'Place',
            accessor: 'place',
        },
        {
            Header: 'Time',
            accessor: 'time',
        }
    ];

    // Stryker disable ArrayDeclaration : [columns] and [students] are performance optimization; mutation preserves correctness
    const memoizedColumns = React.useMemo(() => columns, [columns]);
    const memoizedDates = React.useMemo(() => Features, [Features]);
    // Stryker enable ArrayDeclaration

    return <OurTable
        data={memoizedDates}
        columns={memoizedColumns}
        testid={"EarthquakesTable"}
    />;
};