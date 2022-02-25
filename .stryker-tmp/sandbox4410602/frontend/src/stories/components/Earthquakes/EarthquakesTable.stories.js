import React from 'react';

import EarthquakesTable from 'main/components/Earthquakes/EarthquakesTable';
import { earthquakeFixtures } from 'fixtures/earthquakeFixtures';

export default {
    title: 'components/Earthquakes/EarthquakesTable',
    component: EarthquakesTable
};

const Template = (args) => {
    return (
        <EarthquakesTable {...args} />
    )
};

export const Empty = Template.bind({});

Empty.args = {
    Features: []
};

export const TwoEarthquakes = Template.bind({});

TwoEarthquakes.args = {
    Features: earthquakeFixtures.twoEarthquakes
};


