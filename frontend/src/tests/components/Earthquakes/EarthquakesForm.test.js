import { render, waitFor, fireEvent } from "@testing-library/react";
import EarthquakesForm from "main/components/Earthquakes/EarthquakesForm";
import { earthquakesFixtures } from "fixtures/earthquakeFixtures";
import { BrowserRouter as Router } from "react-router-dom";

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockedNavigate
}));


describe("EarthquakesForm tests", () => {

    test("renders correctly ", async () => {

        const { getByText } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByText(/Minimum Magnitude/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Distance/)).toBeInTheDocument());
        await waitFor(() => expect(getByText(/Retrieve/)).toBeInTheDocument());
    });


    test("renders correctly when passing in a Earthquake ", async () => {

        const { getByText, getByTestId } = render(
            <Router  >
                <EarthquakesForm initialEarthquakes={earthquakesFixtures.oneEarthquake} />
            </Router>
        );
        await waitFor(() => expect(getByTestId(/EarthquakesForm-minMag/)).toBeInTheDocument());
        expect(getByText(/minMag/)).toBeInTheDocument();
        expect(getByTestId(/EarthquakesForm-minMag/)).toHaveValue(6.9);

        await waitFor(() => expect(getByTestId(/EarthquakesForm-dist/)).toBeInTheDocument());
        expect(getByText(/dist/)).toBeInTheDocument();
        expect(getByTestId(/EarthquakesForm-dist/)).toHaveValue(1.6);
    });


    test("Correct Error messages on bad input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-minMag")).toBeInTheDocument());
        const minMag = getByTestId("EarthquakesForm-minMag");
        const dist = getByTestId("EarthquakesForm-dist");
        const submitButton = getByTestId("EarthquakesForm-submit");

        fireEvent.change(minMag, { target: { value: 'bad-input' } });
        fireEvent.change(dist, { target: { value: 'bad-input' } });
        fireEvent.click(submitButton);

        await waitFor(() => expect(getByText(/Minimum Magnitude of an earthquake must be a number, e.g. 5.8/)).toBeInTheDocument());
        expect(getByText(/Distance in km must be a number, e.g. 1.6km'/)).toBeInTheDocument();
    });

    test("Correct Error messages on missing input", async () => {

        const { getByTestId, getByText } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-submit")).toBeInTheDocument());
        const submitButton = getByTestId("EarthquakesForm-submit");

        fireEvent.click(submitButton);

        await waitFor(() => expect(getByText(/Minimum Magnitude of an earthquake is required./)).toBeInTheDocument());
        expect(getByText(/Distance in km from Storke Tower is required./)).toBeInTheDocument();
    });

    test("No Error messsages on good input", async () => {

        const mockSubmitAction = jest.fn();


        const { getByTestId, queryByText } = render(
            <Router  >
                <EarthquakesForm submitAction={mockSubmitAction} />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-minMag")).toBeInTheDocument());

        const minMag = getByTestId("EarthquakesForm-minMag");
        const dist = getByTestId("EarthquakesForm-dist");
        const submitButton = getByTestId("EarthquakesForm-submit");

        fireEvent.change(minMag, { target: { value: 1.2 } });
        fireEvent.change(dist, { target: { value: 12 } });
        fireEvent.click(submitButton);

        await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

        expect(queryByText(/Minimum Magnitude of an earthquake must be a number, e.g. 5.8/)).not.toBeInTheDocument();
        expect(queryByText(/Distance in km must be a number, e.g. 1.6km/)).not.toBeInTheDocument();

    });


    test("Test that navigate(-1) is called when Cancel is clicked", async () => {

        const { getByTestId } = render(
            <Router  >
                <EarthquakesForm />
            </Router>
        );
        await waitFor(() => expect(getByTestId("EarthquakesForm-cancel")).toBeInTheDocument());
        const cancelButton = getByTestId("EarthquakesForm-cancel");

        fireEvent.click(cancelButton);

        await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));

    });

});


