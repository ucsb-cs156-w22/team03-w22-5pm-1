import React from 'react';

import EarthquakesForm from "main/components/Earthquakes/EarthquakesForm"
import { earthquakesFixtures } from 'fixtures/earthquakeFixtures';

export default {
    title: 'components/Earthquakes/EarthquakesForm',
    component: EarthquakesForm
};


const Template = (args) => {
    return (
        <EarthquakesForm {...args} />
    )
};

export const Default = Template.bind({});

Default.args = {
    submitText: "Create",
    submitAction: () => { console.log("Submit was clicked"); }
};

export const Show = Template.bind({});

Show.args = {
    earthquakesFixtures: earthquakesFixtures.oneEarthquake,
    submitText: "",
    submitAction: () => { }
};
