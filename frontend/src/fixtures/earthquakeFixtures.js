const earthquakeFixtures = {
  oneEarthquake: {
    minMag: 6.9,
    dist: 1.6,
  },
  twoEarthquakes: [
    {
      title: "M 6.9 - 10km ESE of Ojai, CA",
      mag: 6.9,
      place: "Ojai, CA",
      time: 1644571919000,
    },
    {
      title: "M 2.8 - 9kmN of San Juan Bautista, CA",
      mag: 2.8,
      place: "San Juan Bautista",
      time: 1644571919000,
    },
  ],
  twoEarthquakeResponses: [
    {
      _id: 12,
      id: 12,
      geometry: {
        type: "Hello",
      },
      type: "Features",
      properties: {
        mag: 5.8,
        place: "Santa Barbara",
        time: 12342134234,
        url: "",
        detail: "",
        felt: "",
        type: "Earthquake",
        title: "M 5.8 - Santa Barbara",
        status: "Ok",
      },
    },
    {
      _id: 13,
      id: 13,
      geometry: {
        type: "Hello",
      },
      type: "Features",
      properties: {
        mag: 2,
        place: "Santa Barbara",
        time: 12342134234,
        url: "",
        detail: "",
        felt: "",
        type: "Earthquake",
        title: "M 2 - Santa Barbara",
        status: "Ok",
      },
    },
  ],
};

export { earthquakeFixtures };
